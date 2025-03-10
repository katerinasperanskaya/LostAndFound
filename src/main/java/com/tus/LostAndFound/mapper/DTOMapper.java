package com.tus.lostAndFound.mapper;

import com.tus.lostAndFound.dto.*;
import com.tus.lostAndFound.model.*;

public class DTOMapper {

    // Convert User Entity to UserDTO
    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }

    // Convert Admin Entity to AdminDTO
    public static AdminDTO toAdminDTO(Admin admin) {
        if (admin == null) return null;

        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setEmail(admin.getEmail());
        dto.setPhone(admin.getPhone());
        return dto;
    }
}

