package com.example.backend.utils;

import io.restassured.RestAssured;

public class RequestSpec {

    public RequestSpec() {
    }

    public static void on(int port) {
        RestAssured.port = port;
    }
}
