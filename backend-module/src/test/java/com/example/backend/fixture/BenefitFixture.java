package com.example.backend.fixture;

import com.example.backend.utils.BodyBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BenefitFixture {

    public static Response getAllBenefits() {
        return given()
                .basePath("/api/beneficios")
                .when()
                .get();
    }

    public static Response postBenefit(BodyBuilder body) {
        return given()
                .basePath("/api/beneficios")
                .contentType(ContentType.JSON)
                .body(body.build())
                .when()
                .post();
    }

    public static Response putBenefit(Object patientId, BodyBuilder body) {
        return given()
                .basePath("/api/beneficios")
                .pathParam("id", patientId)
                .contentType(ContentType.JSON)
                .body(body.build())
                .when()
                .put("/{id}");
    }

    public static Response deleteBenefit(Object patientId) {
        return given()
                .basePath("/api/beneficios")
                .pathParam("id", patientId)
                .contentType(ContentType.JSON)
                .when()
                .delete("/{id}");
    }

}
