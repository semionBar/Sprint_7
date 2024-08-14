package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class DeleteCourierTest {

    CourierLoggedIn courierLoggedIn;
    Courier courier;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        courier = new Courier("s3m3n1337", "1234", "Semen");

        Response addUserResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        System.out.println(addUserResponse.body().asString());

        Response loginUserResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        System.out.println(loginUserResponse.body().asString());
        courierLoggedIn = loginUserResponse.as(CourierLoggedIn.class);
    }

    @Test
    public void deleteCourierTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLoggedIn)
                .when()
                .delete("/api/v1/courier/" + courierLoggedIn.getId());
        System.out.println(response.body().asString());
    }
}
