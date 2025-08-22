package com.librarymgmt.controller;

import com.librarymgmt.model.Staff;
import com.librarymgmt.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<Staff>> getAll() {
        return ResponseEntity.ok(staffService.listAllStaff());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Staff staff) {
        boolean ok = staffService.addStaff(staff);
        if (!ok) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add staff");
        return ResponseEntity.status(HttpStatus.CREATED).body(staff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean ok = staffService.deleteStaffById(id);
        if (!ok) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}

