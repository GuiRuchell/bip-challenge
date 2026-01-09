package com.example.backend.controller;

import com.example.backend.config.IntegrationTest;
import org.junit.Test;

import java.math.BigDecimal;

import static com.example.backend.fixture.BenefitFixture.*;
import static com.example.backend.utils.BodyBuilder.with;
import static org.hamcrest.core.Is.is;

public class BenefitControllerIT extends IntegrationTest {

    @Test
    public void shouldGetAllBenefits() {
        getAllBenefits()
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldCreateBenefit() {
        var name = "Vale Alimentação";
        var description = "Benefício para compras em supermercados";
        var value = new BigDecimal("500.00");
        var active = true;

        var benefitBody = with("name", name)
                .and("description", description)
                .and("value", value)
                .and("active", active);

        postBenefit(benefitBody)
                .then()
                .statusCode(201)
                .body("name", is(name))
                .body("description", is(description))
                .body("value", is(500.00f))
                .body("active", is(true));
    }

    @Test
    public void shouldUpdateBenefitSuccessfully() {
        var initialName = "Vale Transporte";
        var initialDescription = "Benefício inicial";
        var initialValue = new BigDecimal("500.00");
        var initialActive = true;

        var createBody = with("name", initialName)
                .and("description", initialDescription)
                .and("value", initialValue)
                .and("active", initialActive);

        var benefitId = postBenefit(createBody)
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        var updatedName = "Vale Transporte Atualizado";
        var updatedDescription = "Descrição atualizada do benefício";
        var updatedValue = new BigDecimal("750.00");
        var updatedActive = true;

        var updateBody = with("name", updatedName)
                .and("description", updatedDescription)
                .and("value", updatedValue)
                .and("active", updatedActive);

        putBenefit(benefitId, updateBody)
                .then()
                .statusCode(200)
                .body("name", is(updatedName))
                .body("description", is(updatedDescription))
                .body("value", is(750.00f))
                .body("active", is(true));
    }

    @Test
    public void shouldDeleteBenefitSuccessfully() {
        var name = "Vale Transporte";
        var description = "Benefício transporte";
        var value = new BigDecimal("300.00");
        var active = true;

        var createBody = with("name", name)
                .and("description", description)
                .and("value", value)
                .and("active", active);

        var benefitId = postBenefit(createBody)
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        deleteBenefit(benefitId)
                .then()
                .statusCode(204);
    }
}
