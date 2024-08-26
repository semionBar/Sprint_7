package org.example.order;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;

@RunWith(Parameterized.class)
public class OrderTest {

    OrderStep orderStep;

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;

    private final String phone;

    private final int rentTime;

    private final String deliveryDate;

    private final String comment;

    private final List<String> color;


    public OrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }


    @Parameterized.Parameters // добавили аннотацию
    public static Object[][] getSumData() {
        return new Object[][] {
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("GREY")},
                {"asdf", "f", "fhjfghjfghj, 142 apt.", 3, "+7 800 355 35 35", 5, "2020-06-06", "a", List.of("GREY", "BLACK")},
                {"gfds", "fg", "Krtyur", 2, "+7 800 355 35 35", 5, "2020-06-06", "", List.of("BLACK")},
                {"hgfdh", "sdfg sdfg", "wuyruyruty", 1, "8 800 355 35 35", 5, "2020-06-06", "fff", null}
        };
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        orderStep = new OrderStep();
    }
    @Test
    public void makeOrderTest() {

        Order order = new Order(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color
        );

        orderStep.sendMakeOrderRequest(order);

        orderStep.checkStatusCode(SC_CREATED);

        orderStep.checkBodyFieldExists("track");

    }

    @After
    public void CancelOrder() {
        //Всегда возвращает 404
        orderStep.cancelOrder();
    }

}
