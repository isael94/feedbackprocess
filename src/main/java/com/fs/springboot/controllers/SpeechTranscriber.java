package com.fs.springboot.controllers;


import com.fs.springboot.services.SpeechTranscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/speech")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class SpeechTranscriber {

    final private SpeechTranscriberService speechTranscriberService;

    @Autowired
    public SpeechTranscriber(SpeechTranscriberService speechTranscriberService) {
        this.speechTranscriberService = speechTranscriberService;
    }

    @ResponseBody
	@RequestMapping( value  = "/transcribe",  method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String transcribe(@RequestParam("file") String file) throws IOException {
        return speechTranscriberService.transcribe(file);
	}
	
	
	
}
