package org.example.order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class OrderTest {

    OrderId order;
    private final String jsonName;

    public OrderTest(String jsonName) {
        this.jsonName = jsonName;
    }

    @Parameterized.Parameters // добавили аннотацию
    public static Object[][] getSumData() {
        return new Object[][] {
                {"GreyColorOrderTestData.json"},
                {"BlackColorOrderTestData.json"},
                {"BlackAndGreyColorOrderTestData.json"},
                {"NoColorOrderTestData.json"}
        };
    }
    @Before
    public void setUp() {

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

    }

    @Step("Отправить запрос на создание заказа")
    public Response sendMakeOrderRequest(File json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Открыть json с телом запроса")
    public File openJsonRequestBody() {
        return new File("src\\test\\resources\\" + jsonName);
    }

    @Step("Проверить, что код ответа соответствует ожидаемому")
    public void checkStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Проверить наличие поля в body ответа")
    public void checkBodyField(Response response,String field) {
        response.then().body(field, Matchers.notNullValue());
    }
    @Test
    public void makeOrderTest() {
        File json = openJsonRequestBody();

        Response response = sendMakeOrderRequest(json);

        checkStatusCode(response, 201);

        checkBodyField(response,"track");

        order = response.as(OrderId.class);
    }

    @After
    public void CancelOrder() {
        //Всегда возвращает 404
        given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders/cancel");
    }

}
