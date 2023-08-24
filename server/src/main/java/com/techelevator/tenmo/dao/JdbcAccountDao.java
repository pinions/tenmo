package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
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

//    @Override
//    public boolean isApproved(int senderId, int receiverId, Double transferAmount) {
//        boolean approved = true;
//        double senderBalance = getAccountBalance(senderId).getBalance();
//        if (senderBalance > 0 && transferAmount <= senderBalance && senderId != receiverId) {
//            transferBucks(senderId, receiverId, transferAmount);
//        } else {
//            approved = false;
//            System.out.println("Please choose a valid transfer amount." );
//        }
//        return approved;
//    }
//
//    @Override
//    public Transfer transferBucks(int senderId, int receiverId, Double transferAmount) {
//        SqlRowSet transfer = null;
//        double senderBalance = getAccountBalance(senderId).getBalance();
//        if (senderBalance > 0 && transferAmount <= senderBalance && senderId != receiverId) {
//            String sql = "UPDATE account SET balance = ? WHERE user_id = ?;";
//            jdbcTemplate.queryForRowSet(sql, senderBalance - transferAmount, senderId);
//            jdbcTemplate.queryForRowSet(sql, getAccountBalance(receiverId).getBalance() + transferAmount);
//
//            String sqlTransfer = "INSERT INTO transfers (sender_id, receiver_id, amount)" +
//                    "VALUES (?, ?, ?, ?)";
//            transfer = jdbcTemplate.queryForRowSet(sqlTransfer, Integer.class, senderId, receiverId, transferAmount);
//            System.out.println("You transferred $" + transferAmount + " to " + userDao.findUsernameById(receiverId) + ".");
//        }
//        if (transfer.next()) {
//            return mapRowToTransfer(transfer);
//        }
//        throw new UsernameNotFoundException("Username not found");
//    }

    @Override
    public Transfer transferBucks(double transferAmount, String senderUsername, String receiverUsername) {

        SqlRowSet transfer = null;
        double senderBalance = getAccountBalance(senderUsername).getBalance();
        double receiverBalance = getAccountBalance(receiverUsername).getBalance();
        int senderId = userDao.findIdByUsername(senderUsername);
        int receiverId = userDao.findIdByUsername(receiverUsername);

        if (senderBalance > 0 && transferAmount <= senderBalance && !senderUsername.equals(receiverUsername)) {
            String sql = "UPDATE account SET balance = ? WHERE user_id = ?;";
            jdbcTemplate.queryForRowSet(sql, senderBalance - transferAmount, senderId);
            jdbcTemplate.queryForRowSet(sql, receiverBalance + transferAmount, receiverId);

            String sqlTransfer = "INSERT INTO transfers (transfer_amount, sender_username, receiver_username)" +
                    "VALUES (?, ?, ?)";
            transfer = jdbcTemplate.queryForRowSet(sqlTransfer, Integer.class, transferAmount, senderUsername, receiverUsername);
            System.out.println("You transferred $" + transferAmount + " to " + receiverUsername + ".");
        }

        assert transfer != null;
        if (transfer.next()) {
            return mapRowToTransfer(transfer);
        }

        throw new UsernameNotFoundException("Username not found");

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

