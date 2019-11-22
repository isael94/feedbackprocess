package com.fs.springboot.dao;

import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;

public interface ToneAnalyzerDao {
    ToneAnalysis selectToneOptions (String text);
}
