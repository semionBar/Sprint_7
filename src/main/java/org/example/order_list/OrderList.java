package org.example.order_list;

import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderList {
    private List<OrderInList> orders;


}
