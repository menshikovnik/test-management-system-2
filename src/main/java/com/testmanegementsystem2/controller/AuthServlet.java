package com.testmanegementsystem2.controller;

import com.testmanegementsystem2.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.testmanegementsystem2.util.JsonUtil;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext()
                .getAttribute("userService");
    }

    // POST /api/auth/register
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String path = req.getPathInfo();
        if ("/register".equals(path)) {
            Map<String, String> body = JsonUtil.read(req);
            try {
                userService.register(body.get("email"), body.get("password"));
                JsonUtil.write(resp, Map.of("status", "OK"));
            } catch (SQLException ex) {
                resp.sendError(500, ex.getMessage());
            }
        }
    }
}
