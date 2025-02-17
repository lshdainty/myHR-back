package com.lshdainty.myhr.api;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @GetMapping("/api/v1/user/{id}")
    public ApiResponse user(@PathVariable("id") int userId) {
        User user = userService.findUserByUserId(userId);

        return ApiResponse.success(user);
    }

//    @GetMapping("/api/v1/users")

}
