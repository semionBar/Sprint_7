package org.example.courier;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;


public class LoginCourierTest {

    Courier courier;
    CourierStep courierStep;

    @Before
    public void setUp() {
        courier = new Courier("bar1234", "1234", null);

        courierStep = new CourierStep();

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        courierStep.sendCreateNewCourierRequest(courier);
    }

    @Test
    public void loginUserTest() {

        courierStep.sendLoginCourierRequest(courier);

        courierStep.checkResponseCode(SC_OK);

        courierStep.checkBodyFieldExists("id");
    }

    @Test
    public void loginCourierNoLoginDataReturns400() {

        courierStep.setNewFieldToCourier(courier, null, "1234", null);

        courierStep.sendLoginCourierRequest(courier);

        courierStep.checkResponseCode(SC_BAD_REQUEST);

        courierStep.checkBodyFieldExists("message");


    }

    @Test
    public void loginCourierNoPasswordDataReturns400() {

    //При отсутствии поля password сервер не отвечает

        courierStep.setNewFieldToCourier(courier, "bar1234", "", null);

        courierStep.sendLoginCourierRequest(courier);

        courierStep.checkResponseCode(SC_BAD_REQUEST);

        courierStep.checkBodyFieldExists("message");

    }

    @Test
    public void loginCourierWrongPasswordReturns404() {

        courierStep.setNewFieldToCourier(courier, "bar1234", "12345", null);

        courierStep.sendLoginCourierRequest(courier);

        courierStep.checkResponseCode(SC_NOT_FOUND);

        courierStep.checkBodyFieldExists("message");

    }

    @Test
    public void loginCourierWrongLoginReturns404() {

        courierStep.setNewFieldToCourier(courier, "12345", "1234", null);

        courierStep.sendLoginCourierRequest(courier);

        courierStep.checkResponseCode(SC_NOT_FOUND);

        courierStep.checkBodyFieldExists("message");

    }

    @Test
    public void loginCourierWrongLoginAndPasswordReturns404() {


        courierStep.setNewFieldToCourier(courier, "asdasdf", "12345", null);

        courierStep.sendLoginCourierRequest(courier);

        courierStep.checkResponseCode(SC_NOT_FOUND);

        courierStep.checkBodyFieldExists("message");

    }

    @After
    public void deleteUser() {

        courierStep.resetCourier(courier);

        courierStep.deleteUserIfNeeded(courier);
    }
}
