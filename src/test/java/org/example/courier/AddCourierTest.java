package org.example.courier;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierLoggedIn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AddCourierTest {

    CourierLoggedIn courierLoggedIn;
    Courier courier;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void addNewCourierTestReturnsOkTrue200() {
        courier = new Courier("s3m3n1337", "1234", "Semen");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        System.out.println(response.body().asString());
    }

    @Test
    public void addTwoEqualCouriersReturns409() {
        courier = new Courier("s3m3n1337", "1234", "Semen");
        Response response1 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        System.out.println(response1.body().asString());

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier").then().statusCode(409);

    }

    @Test
    public void noLoginDataReturns400() {
        courier = new Courier(null, "1234", "Semen");
        Gson gson = new Gson();
        String json = gson.toJson(courier);
        System.out.println(json);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier").then().statusCode(400);

    }

    @Test
    public void noPasswordDataReturns400() {

    }

    @Test
    public void noFirstNameDataReturns400() {

    }

    @After
    public void clearUserData() {
        courierLoggedIn = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login").as(CourierLoggedIn.class);
        System.out.println(courierLoggedIn.getId());

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLoggedIn)
                .when()
                .delete("/api/v1/courier/" + courierLoggedIn.getId());
        System.out.println(response.body().asString());
    }
}
