package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.security.jwt.TokenProvider;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

//@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
    @Autowired
    private final AccountDao accountDao;
    @Autowired
    private JdbcUserDao jdbcUserDao;
    @Autowired
    private final UserDao userDao;

    public AccountController(UserDao userDao, AccountDao accountDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/accountbalance", method = RequestMethod.GET)
    public UserAccount getUserAccountNameAndBalance(Principal principal) {
        String username = principal.getName();
        return accountDao.getAccountBalance(username);
    }

    @RequestMapping(path = "/userlist", method = RequestMethod.GET)
    public List<String> getListOfUsers(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        return userDao.findOtherUsernames(userId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer transfer(@Valid @RequestBody Transfer transfer, Principal principal) {

        return accountDao.transferBucks(transfer);
    }
}

