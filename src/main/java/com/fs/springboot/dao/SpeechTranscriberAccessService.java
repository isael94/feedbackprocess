package com.fs.springboot.dao;

import com.fs.springboot.services.LogService;
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

        File convertFile = new File("/var/tmp/audio.webm");
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
        System.out.println(transcript);

        List<SpeechRecognitionResult> result = transcript.getResults();

        ToneAnalysis toneText = null;

        for (SpeechRecognitionResult i : result ) {

            List<SpeechRecognitionAlternative> alternative = i.getAlternatives();
            SpeechRecognitionAlternative first = alternative.stream().findFirst().get();
            String transcriptText = first.getTranscript();
            toneText = toneAnalyzerService.getToneOptions(transcriptText);

        }

        System.out.printf(toneText.toString());

        logService.setLogService(toneText.toString(), "200", ToneAnalyzerService.class.getName() );
        logService.setLogService(result.toString(), "200", "SpeechTranscribir");


        return transcript.toString();
    }
}
