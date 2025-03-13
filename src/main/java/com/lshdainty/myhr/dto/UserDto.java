package com.lshdainty.myhr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lshdainty.myhr.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long userNo;
    private String userName;
    private String userBirth;
    private String userWorkTime;
    private String userEmploy;
    private String lunarYN;
    private String delYN;

    public UserDto(Long no) {
        this.userNo = no;
    }

    public UserDto(User user) {
        this.userNo = user.getId();
        this.userName = user.getName();
        this.userBirth = user.getBirth();
        this.userWorkTime = user.getWorkTime();
        this.userEmploy = user.getEmploy();
        this.lunarYN = user.getLunarYN();
        this.delYN = user.getDelYN();
    }
}
