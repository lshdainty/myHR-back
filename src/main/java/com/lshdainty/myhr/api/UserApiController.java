package com.lshdainty.myhr.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {
    private final UserService userService;

    @PostMapping("/api/v1/user")
    public ApiResponse join(@RequestBody UserReq req) {
        Long userId = userService.join(
                req.getUserName(),
                req.getUserBirth(),
                req.getUserEmploy(),
                req.getUserWorkTime(),
                req.getLunarYN()
        );

        return ApiResponse.success(new UserResp(userId));
    }

    @GetMapping("/api/v1/user/{id}")
    public ApiResponse user(@PathVariable("id") Long userId) {
        User user = userService.findUser(userId);
        return ApiResponse.success(new UserResp(user));
    }

    @GetMapping("/api/v1/users")
    public ApiResponse users() {
        List<User> users = userService.findUsers();
        List<UserResp> resps = users.stream()
                .map(u -> new UserResp(u))
                .collect(Collectors.toList());

        return ApiResponse.success(resps);
    }

    @PutMapping("/api/v1/user/{id}")
    public ApiResponse editUser(@PathVariable("id") Long userId, @RequestBody UserReq req) {
        userService.editUser(
                userId,
                req.getUserName(),
                req.getUserBirth(),
                req.getUserEmploy(),
                req.getUserWorkTime(),
                req.getLunarYN()
        );

        User findUser = userService.findUser(userId);
        return ApiResponse.success(new UserResp(findUser));
    }

    @DeleteMapping("/api/v1/user/{id}")
    public ApiResponse deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.success();
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class UserReq {
        private String userName;
        private String userBirth;
        private String userWorkTime;
        private String userEmploy;
        private String lunarYN;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class UserResp {
        private Long userNo;
        private String userName;
        private String userBirth;
        private String userWorkTime;
        private String userEmploy;
        private String lunarYN;
        private String delYN;

        public UserResp(Long id) {
            userNo = id;
        }

        public UserResp(User user) {
            userNo = user.getId();
            userName = user.getName();
            userBirth = user.getBirth();
            userWorkTime = user.getWorkTime();
            userEmploy = user.getEmploy();
            lunarYN = user.getLunarYN();
            delYN = user.getDelYN();
        }
    }
}
