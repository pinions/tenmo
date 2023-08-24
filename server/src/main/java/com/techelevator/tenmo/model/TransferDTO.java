package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;

public class TransferDTO {

    @NotEmpty
    private String transferId;

    @NotEmpty
    private String senderUsername;

    @NotEmpty
    private String receiverUsername;

    @NotEmpty Double transferAmount;

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
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

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "transferId='" + transferId + '\'' +
                ", transferAmount='" + transferAmount + '\'' +
                ", from='" + senderUsername + '\'' +
                ", to='" + receiverUsername + '\'' +
                '}';
    }
}
