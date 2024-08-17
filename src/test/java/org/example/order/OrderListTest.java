package org.example.order;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

public class OrderListTest {

    OrderListStep orderListStep;
    @Before
    public void setUp() {

        orderListStep = new OrderListStep();

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void getOrderListTest() {

        orderListStep.sendGetOrderListRequest();

        orderListStep.orderListDeserialization();

        orderListStep.listContainsAtListOneItem();

    }
}
