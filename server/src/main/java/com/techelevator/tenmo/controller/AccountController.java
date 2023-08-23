package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.security.jwt.TokenProvider;

import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private final AccountDao accountDao;
    private AuthenticationController authenticationController;

    private final UserDao userDao;

    public AccountController(UserDao userDao, AccountDao accountDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/accountbalance", method = RequestMethod.GET)
    public UserAccount getUserAccountNameAndBalance(Principal principal) {
        int userid = userDao.findIdByUsername(principal.getName());
        return accountDao.getAccountBalance(userid);
    }
}
