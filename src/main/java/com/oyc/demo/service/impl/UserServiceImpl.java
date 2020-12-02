package com.oyc.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyc.demo.domain.User;
import com.oyc.demo.mapper.UserMapper;
import com.oyc.demo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author oyc
 * @Date 2020/12/2 12:24 上午
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
