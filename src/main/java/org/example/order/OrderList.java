package org.example.order;

import java.util.List;

public class OrderList {
    private List<Order> orders;


    public OrderList(List<Order> orderList) {
        this.orders = orderList;

    }

    public OrderList() {
    }


    public List<Order> getOrderList() {
        return orders;
    }

}
