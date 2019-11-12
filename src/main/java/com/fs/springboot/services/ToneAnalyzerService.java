package com.fs.springboot.services;

import com.fs.springboot.dao.ToneAnalyzerDao;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ToneAnalyzerService {

    private final ToneAnalyzerDao toneAnalyzerDao;

    @Autowired
    public ToneAnalyzerService( @Qualifier("toneAnalyzer") ToneAnalyzerDao toneAnalyzerDao) {
        this.toneAnalyzerDao = toneAnalyzerDao;
    }


    public ToneAnalysis getToneOptions (String text) {
        return toneAnalyzerDao.selectToneOptions(text);
    }

}
