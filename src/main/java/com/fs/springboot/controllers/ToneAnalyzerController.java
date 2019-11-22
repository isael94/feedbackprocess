package com.fs.springboot.controllers;

import com.fs.springboot.services.ToneAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/toneAnalyzer")
@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class ToneAnalyzerController {

    private final ToneAnalyzerService toneAnalyzerService;

    @Autowired
    public ToneAnalyzerController(ToneAnalyzerService toneAnalyzerService) {
        this.toneAnalyzerService = toneAnalyzerService;
    }

    @ResponseBody
    @RequestMapping( value  = "/getToneOptions",  method = RequestMethod.POST,
            consumes = MediaType.TEXT_PLAIN_VALUE)

    public String getToneOptions(@RequestParam("text") String text) {
        return toneAnalyzerService.getToneOptions(text).toString();
    }
}
