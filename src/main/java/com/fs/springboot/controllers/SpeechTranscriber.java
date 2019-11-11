package com.fs.springboot.controllers;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.fs.springboot.models.SpeechPost;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/speech")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class SpeechTranscriber {

	@ResponseBody
	@RequestMapping( value  = "/transcribe",  method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public String transcribe(@RequestParam("file") String file) throws Exception{

		Base64.Decoder decoder = Base64.getDecoder();

		file = String.valueOf(file);

		byte[] decodedByte = decoder.decode(file.split(",")[1]);

		File convertFile = new File("/var/tmp/audio.webm");
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(decodedByte);
		fout.close();



		Authenticator authenticator = new IamAuthenticator("6bHdpbwdbEVx4EmuCIxQuRTV4KZtKL8UT8AjFgDkKFcm");
		SpeechToText service = new SpeechToText(authenticator);

		File audio = new File(convertFile.getPath());

		RecognizeOptions options = null;
		try {
			options = new RecognizeOptions.Builder()
					.audio(audio)
					.contentType(HttpMediaType.AUDIO_WEBM)
					.model("es-MX_BroadbandModel")
					.build();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		SpeechRecognitionResults transcript = service.recognize(options).execute().getResult();
		System.out.println(transcript);


		return transcript.toString();
	}
	
	
	
}
