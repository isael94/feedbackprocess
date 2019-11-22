package com.fs.springboot.dao;

import com.fs.springboot.services.LogService;
import com.fs.springboot.services.SpeechTranscriberService;
import com.fs.springboot.services.ToneAnalyzerService;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionAlternative;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Repository("speechTranscriberDao")
public class SpeechTranscriberAccessService implements SpeechTranscriberDao {

    private final Authenticator authenticator = new IamAuthenticator("6bHdpbwdbEVx4EmuCIxQuRTV4KZtKL8UT8AjFgDkKFcm");
    private final ToneAnalyzerService toneAnalyzerService;
    private final LogService logService;

    @Autowired
    public SpeechTranscriberAccessService(ToneAnalyzerService toneAnalyzerService, LogService logService) {
        this.toneAnalyzerService = toneAnalyzerService;
        this.logService = logService;
    }

    @Override
    public String transcribe(String file) throws IOException {
        Base64.Decoder decoder = Base64.getDecoder();

        file = String.valueOf(file);

        byte[] decodedByte = decoder.decode(file.split(",")[1]);

        File convertFile = new File("C:\\Users\\javieri.garcia\\Documents\\test.json");
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(decodedByte);
        fout.close();

        SpeechToText service = new SpeechToText(authenticator);

        File audio = new File(convertFile.getPath());

        RecognizeOptions options = null;
        try {
            options = new RecognizeOptions.Builder()
                    .audio(audio)
                    .contentType(HttpMediaType.AUDIO_WEBM)
                    .model("en-US_BroadbandModel") //spanish: es-MX_BroadbandModel
                    .build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        SpeechRecognitionResults transcript = service.recognize(options).execute().getResult();

        List<SpeechRecognitionResult> result = transcript.getResults();

        ToneAnalysis toneText = null;

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (SpeechRecognitionResult i : result ) {

            List<SpeechRecognitionAlternative> alternative = i.getAlternatives();

            for (SpeechRecognitionAlternative  alt : alternative ) {

                String transcriptText = alt.getTranscript();
                toneText = toneAnalyzerService.getToneOptions(transcriptText);

                AsyncLog task = new AsyncLog(logService, toneText.toString(), "200", ToneAnalyzerService.class.getName() );

                FutureTask<String> futureTask = new FutureTask<>(task,
                        "FutureTask is complete: " + task.hashCode());

                executor.submit(futureTask);

            }

        }

        AsyncLog task = new AsyncLog(logService, result.toString(), "200", SpeechTranscriberService.class.getName());

        FutureTask<String> futureTask = new FutureTask<>(task,
                "FutureTask is complete: " + task.hashCode());

        executor.submit(futureTask);

        System.out.println("END SERVICE");
        return transcript.toString();
    }
}

class AsyncLog implements  Runnable {

    private final LogService logService;

    private String message;

    public AsyncLog(LogService logService, String message, String status, String service) {
        this.logService = logService;
        this.message = message;
        this.status = status;
        this.service = service;
    }

    private String status;
    private String service;


    @Override
    public void run() {
        logService.setLogService(this.message, this.status, this.service);
    }
}
