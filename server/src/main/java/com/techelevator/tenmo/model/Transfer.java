package com.techelevator.tenmo.model;

import org.springframework.stereotype.Component;

@Component
public class Transfer {
    private int transferId;
    private String senderUsername;
    private String receiverUsername;
    private double transferAmount;

    public Transfer() {};

    public Transfer(int transferId, String senderUsername, String receiverUsername, Double transferAmount) {
        this.transferId = transferId;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.transferAmount = transferAmount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }
}