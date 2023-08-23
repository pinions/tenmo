package com.techelevator.tenmo.controller;

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

    private UserDao userDao;
    private final TokenProvider tokenProvider;

    public AccountController(UserDao userDao, TokenProvider tokenProvider) {
        this.userDao = userDao;
        this.tokenProvider = tokenProvider;
    }

    @RequestMapping(path = "/accountbalance", method = RequestMethod.GET)
    public double getAccountBalance(Principal principal) {

        //let's get some lunch, when we come back we're looking to make a new DAO to access the balance or something like that

        return 1; //return userObject(username, balance);
    }
}
