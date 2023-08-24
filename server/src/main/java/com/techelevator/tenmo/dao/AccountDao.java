package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
@Component
public interface AccountDao {

    UserAccount getAccountBalance(String username);
    Transfer transferBucks(Transfer transfer);

}

