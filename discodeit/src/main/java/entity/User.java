package entity;

import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String displayName;
    private String password;
    private String email;
    private String phoneNumber;


    public User(String displayName, String password, String email, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.password = password;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();;
    }

    public boolean setDisplayName(String displayName) {
        if (displayName == null) {
            return false;
        }
        setUpdatedAt();
        this.displayName = displayName;
        return true;
    }

    public boolean setPassword(String password) {
        if (password == null) {
            return false;
        }
        setUpdatedAt();
        this.password = password;
        return true;
    }

    public boolean setEmail(String email) {
        if (email == null) {
            return false;
        }
        setUpdatedAt();
        this.email = email;
        return true;
    }

    public boolean setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        setUpdatedAt();
        this.phoneNumber = phoneNumber;
        return true;
    }

    @Override
    public String toString() {
        return """
                ... My Information ...
                Name: %s
                Id: %s
                Password: [CENSORED]
                Email: %s
                Phone number: %s
                Created time: %d
                Updated time: %d
                """.formatted(displayName, id, email, phoneNumber, createdAt, updatedAt);
    }
}
