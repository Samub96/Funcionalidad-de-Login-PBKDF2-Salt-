package com.security.login;

import java.sql.*;
import java.time.LocalDateTime;

public class UserService {

    public boolean adminExists() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE is_admin = 1")) {
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(String username, String password, boolean isAdmin) {
        byte[] salt = PasswordUtils.generateSalt();
        byte[] hash = PasswordUtils.hashPassword(password, salt, PasswordUtils.getIterations());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users(username, salt, hash, iterations, is_admin) VALUES (?,?,?,?,?)")) {
            stmt.setString(1, username);
            stmt.setBytes(2, salt);
            stmt.setBytes(3, hash);
            stmt.setInt(4, PasswordUtils.getIterations());
            stmt.setBoolean(5, isAdmin);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creando usuario: " + e.getMessage());
        }
    }

    public User authenticate(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return null;

            byte[] salt = rs.getBytes("salt");
            byte[] hash = rs.getBytes("hash");
            int iter = rs.getInt("iterations");
            boolean isAdmin = rs.getBoolean("is_admin");
            LocalDateTime lastLogin = rs.getTimestamp("last_login") != null
                    ? rs.getTimestamp("last_login").toLocalDateTime() : null;

            if (PasswordUtils.verifyPassword(password, salt, hash, iter)) {
                updateLastLogin(username);
                return new User(username, salt, hash, iter, isAdmin, lastLogin);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLastLogin(String username) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET last_login = NOW() WHERE username = ?")) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void listUsers() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT username, is_admin, last_login FROM users");
            while (rs.next()) {
                System.out.printf("- %s | %s | Último login: %s%n",
                        rs.getString("username"),
                        rs.getBoolean("is_admin") ? "ADMIN" : "USER",
                        rs.getTimestamp("last_login"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String username) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetPassword(String username) {
        byte[] salt = PasswordUtils.generateSalt();
        byte[] hash = PasswordUtils.hashPassword("", salt, PasswordUtils.getIterations());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET salt=?, hash=?, iterations=? WHERE username=?")) {
            stmt.setBytes(1, salt);
            stmt.setBytes(2, hash);
            stmt.setInt(3, PasswordUtils.getIterations());
            stmt.setString(4, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword(String username, String newPassword) {
        byte[] salt = PasswordUtils.generateSalt();
        byte[] hash = PasswordUtils.hashPassword(newPassword, salt, PasswordUtils.getIterations());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET salt=?, hash=?, iterations=? WHERE username=?")) {
            stmt.setBytes(1, salt);
            stmt.setBytes(2, hash);
            stmt.setInt(3, PasswordUtils.getIterations());
            stmt.setString(4, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
