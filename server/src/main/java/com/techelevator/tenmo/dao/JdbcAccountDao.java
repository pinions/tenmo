package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.controller.AuthenticationController;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserAccount getAccountBalance(int id) {
        String sql = "SELECT username, balance\n" +
                "FROM tenmo_user\n" +
                "JOIN account ON tenmo_user.user_id = account.user_id\n" +
                "WHERE tenmo_user.user_id = ?;";

        
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToUser(results);
        }
        throw new UsernameNotFoundException("Username not found");
    }

    private UserAccount mapRowToUser (SqlRowSet results) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(results.getString("username"));
        userAccount.setBalance(results.getDouble("balance"));
        return userAccount;
    }

}
