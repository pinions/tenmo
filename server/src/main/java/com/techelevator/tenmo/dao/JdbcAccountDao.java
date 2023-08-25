package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDao userDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public UserAccount getAccountBalance(String username) {
        String sql = "SELECT username, balance\n" +
                "FROM tenmo_user\n" +
                "JOIN account ON tenmo_user.user_id = account.user_id\n" +
                "WHERE tenmo_user.username = ?;";


        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        if (results.next()) {
            return mapRowToUser(results);
        }
        throw new UsernameNotFoundException("Username not found");
    }



    @Override
    public Transfer transferBucks(Transfer transfer) {
        double senderBalance = getAccountBalance(transfer.getSenderUsername()).getBalance();
        double receiverBalance = getAccountBalance(transfer.getReceiverUsername()).getBalance();
        int senderId = userDao.findIdByUsername(transfer.getSenderUsername());
        int receiverId = userDao.findIdByUsername(transfer.getReceiverUsername());

        if (senderBalance > 0 && transfer.getTransferAmount() <= senderBalance && !transfer.getSenderUsername().equals(transfer.getReceiverUsername())) {
            String sql = "UPDATE account SET balance = ? WHERE user_id = ?;";
            jdbcTemplate.update(sql, senderBalance - transfer.getTransferAmount(), senderId);
            jdbcTemplate.update(sql, receiverBalance + transfer.getTransferAmount(), receiverId);

            String sqlTransfer = "INSERT INTO transfer (transfer_amount, sender_username, receiver_username)" +
                    "VALUES (?, ?, ?) RETURNING transfer_id;";
            Integer transferId = jdbcTemplate.queryForObject(sqlTransfer, Integer.class, transfer.getTransferAmount(),
                    transfer.getSenderUsername(), transfer.getReceiverUsername());
            transfer.setTransferId(transferId);
            System.out.println("You transferred $" + transfer.getTransferAmount() + " to " + transfer.getReceiverUsername() + ".");
        }
        return transfer;
    }



    @Override
    public List<Transfer> findTransfers(int senderId) {
        List<Transfer> transfers = new ArrayList<>();
        String senderUsername = userDao.findUsernameById(senderId);
        String sql = "SELECT transfer_id, transfer_amount, sender_username, receiver_username " +
                "FROM transfer WHERE sender_username ILIKE ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, senderUsername);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        }
        return transfers;
    }



    public Transfer findTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_amount, sender_username, receiver_username " +
                "FROM transfer WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            while (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        }
        return transfer;
    }



    private UserAccount mapRowToUser (SqlRowSet results) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(results.getString("username"));
        userAccount.setBalance(results.getDouble("balance"));
        return userAccount;
    }



    private Transfer mapRowToTransfer (SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferAmount(results.getDouble("transfer_amount"));
        transfer.setSenderUsername(results.getString("sender_username"));
        transfer.setReceiverUsername(results.getString("receiver_username"));
        return transfer;
    }

}

