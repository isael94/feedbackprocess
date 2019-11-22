package com.fs.springboot.services;

import com.fs.springboot.dao.SpeechTranscriberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public class SpeechTranscriberService {

    private final SpeechTranscriberDao speechTranscriberDao;

     @Autowired
    public SpeechTranscriberService( @Qualifier("speechTranscriberDao") SpeechTranscriberDao speechTranscriberDao) {
        this.speechTranscriberDao = speechTranscriberDao;
    }

    public String transcribe(String file) throws IOException {
         return speechTranscriberDao.transcribe(file);
    }
}
