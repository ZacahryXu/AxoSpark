package icu.axospark.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import icu.axospark.exception.AccountNotFoundException;
import icu.axospark.mapper.LoginMapper;
import icu.axospark.pojo.dto.UsersDTO;
import icu.axospark.pojo.entity.Users;
import icu.axospark.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public void login(UsersDTO usersDTO) {
        String username = usersDTO.getUsername();
        Users user = loginMapper.selectByUsername(username);
        if(user==null){
            log.debug("该用户不存在");
            throw new AccountNotFoundException("该用户不存在");
        }
        String password1 = user.getPasswordHash();

        String password2 = usersDTO.getPassword();
        Long id = user.getId();
        if(password1.equals(password2)){
            StpUtil.login(id);
        }
    }
}
