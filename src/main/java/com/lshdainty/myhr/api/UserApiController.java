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
        User user = new User();
        user.setName(req.getUserName());
        user.setBirth(req.getUserBirth());
        user.setEmploy(req.getUserEmploy());
        user.setWorkTime(req.getUserWorkTime());
        user.setLunarYN(req.getLunarYN());
        user.setDelYN("N");

        Long userId = userService.join(user);

        UserResp resp = new UserResp();
        resp.setUserNo(userId);

        return ApiResponse.success(resp);
    }

    @GetMapping("/api/v1/user/{id}")
    public ApiResponse user(@PathVariable("id") int userId) {
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
    public ApiResponse editUser(@PathVariable("id") int userId, @RequestBody UserReq req) {
        User user = new User();
        user.setName(req.getUserName());
        user.setBirth(req.getUserBirth());
        user.setEmploy(req.getUserEmploy());
        user.setWorkTime(req.getUserWorkTime());
        user.setLunarYN(req.getLunarYN());
        userService.editUser(userId, user);

        User findUser = userService.findUser(userId);
        return ApiResponse.success(new UserResp(findUser));
    }

    @DeleteMapping("/api/v1/user/{id}")
    public ApiResponse deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId, "Y");
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
        private String delYN;
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

        public UserResp() {}

        public UserResp(User user) {
            this.userNo = user.getId();
            this.userName = user.getName();
            this.userBirth = user.getBirth();
            this.userWorkTime = user.getWorkTime();
            this.userEmploy = user.getEmploy();
            this.lunarYN = user.getLunarYN();
            this.delYN = user.getDelYN();
        }
    }
}
