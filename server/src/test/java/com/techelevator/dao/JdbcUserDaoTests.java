package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcUserDaoTests extends BaseDaoTests{
    private static final User TEST_USER_1 = new User(1001, "bob", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "ROLE_USER");
    private static final User TEST_USER_2 = new User(1002, "user", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy", "ROLE_USER");
    private static final User TEST_USER_3 = new User(1003, "nicholas", "$2a$10$XYwSwRxxkUxGcsKyozcwEur5UXJ2nyXLki.BORll9jsGxZEcy3CXW", "ROLE_USER");
    private static final User TEST_USER_4 = new User(1004, "nix", "$2a$10$7CcedCBqk9hvQAHO5CnmI.UkolotAD1yCXe.3PjFn/zGHUkLsLTre", "ROLE_USER");
    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("bob","password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("bob");
        Assert.assertEquals("bob", user.getUsername());
    }

}

