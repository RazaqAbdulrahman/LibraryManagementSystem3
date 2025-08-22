package com.librarymgmt.model;

public class Staff {
    private String staffId;
    private String name;
    private String role;
    private String email;
    private String phone;

    public Staff(String staffId, String name, String role, String email, String phone) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

