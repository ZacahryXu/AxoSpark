package icu.axospark.pojo.dto;

import lombok.Data;

@Data
public class UsersDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码哈希
     */
    private String password;
}
