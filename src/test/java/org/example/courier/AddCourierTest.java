package org.example.courier;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AddCourierTest {

    CourierLoggedIn courierLoggedIn;
    Courier courier;
    boolean isCourierCreated = false;

    public void resetCourier() throws FileNotFoundException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("C:\\Sprint_7\\src\\test\\resources\\AddCourierTestData.json"));
        courier = gson.fromJson(br, Courier.class);
    }
    @Before
    public void setUp() throws FileNotFoundException {

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        resetCourier();
    }

    @Step("Отправить запрос на создание курьера")
    public Response sendCreateNewCourierResponse() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Изменить данные курьера")
    public void setNewFieldToCourier(String login, String password, String firstName) {
        courier.setLogin(login);
        courier.setPassword(password);
        courier.setFirstName(firstName);
    }

    @Step("Проверить, что код ответа равен ожидаемому")
    public void checkResponseCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Проверить поле из body")
    public void checkBodyFieldEqualsTrue(Response response, Object object, String field) {
        response.then().assertThat().body(field,equalTo(object));
    }

    @DisplayName("Добавление нового курьера")
    @Test
    public void addNewCourierTestReturnsOkTrue200() {
        Response response = sendCreateNewCourierResponse();

        checkResponseCode(response, 201);

        checkBodyFieldEqualsTrue(response, true, "ok");

        isCourierCreated = response.statusCode() == 201 || response.statusCode() == 409;

    }

    @Test
    public void addTwoEqualCouriersReturns409() {

        Response response1 = sendCreateNewCourierResponse();

        Response response2 = sendCreateNewCourierResponse();

        checkResponseCode(response2,409);

        isCourierCreated = response1.statusCode() == 201 || response1.statusCode() == 409;
    }
    @Test
    public void addTwoCouriersWithSameLoginReturns409() {

        Response response1 = sendCreateNewCourierResponse();

        setNewFieldToCourier("s3m3n1337", "4321", "Semion");

        Response response2 = sendCreateNewCourierResponse();

        checkResponseCode(response2, 409);

        isCourierCreated = response1.statusCode() == 201 || response1.statusCode() == 409;

    }


    @Test
    public void addNewCourierNoLoginDataReturns400() {


        setNewFieldToCourier(null, "1234", "Semen");

        Response response = sendCreateNewCourierResponse();

        checkResponseCode(response, 400);

        isCourierCreated = response.statusCode() == 201 || response.statusCode() == 409;

    }

    @Test
    public void addNewCourierNoPasswordDataReturns400() {
        courier.setPassword(null);

        setNewFieldToCourier("s3m3n1337", null, "Semen");

        Response response = sendCreateNewCourierResponse();

        checkResponseCode(response, 400);

        isCourierCreated = response.statusCode() == 201 || response.statusCode() == 409;
    }

    @After
    public void clearUserData() throws FileNotFoundException {
        resetCourier();

        if (isCourierCreated) {
            courierLoggedIn = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(courier)
                    .when()
                    .post("/api/v1/courier/login").as(CourierLoggedIn.class);

            if (courierLoggedIn.getId() != null) {
                Response response = given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLoggedIn)
                        .when()
                        .delete("/api/v1/courier/" + courierLoggedIn.getId());

            }
        }
    }
}
