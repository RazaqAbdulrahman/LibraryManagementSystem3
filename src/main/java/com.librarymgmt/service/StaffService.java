package com.librarymgmt.service;

import com.librarymgmt.model.Staff;
import java.util.ArrayList;
import java.util.List;

public class StaffService {
    private List<Staff> staffList = new ArrayList<>();

    public boolean addStaff(Staff staff) {
        staffList.add(staff);
        System.out.println("Staff added successfully.");
        return false;
    }

    public void viewStaff() {
        if (staffList.isEmpty()) {
            System.out.println("No staff found.");
            return;
        }
        for (Staff staff : staffList) {
            System.out.println(staff.getName() + " - " + staff.getRole());
        }
    }

    public List<Staff> listAllStaff() {
        return List.of();
    }

    public boolean deleteStaffById(String id) {
        return false;
    }
}


