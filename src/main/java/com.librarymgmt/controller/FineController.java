package com.librarymgmt.controller;

import com.librarymgmt.model.Fine;
import com.librarymgmt.service.FineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping
    public ResponseEntity<List<Fine>> getAll() {
        return ResponseEntity.ok(fineService.listAllFines());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Fine fine) {
        boolean ok = fineService.issueFine(fine);
        if (!ok) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create fine");
        return ResponseEntity.status(HttpStatus.CREATED).body(fine);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> pay(@PathVariable String id) {
        boolean ok = fineService.payFine(id);
        if (!ok) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fine not found or could not be paid");
        return ResponseEntity.ok("Fine marked paid");
    }
}

