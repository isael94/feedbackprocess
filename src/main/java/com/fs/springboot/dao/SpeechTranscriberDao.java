package com.fs.springboot.dao;

import java.io.IOException;

public interface SpeechTranscriberDao {
    String transcribe(String file) throws IOException;
}
