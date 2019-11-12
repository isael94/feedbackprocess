package com.fs.springboot.controllers;

import com.fs.springboot.services.ToneAnalyzerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/toneAnalyzer")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class ToneAnalyzerController {

    @ResponseBody
    @RequestMapping( value  = "/getToneOptions",  method = RequestMethod.POST,
            consumes = MediaType.TEXT_PLAIN_VALUE)
    public String getToneOptions(@RequestParam("text") String text) {
        String result = "";
        try {
            ToneAnalyzerService toneAnalyzerService = new ToneAnalyzerService();
            result = toneAnalyzerService.getToneOptions(text).toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
