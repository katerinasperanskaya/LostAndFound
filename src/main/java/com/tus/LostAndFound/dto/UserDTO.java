package com.tus.lostAndFound.dto;

import lombok.Getter;
import lombok.Setter;


//Hides Password

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
}
