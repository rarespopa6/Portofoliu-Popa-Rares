package org.rares.eventservice.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserDTO {
    private String email;
    private String role;
}
