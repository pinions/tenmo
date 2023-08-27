package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.UserDao;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
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

        if (transfer.getTransferAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot send 0 or a negative amount");
        }

        return accountDao.transferBucks(transfer);
    }

    @RequestMapping(path = "/mytransfers", method = RequestMethod.GET)
    public List<Transfer> getListOfTransfers(Principal principal) {
        int senderId = userDao.findIdByUsername(principal.getName());
        return accountDao.findTransfers(senderId);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable("id") int transferId) {
        return accountDao.findTransferById(transferId);
    }

}
