package org.example.courier;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static io.restassured.RestAssured.given;


public class LoginCourierTest {

    Courier courier;
    CourierLoggedIn courierLoggedIn;

    @Before
    public void setUp() throws FileNotFoundException {

        resetCourier();

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }


    @Step("Отправить запрос логина")
    public Response sendLoginRequest() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Проверить, что код ответа соответствует ожидаемому")
    public void checkStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Проверить, что поле из body не пустое")
    public void checkBodyField(Response response, String field) {
        response.then().assertThat().body(field, Matchers.notNullValue());
    }

    @Step("Изменить данные курьера")
    public void setNewFieldToCourier(String login, String password, String firstName) {
        courier.setLogin(login);
        courier.setPassword(password);
        courier.setFirstName(firstName);
    }

    @Test
    public void loginUserTest() {
        Response response = sendLoginRequest();

        checkStatusCode(response, 200);

        checkBodyField(response, "id");

        courierLoggedIn = response.as(CourierLoggedIn.class);
    }

    @Test
    public void loginCourierNoLoginDataReturns400() {

        setNewFieldToCourier(null, "1234", null);

        Response response = sendLoginRequest();

        checkStatusCode(response, 400);

        checkBodyField(response, "message");

    }

    @Test
    public void loginCourierNoPasswordDataReturns400() {

    //Gри отсутствии поля password сервер не отвечает

        setNewFieldToCourier("s3m3n1337", "", null);

        Response response = sendLoginRequest();

        checkStatusCode(response,400);

        checkBodyField(response, "message");

    }

    @Test
    public void loginCourierWrongPasswordReturns404() {

        setNewFieldToCourier("s3m3n1337", "12345", null);

        Response response = sendLoginRequest();

        checkStatusCode(response,404);

        checkBodyField(response, "message");
    }

    @Test
    public void loginCourierWrongLoginReturns404() {

        setNewFieldToCourier("12345", "1234", null);

        Response response = sendLoginRequest();

        checkStatusCode(response,404);

        checkBodyField(response, "message");

    }

    @Test
    public void loginCourierWrongLoginAndPasswordReturns404() {

        setNewFieldToCourier("asdfasdf", "12345", null);

        Response response = sendLoginRequest();

        response.then().statusCode(404);

        checkBodyField(response, "message");
    }

    @After
    public void deleteUser() throws FileNotFoundException {
        resetCourier();

        if (courierLoggedIn == null) {
            courierLoggedIn = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(courier)
                    .when()
                    .post("/api/v1/courier/login").as(CourierLoggedIn.class);
        }
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLoggedIn)
                .when()
                .delete("/api/v1/courier/" + courierLoggedIn.getId());


    }
    public void resetCourier() throws FileNotFoundException {

        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("C:\\Sprint_7\\src\\test\\resources\\LoginCourierTestData.json"));
        courier = gson.fromJson(br, Courier.class);
    }
}
