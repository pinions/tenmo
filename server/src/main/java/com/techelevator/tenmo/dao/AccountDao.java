package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AccountDao {

    UserAccount getAccountBalance(String username);

    Transfer transferBucks(Transfer transfer);

    List<Transfer> findTransfers(int senderId);

    Transfer findTransferById(int transferId);

    Transfer updateTransfer(Transfer transfer);

}


