package com.fs.springboot.dao;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;
import org.springframework.stereotype.Repository;

@Repository("toneAnalyzer")

public class ToneAnalyzerAccessService implements  ToneAnalyzerDao {

    @Override
    public ToneAnalysis selectToneOptions(String text) {

        final IamAuthenticator auth2 = new IamAuthenticator("eq1TPfhTdezcovjoXPeSY50PJhPCj9pZwkQwsrfyGtCN");

        ToneAnalysis tone = new ToneAnalysis();
        try {
            // Invoke a Tone Analyzer method

            ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", auth2);

            // Call the service and get the tone ****General****
            ToneOptions toneOptions = new ToneOptions.Builder()
                    .text(text)
                    .build();

            //General
            tone = toneAnalyzer.tone(toneOptions).execute().getResult();
        } catch (NotFoundException e) {
            // Handle Not Found (404) exception
            System.out.println("Not Found Exception (404)");
            e.printStackTrace();
        } catch (RequestTooLargeException e) {
            // Handle Request Too Large (413) exception
            System.out.println("Request Too Large Exception (413)");
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            // Base class for all exceptions caused by error responses from the service
            System.out.println("Service returned status code "
                    + e.getStatusCode() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return tone;
    }
}
