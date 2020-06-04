package com.jgji.spring.domain.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileControllerUnitTest {

    @Test
    public void prod_profile이_조회된다() {
        String expectedProfile = "prod";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);

        ProfileController controller = new ProfileController(env);

        String profile = controller.getProfile();

        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void prod_profile이_없으면_default가_조회된다() {
        //given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        ProfileController controller = new ProfileController(env);

        //when
        String profile = controller.getProfile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}