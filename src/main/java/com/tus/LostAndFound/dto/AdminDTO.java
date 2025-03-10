package com.tus.lostAndFound.dto;

import lombok.Getter;
import lombok.Setter;

//Prevents Unauthorized Role Modifications

@Getter
@Setter
public class AdminDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
}
