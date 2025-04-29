package com.testmanegementsystem2.dao;

import com.testmanegementsystem2.model.User;
import com.testmanegementsystem2.util.DbUtil;

import javax.naming.NamingException;
import java.sql.*;

import static com.testmanegementsystem2.util.DbUtil.getConnection;

public class UserDao {
    public User findByEmail(String email) throws SQLException, NamingException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            u.setEnabled(rs.getBoolean("enabled"));
            return u;
        }
    }
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users(email, password, enabled) VALUES (?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.isEnabled());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getLong(1));
            }
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(User u) { }
}
