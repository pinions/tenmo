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
    private UserDao userDao;

    // this constructor is here in the event that we only needed a jdbcTemplate somewhere else
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // this constructor has to take the userDao in order for the integration tests to work
    public JdbcAccountDao(UserDao userDao, JdbcTemplate jdbcTemplate) {
        this.userDao = userDao;
        this.jdbcTemplate = jdbcTemplate;
    }




    // getAccountBalance uses principal in the endpoint to find the signed-in user's balance
    // the username parameter is the principal
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


    // transferBucks uses the jdbcTemplate to update the balances of the sender and receiver
    // the usernames are found by the userDao
    @Override
    public Transfer transferBucks(Transfer transfer) {
        double senderBalance = getAccountBalance(transfer.getSenderUsername()).getBalance();
        double receiverBalance = getAccountBalance(transfer.getReceiverUsername()).getBalance();
        int senderId = userDao.findIdByUsername(transfer.getSenderUsername());
        int receiverId = userDao.findIdByUsername(transfer.getReceiverUsername());

        if (transfer.getTransferAmount() > 0 && transfer.getTransferAmount() <= senderBalance
                && !transfer.getSenderUsername().equals(transfer.getReceiverUsername())) {
            String sql = "UPDATE account SET balance = ? WHERE user_id = ?;";
            jdbcTemplate.update(sql, senderBalance - transfer.getTransferAmount(), senderId);
            jdbcTemplate.update(sql, receiverBalance + transfer.getTransferAmount(), receiverId);

            updateTransfer(transfer);
            System.out.println("You transferred $" + transfer.getTransferAmount() + " to " + transfer.getReceiverUsername() + ".");
        }
        return transfer;
    }


    // updateTransfer adds the transfer records into its own table in the database
    @Override
    public Transfer updateTransfer(Transfer transfer) {
        String sqlTransfer = "INSERT INTO transfer (transfer_amount, sender_username, receiver_username)" +
                "VALUES (?, ?, ?) RETURNING transfer_id;";
        Integer transferId = jdbcTemplate.queryForObject(sqlTransfer, Integer.class, transfer.getTransferAmount(),
                transfer.getSenderUsername(), transfer.getReceiverUsername());
        transfer.setTransferId(transferId);
        return transfer;
    }


    // findTransfers uses Principal in the endpoint to only lookup transfer of the signed-in user
    // .findUsernameById comes from the userDao, this could probably be reworked to take the username
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


    //findTransferById also uses principal in the endpoint, but takes an additional parameter to find a specific transfer
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






    // methods to map properties to JSON objects

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

