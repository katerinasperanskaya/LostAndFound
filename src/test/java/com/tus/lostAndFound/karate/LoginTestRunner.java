package com.tus.lostAndFound.karate;

import com.intuit.karate.junit5.Karate;

public class LoginTestRunner {

    @Karate.Test
    Karate testLogin() {
        return Karate.run("classpath:karate/login.feature");
    }
}

