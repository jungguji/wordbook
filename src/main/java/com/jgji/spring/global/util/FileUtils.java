package com.jgji.spring.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {

    public static final String REGEX = ".*[ㄱ-힣]+.*";

    private final static String ENCODE_UTF8 = "UTF-8";
    private final static String ENCODE_EUCKR = "EUC-KR";

    public static String getFileEncodeUTF8orEucKr(MultipartFile file) {
        String encode = ENCODE_UTF8;

        try (InputStreamReader isr = new InputStreamReader(file.getInputStream(), encode);
             BufferedReader br = new BufferedReader(isr) ) {

            if (!br.readLine().matches(REGEX)) {
                encode = ENCODE_EUCKR;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return encode;
    }
}
