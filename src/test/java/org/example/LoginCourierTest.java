package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class LoginCourierTest {

    Courier courier;
    CourierLoggedIn courierLoggedIn;

    @Before
    public void setUp() {
        courier = new Courier("s3m3n1337", "1234", "Semen");

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        System.out.println(response.body().asString());
    }

    @Test
    public void loginUserTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        courierLoggedIn = response.as(CourierLoggedIn.class);
        System.out.println(courierLoggedIn.getId());
    }

    @After
    public void deleteUser() {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLoggedIn)
                .when()
                .delete("/api/v1/courier/" + courierLoggedIn.getId());
        System.out.println(response.body().asString());

    }
}
