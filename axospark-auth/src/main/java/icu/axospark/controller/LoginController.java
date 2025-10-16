package icu.axospark.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import entity.AxoSparkResult;
import icu.axospark.pojo.dto.UsersDTO;
import icu.axospark.pojo.entity.Users;
import icu.axospark.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录测试
 */
@RestController
@Tag(name = "登录请求")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping ("login")
    @Operation(description = "登录")
    public AxoSparkResult login(@RequestBody UsersDTO usersDTO){
        loginService.login(usersDTO);
        return AxoSparkResult.success().code(200);
    }

    // 测试注销  ---- http://localhost:8081/acc/logout
    @GetMapping("logout")
    @Operation(description = "退出登录")
    public AxoSparkResult logout() {
        StpUtil.logout();
        return  AxoSparkResult.success().code(200);
    }

}
