package com.cloudE.ucenter.manager;

import com.cloudE.entity.Memberfides;
import com.cloudE.entity.User;
import com.cloudE.mapper.MemberfidesMapper;
import com.cloudE.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserManager {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MemberfidesMapper memberfidesMapper;

    public User getUserByUserId(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public Memberfides getMeberByMid(Integer mid) {
        return memberfidesMapper.selectByPrimaryKey(mid);
    }
}
