package org.rares.eventservice.feign;

import org.rares.eventservice.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/{email}")
    UserDTO getUserByUsername(@RequestHeader("Authorization") String token, @PathVariable("email") String email);
}
