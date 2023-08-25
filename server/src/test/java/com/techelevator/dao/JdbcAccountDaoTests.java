package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserAccount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDaoTests extends BaseDaoTests {
    private JdbcAccountDao sut;
    private UserAccount testUser;
    private Transfer testTransfer;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);

        testUser = new UserAccount(1000, "user");
        testTransfer = new Transfer(3002, "nix", "nicholas", 500.00);
    }

    @Test
    public void getAccountBalance_returns_correct_username_and_balance() {
        double retrievedBalance = sut.getAccountBalance(testUser.getUsername()).getBalance();
        double expectedBalance = 1000;
        Assert.assertEquals(retrievedBalance, expectedBalance, 0.001);
    }

    @Test
    public void transferBucks_updates_balance_for_sender_and_receiver() {
        double transferAmount = sut.transferBucks(testTransfer).getTransferAmount();
        String sql = "SELECT balance FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?;";
        Assert.fail();
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