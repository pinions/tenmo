package com.techelevator.tenmo.model;

import org.springframework.stereotype.Component;

@Component
public class Transfer {
    private int senderId;
    private int receiverId;
    private Double transferAmount;
    private int transferId;

    public Transfer() {}
    public Transfer(int senderId, int receiverId, Double transferAmount, int transferId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transferAmount = transferAmount;
        this.transferId = transferId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }
}