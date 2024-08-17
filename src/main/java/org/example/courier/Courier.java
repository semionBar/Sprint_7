package org.example.courier;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Courier {
    private String login;
    private String password;
    private String firstName;

}
