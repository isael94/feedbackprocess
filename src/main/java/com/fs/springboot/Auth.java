package com.fs.springboot;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Auth {

    private final Authenticator authenticator = new IamAuthenticator("6bHdpbwdbEVx4EmuCIxQuRTV4KZtKL8UT8AjFgDkKFcm");

    @Bean
    public Authenticator getAuthenticator() {
        return authenticator;
    }
}
