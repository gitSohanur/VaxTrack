package com.vaxtrack.model;

import com.vaxtrack.utils.Role;

/**
 * Represents an admin/viewer who can log in.
 * Demonstrates: Encapsulation, Inheritance
 */
public class User extends BaseEntity {

    private String username;
    private String password;
    private Role   role;

    // --- Constructors ---

    public User() {
        super();
    }

    public User(int id, String username, String password, Role role) {
        super(id);
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    // --- Getters & Setters ---

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // --- Displayable implementation ---

    @Override
    public String getDisplayInfo() {
        return "User[" + getId() + "] " + username + " (" + role + ")";
    }
}
