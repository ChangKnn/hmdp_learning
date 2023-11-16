package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.hmdp.utils.SystemConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result sendCode(String phoneNum, HttpSession session) {
        // If phone number is valid?
        if (RegexUtils.isPhoneInvalid(phoneNum)) {
            return Result.fail("Phone number is wrong.");
        }
        String code = RandomUtil.randomNumbers(6);
        session.setAttribute(SESSION_CODE_NAME, code);
        log.debug("Code has been send: {}", code);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("Phone number format is wrong.");
        }
        Object codeSession = session.getAttribute(SESSION_CODE_NAME);
        String codeGet = loginForm.getCode();
        if (codeSession == null || !(codeSession.toString().equals(codeGet))) {
            return Result.fail("Code is wrong.");
        }
        User user = query().eq(SQL_USER_PHONE_NAME, phone).one();
        if (user == null) {
            creatUser(phone);
        }
        session.setAttribute(SESSION_USER_NAME, user);
        return Result.ok();
    }

    private User creatUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        save(user);
        return user;
    }

}
