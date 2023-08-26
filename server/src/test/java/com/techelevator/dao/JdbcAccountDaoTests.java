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

import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests {
    private JdbcAccountDao sut;
    private UserAccount testUser1;
    private UserAccount testUser2;
    private Transfer testTransfer;
    private Transfer failedTransfer;
    private Transfer testTransfer2;
    private Transfer listTransfer;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);

        testUser1 = new UserAccount(1002, "nicholas");
        testUser2 = new UserAccount(1003, "nix");
        testTransfer = new Transfer(3002, "nix", "nicholas", 500.00);
        testTransfer2 = new Transfer(3012,"nicholas","nix",200.00);
        failedTransfer = new Transfer(3011,"caroline","nicholas",20.00);
        listTransfer = new Transfer(3008,"nicholas","nix",9.00);
    }

    @Test
    public void getAccountBalance_returns_correct_username_and_balance() {
        double retrievedBalance = sut.getAccountBalance(testUser1.getUsername()).getBalance();
        double expectedBalance = 2000;
        Assert.assertEquals(retrievedBalance, expectedBalance, 0.001);
    }

    @Test
    public void transferBucks_updates_balance_for_sender_and_receiver() {
        Transfer newTransfer = sut.transferBucks(testTransfer);
        double transferAmount = newTransfer.getTransferAmount();
        double expectedTransferAmount = 500;
        double senderBalance = 0;
        double receiverBalance = 0;
        double expectedSenderBalance = 100;
        double expectedReceiverBalance = 2500;
        String senderUsername = newTransfer.getSenderUsername();
        String receiverUsername = newTransfer.getReceiverUsername();
        String sql = "SELECT balance FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username ILIKE ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, senderUsername);
            while (results.next()) {
                senderBalance = results.getDouble("balance");
            }
            results = jdbcTemplate.queryForRowSet(sql, receiverUsername);
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
        Transfer newTransfer = sut.transferBucks(failedTransfer);
        Assert.assertNull(newTransfer);
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
    public void updateTransfer_creates_new_entry_in_transfer_table() {
        Transfer createdTransfer = sut.updateTransfer(testTransfer2);
        int newId = createdTransfer.getTransferId();
        Assert.assertTrue(newId > 0);

        Transfer retrievedTransfer = sut.findTransferById(newId);
        assertTransfersMatch(createdTransfer, retrievedTransfer);
    }

    @Test
    public void findTransfers_returns_correct_list_of_transfers_for_user() {
        List<Transfer> transfers = sut.findTransfers(1001);
        Assert.assertEquals(1, transfers.size());
        assertTransfersMatch(listTransfer, transfers.get(0));
    }

    @Test
    public void findTransferById_returns_correct_transfer() {
        Transfer transfer = sut.findTransferById(3002);
        assertTransfersMatch(testTransfer, transfer);
    }

    @Test
    public void findTransferById_returns_null_when_id_not_found() {
        Transfer transfer = sut.findTransferById(4000);
        Assert.assertNull(transfer);
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
        Assert.assertEquals(expected.getSenderUsername(), actual.getSenderUsername());
        Assert.assertEquals(expected.getReceiverUsername(), actual.getReceiverUsername());
    }

}