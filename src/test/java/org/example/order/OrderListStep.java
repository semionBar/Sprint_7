package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.order_list.OrderList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;


public class OrderListStep {

    OrderList orderList;
    Response response;
    final private String getOrderListPath = "/api/v1/orders";
    @Step("Отправить запрос на получение списка заказов")
    public void sendGetOrderListRequest() {
        response = given()
                .header("Content-type", "application/json")
                .when()
                .get(getOrderListPath);
    }
    @Step("Десериализация ответа")
    public void orderListDeserialization() {
        orderList = response.body().as(OrderList.class);
    }

    @Step("Проверить, что списке заказов есть хотя бы один заказ")
    public void listContainsAtListOneItem() {
        MatcherAssert.assertThat(orderList.getOrders().size(), Matchers.greaterThan(0));
    }
}
