package org.example.order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OrderListTest {

    OrderList orderList;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Отправить запрос на получение списка заказов")
    public Response sendGetOrderListRequest() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
    }

    @Step("Десериализация ответа")
    public OrderList orderListDeserialization(Response response) {
        return response.body().as(OrderList.class);
    }

    @Step("Проверить, что списке заказов есть хотя бы один заказ")
    public void listContainsAtListOneItem() {
        MatcherAssert.assertThat(orderList.getOrderList().size(), Matchers.greaterThan(0));
    }
    @Test
    public void getOrderListTest() {
        Response response = sendGetOrderListRequest();

        orderList = orderListDeserialization(response);

        listContainsAtListOneItem();
    }
}
