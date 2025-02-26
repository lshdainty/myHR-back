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
        String name = req.getUserName();
        String birth = req.getUserBirth();
        String employ = req.getUserEmploy();
        String workTime = req.getUserWorkTime();
        String lunar = req.getLunarYN();

        Long userId = userService.join(name, birth, employ, workTime, lunar);

        UserResp resp = new UserResp();
        resp.setUserNo(userId);

        return ApiResponse.success(resp);
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
        String name = req.getUserName();
        String birth = req.getUserBirth();
        String employ = req.getUserEmploy();
        String workTime = req.getUserWorkTime();
        String lunar = req.getLunarYN();
        userService.editUser(userId, name, birth, employ, workTime, lunar);

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
