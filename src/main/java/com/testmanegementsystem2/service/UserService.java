package com.testmanegementsystem2.service;

import com.testmanegementsystem2.dao.UserDao;
import com.testmanegementsystem2.model.User;
import com.testmanegementsystem2.util.PasswordEncoder;

import java.sql.SQLException;

public class UserService {
    private final UserDao userDao;
    private final PasswordEncoder encoder = new PasswordEncoder();

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void register(String email, String rawPwd) throws SQLException {
        User u = new User();
        u.setEmail(email);
        u.setPassword(encoder.hash(rawPwd));
        u.setEnabled(true);
        userDao.save(u);
    }
}
