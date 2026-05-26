package com.thirdeye30.resumehelper.messagebroker.utils;

import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {

    @Value("${thirdeye.istesting}")
    private Integer testing;

    @Value("${thirdeye.istesting.code}")
    private String testingCode;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";
    private static final SecureRandom random = new SecureRandom();

    public String generateUniqueCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return (testing != 1) ? sb.toString() : testingCode;
    }
}

