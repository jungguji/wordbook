package com.jgji.spring.domain.global.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static final String FILE_NAME = "src/main/resources/message.properties";

    public static String getMessage(String key) {
        String value = "";
        try(FileInputStream propFis = new FileInputStream(FILE_NAME)) {
            Properties properties = new Properties();
            properties.load(new BufferedInputStream(propFis));

            value = properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }
}
