package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class OrderStep {

    Response response;

    final private String makeOrderPath = "/api/v1/orders";
    final private String cancelOrderPath = "/api/v1/orders/cancel";
    OrderTrack orderTrack;

    @Step("Отменить заказ")
    public void cancelOrder() {
        if (orderTrack != null) {
            if (orderTrack.getTrack() != 0) {
                sendCancelOrderRequest();
            }
        }
    }

    @Step("Отправить запрос на создание заказа")
    public void sendMakeOrderRequest(Order order) {
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(makeOrderPath);


        orderTrack = response.as(OrderTrack.class);
    }

    @Step("Проверить, что код ответа соответствует ожидаемому")
    public void checkStatusCode(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Проверить наличие поля в body ответа")
    public void checkBodyFieldExists(String field) {
        response.then().body(field, Matchers.notNullValue());
    }

    public void sendCancelOrderRequest() {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(orderTrack)
                .when()
                .post(cancelOrderPath);
    }
}
