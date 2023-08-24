package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.security.jwt.TokenProvider;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private final AccountDao accountDao;
    private JdbcUserDao jdbcUserDao;
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

    @RequestMapping(path = "/userlist", method = RequestMethod.GET)
    public List<String> getListOfUsers(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        return userDao.findOtherUsernames(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer transfer(Principal principal, String username, double transferAmount) {
        int senderId = userDao.findIdByUsername(principal.getName());
        int receiverId = userDao.findIdByUsername(username);
        return accountDao.transferBucks(senderId, receiverId, transferAmount);
    }
}

