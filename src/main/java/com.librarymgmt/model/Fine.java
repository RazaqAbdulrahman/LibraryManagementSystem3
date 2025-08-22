package com.librarymgmt.model;

import java.time.LocalDate;

public class Fine {
    private String fineId;
    private String memberId;
    private double amount;
    private String reason;
    private LocalDate issueDate;
    private boolean paid;

    public Fine(String fineId, String memberId, double amount, String reason, LocalDate issueDate, boolean paid) {
        this.fineId = fineId;
        this.memberId = memberId;
        this.amount = amount;
        this.reason = reason;
        this.issueDate = issueDate;
        this.paid = paid;
    }

    public Fine() {}

    public String getFineId() { return fineId; }
    public void setFineId(String fineId) { this.fineId = fineId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
}
