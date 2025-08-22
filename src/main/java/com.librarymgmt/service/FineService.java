package com.librarymgmt.service;

import com.librarymgmt.model.Fine;
import java.util.ArrayList;
import java.util.List;

public class FineService {
    private List<Fine> fines = new ArrayList<>();

    public boolean issueFine(Fine fine) {
        fines.add(fine);
        System.out.println("Fine issued successfully.");
        return false;
    }

    public void viewFines() {
        if (fines.isEmpty()) {
            System.out.println("No fines recorded.");
            return;
        }
        for (Fine fine : fines) {
            System.out.println("Fine ID: " + fine.getFineId() + " | Member: " + fine.getMemberId() + " | Amount: " + fine.getAmount());
        }
    }

    public boolean payFine(String id) {
        return false;
    }

    public List<Fine> listAllFines() {
        return List.of();
    }
}
