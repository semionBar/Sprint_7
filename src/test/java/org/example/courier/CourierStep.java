package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.equalTo;

public class CourierStep {

    Response response;
    boolean isCourierCreated = false;

    final private String createCourierPath = "/api/v1/courier";
    final private String deleteCourierPath = "/api/v1/courier/";
    final private String loginCourierPath = "/api/v1/courier/login";

    @Step("Проверить, что поле из body не пустое")
    public void checkBodyFieldExists(String field) {
        response.then().assertThat().body(field, Matchers.notNullValue());
    }


    @Step("Отправить запрос на создание курьера")
    public void sendCreateNewCourierRequest(Courier courier) {
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(createCourierPath);

    }

    @Step("Создать курьера")
    public void createNewCourier(Courier courier) {
        sendCreateNewCourierRequest(courier);
        checkIsCourierCreated();
    }

    @Step("Проверить был ли создан курьер")
    public void checkIsCourierCreated() {
        if (response.statusCode() == SC_CREATED || response.statusCode() == SC_CONFLICT) {
            isCourierCreated = true;
        }
    }


    @Step("Отправить запрос на удаление курьера")
    public void sendDeleteCourierRequest(CourierLoggedIn courierLoggedIn) {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLoggedIn)
                .when()
                .delete(deleteCourierPath + courierLoggedIn.getId());
    }

    @Step("Отправить запрос на логин курьера")
    public CourierLoggedIn sendLoginCourierRequest(Courier courier) {
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(loginCourierPath);
        return response.as(CourierLoggedIn.class);
    }

    @Step("Проверить, что код ответа равен ожидаемому")
    public void checkResponseCode(int code) {
        response.then().statusCode(code);
    }

    @Step("Проверить поле из body")
    public void checkBodyFieldEqualsTrue(Object object, String field) {
        response.then().assertThat().body(field,equalTo(object));
    }

    @Step("Изменить данные курьера")
    public void setNewFieldToCourier(Courier courier, String login, String password, String firstName) {
        courier.setLogin(login);
        courier.setPassword(password);
        courier.setFirstName(firstName);
    }

    @Step("Вернуть поля к изначальному состоянию")
    public void resetCourier(Courier courier) {
        courier.setLogin("bar1234");
        courier.setPassword("1234");
        courier.setFirstName("Semen");
    }


    @Step("Удалить курьера, если это необходимо")
    public void deleteUserIfNeeded(Courier courier) {
        if (isCourierCreated) {
            CourierLoggedIn courierLoggedIn = new CourierLoggedIn();
            courierLoggedIn = sendLoginCourierRequest(courier);

            if (courierLoggedIn.getId() != null) {
                sendDeleteCourierRequest(courierLoggedIn);
            }

        }
    }

}
