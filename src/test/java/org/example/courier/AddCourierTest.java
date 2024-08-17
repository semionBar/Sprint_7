package org.example.courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;


public class AddCourierTest {
    Courier courier;

    CourierStep courierStep;

    @Before
    public void setUp() {

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        courierStep = new CourierStep();

        courier = new Courier("bar1234", "1234", "Semen");
    }
    @DisplayName("Добавление нового курьера")
    @Test
    public void addNewCourierTestReturnsOkTrue200() {

        courierStep.sendCreateNewCourierRequest(courier);

        courierStep.checkResponseCode(SC_CREATED);

        courierStep.checkBodyFieldEqualsTrue(true, "ok");

    }

    @Test
    public void addTwoEqualCouriersReturns409() {

        courierStep.sendCreateNewCourierRequest(courier);

        courierStep.sendCreateNewCourierRequest(courier);

        courierStep.checkResponseCode(SC_CONFLICT);

    }
    @Test
    public void addTwoCouriersWithSameLoginReturns409() {


        courierStep.sendCreateNewCourierRequest(courier);

        courierStep.setNewFieldToCourier(courier, "bar1234", "4321", "Semion");

        courierStep.sendCreateNewCourierRequest(courier);

        courierStep.checkResponseCode(SC_CONFLICT);

        courierStep.resetCourier(courier);

    }


    @Test
    public void addNewCourierNoLoginDataReturns400() {

        courierStep.setNewFieldToCourier(courier, null, "1234", "Semen");

        courierStep.sendCreateNewCourierRequest(courier);

        courierStep.checkResponseCode(SC_BAD_REQUEST);

    }

    @Test
    public void addNewCourierNoPasswordDataReturns400() {

        courierStep.setNewFieldToCourier(courier, "bar1234", null, "Semen");

        courierStep.sendCreateNewCourierRequest(courier);

        courierStep.checkResponseCode(SC_BAD_REQUEST);

    }

    @After
    public void deleteCourier() {
        courierStep.resetCourier(courier);
        courierStep.deleteUserIfNeeded(courier);
    }
}
