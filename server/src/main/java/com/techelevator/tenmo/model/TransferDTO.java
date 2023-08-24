package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;

public class TransferDTO {

    @NotEmpty
    private String username;

    @NotEmpty Double transferAmount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }
}
