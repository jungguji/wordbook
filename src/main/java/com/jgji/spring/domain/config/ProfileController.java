package com.jgji.spring.domain.config;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
//    private final Environment env;
//
//    public ProfileController(Environment env) {
//        this.env = env;
//    }
//
//    @GetMapping("/profile")
//    public String getProfile() {
//        List<String> profiles = Arrays.asList(env.getActiveProfiles());
//        List<String> prodProfiles = Arrays.asList("prod", "prod1", "prod2");
//        String defaultProfile = profiles.isEmpty() ? "default" : prodProfiles.get(0);
//
//        System.out.println(" @@@@ getProfile() @@@@ ");
//        return profiles.stream()
//                .filter(prodProfiles::contains)
//                .findAny()
//                .orElse(defaultProfile);
//
//        return "test";
//    }
}
