package org.example.order_list;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderInList {

    private int id;

    private int courierId;

    private String firstName;

    private String lastName;

    private String address;

    private String metroStation;

    private String phone;

    private int rentTime;

    private String deliveryDate;

    private int track;

    private List<String> color;

    private String comment;

    private String createdAt;

    private String updatedAt;

    private int status;


}
