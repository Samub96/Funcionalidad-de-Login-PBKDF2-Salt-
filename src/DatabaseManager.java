

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/login_db";
    private static final String USER = "root";
    private static final String PASSWORD = "tu_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ No se encontró el driver de MySQL.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = """
                    CREATE TABLE IF NOT EXISTS users (
                        username VARCHAR(50) PRIMARY KEY,
                        salt VARBINARY(32) NOT NULL,
                        hash VARBINARY(64) NOT NULL,
                        iterations INT NOT NULL,
                        is_admin BOOLEAN NOT NULL DEFAULT 0,
                        last_login DATETIME NULL
                    )
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al inicializar base de datos: " + e.getMessage());
        }
    }
}