package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserAccount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcAccountDaoTests extends BaseDaoTests {
    private JdbcAccountDao sut;
    private UserAccount testUser1;
    private UserAccount testUser2;
    private Transfer testTransfer;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);

        testUser1 = new UserAccount(1002, "nicholas");
        testUser2 = new UserAccount(1003, "nix");
        testTransfer = new Transfer(3002, "nix", "nicholas", 500.00);
    }

    @Test
    public void getAccountBalance_returns_correct_username_and_balance() {
        double retrievedBalance = sut.getAccountBalance(testUser1.getUsername()).getBalance();
        double expectedBalance = 2000;
        Assert.assertEquals(retrievedBalance, expectedBalance, 0.001);
    }

    @Test
    public void transferBucks_updates_balance_for_sender_and_receiver() {
        double transferAmount = sut.transferBucks(testTransfer).getTransferAmount();
        double expectedTransferAmount = 500;
        double senderBalance = 0;
        double receiverBalance = 0;
        double expectedSenderBalance = 100;
        double expectedReceiverBalance = 2500;
        String sql = "SELECT balance FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username ILIKE ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, testTransfer.getSenderUsername());
            while (results.next()) {
                senderBalance = results.getDouble("balance");
            }
            results = jdbcTemplate.queryForRowSet(sql, testTransfer.getReceiverUsername());
            while (results.next()) {
                receiverBalance = results.getDouble("balance");
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data integrity violation");
        }
        Assert.assertEquals(transferAmount, expectedTransferAmount, 0.001);
        Assert.assertEquals(senderBalance, expectedSenderBalance, 0.001);
        Assert.assertEquals(receiverBalance, expectedReceiverBalance, 0.001);
    }

    @Test
    public void transferBucks_does_not_update_balance_if_sent_to_same_user() {
        Assert.fail();
    }

    @Test
    public void transferBucks_does_not_update_balance_if_transfer_amount_is_less_than_zero() {
        Assert.fail();
    }

    @Test
    public void transferBucks_does_not_update_balance_if_sender_balance_is_too_low() {
        Assert.fail();
    }

    @Test
    public void updateTransfer_updates_the_transfer_table() {
        Assert.fail();
    }

    @Test
    public void findTransfers_returns_correct_list_of_transfers_for_user() {
        Assert.fail();
    }

    @Test
    public void findTransferById_returns_correct_transfer() {
        Assert.fail();
    }

}