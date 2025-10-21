import java.time.LocalDateTime;

public class User {
    private String username;
    private byte[] salt;
    private byte[] hash;
    private int iterations;
    private boolean isAdmin;
    private LocalDateTime lastLogin;

    public User(String username, byte[] salt, byte[] hash, int iterations, boolean isAdmin, LocalDateTime lastLogin) {
        this.username = username;
        this.salt = salt;
        this.hash = hash;
        this.iterations = iterations;
        this.isAdmin = isAdmin;
        this.lastLogin = lastLogin;
    }

    public String getUsername() { return username; }
    public byte[] getSalt() { return salt; }
    public byte[] getHash() { return hash; }
    public int getIterations() { return iterations; }
    public boolean isAdmin() { return isAdmin; }
    public LocalDateTime getLastLogin() { return lastLogin; }
}