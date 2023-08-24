package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.controller.AuthenticationController;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    private JdbcUserDao jdbcUserDao;

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

    public boolean transferBucks(int senderId, int receiverId, double transferAmount) {
        boolean approved = true;
        double senderBalance = getAccountBalance(senderId).getBalance();
        if (senderBalance > 0 && transferAmount <= senderBalance && senderId != receiverId) {
            String sql = "UPDATE account SET balance = ? WHERE user_id = ?;";
            jdbcTemplate.queryForRowSet(sql, senderBalance - transferAmount, senderId);
            jdbcTemplate.queryForRowSet(sql, getAccountBalance(receiverId).getBalance() + transferAmount);

            String sqlTransfer = "INSERT INTO transfers (sender_id, receiver_id, amount, timestamp)" +
                    "VALUES (?, ?, ?, ?)";
            jdbcTemplate.queryForRowSet(sqlTransfer, senderId, receiverId, transferAmount, LocalDate.now());
            System.out.println("You transferred $" + transferAmount + " to " + jdbcUserDao.findUsernameById(receiverId) + ".");
        } else {
            approved = false;
            System.out.println("Please choose a valid transfer amount." );
        }
        return approved;
    }

    private UserAccount mapRowToUser (SqlRowSet results) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(results.getString("username"));
        userAccount.setBalance(results.getDouble("balance"));
        return userAccount;
    }

}
