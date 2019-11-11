package com.fs.springboot.controllers;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fs.springboot.models.SpeechPost;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
@RestController
@RequestMapping("/speech")
public class SpeechTranscriber {
	@ResponseBody
	@RequestMapping("/transcribe")
	public String transcribe(SpeechPost post) throws Exception{
		String text = null;
		
		byte[] byteArray = Base64.getDecoder().decode(post.getAudio());
		
		File tempFile = File.createTempFile("speech-", ".flac",null);
		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(byteArray);
		fos.close();
		
		SpeechToText service = new SpeechToText();
		service.setApiKey("_lxTv_FWxW5UGkWjVVGnxKvYpqM7VLrh4skQqMp7noR0");
		service.setEndPoint("https://gateway-wdc.watsonplatform.net/speech-to-text/api");
		
		RecognizeOptions options = new RecognizeOptions.Builder()
				.contentType(HttpMediaType.AUDIO_WAV)
				.build();
		
		SpeechResults result = service.recognize(tempFile, options).execute();
		
		if (!result.getResults().isEmpty()) {
			List<Transcript> transcripts = result.getResults();
			
			for(Transcript transcript: transcripts) {
				text = transcript.getAlternatives().get(0).getTranscript();
				break;
			}
			
		}
		return text;
	}
	
	
	
}
