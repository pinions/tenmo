package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcUserDaoTests extends BaseDaoTests {
    private static final User TEST_USER_1 = new User(1001, "user", "password", "ROLE_USER");
    private static final User TEST_USER_2 = new User(1002, "nicholas", "password", "ROLE_USER");
    private static final User TEST_USER_3 = new User(1003, "nix", "password", "ROLE_USER");
    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("bob", "password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("bob");
        Assert.assertEquals("bob", user.getUsername());
    }

    @Test
    public void findIdByUsername_finds_correct_id_when_username_is_entered() {
        Assert.assertEquals(1002, sut.findIdByUsername("nicholas"));
        Assert.assertEquals(1001, sut.findIdByUsername("user"));
    }

    @Test
    public void findUsernameById_returns_correct_username_when_id_is_entered() {
        Assert.assertEquals("nicholas", sut.findUsernameById(1002));
        Assert.assertEquals("user", sut.findUsernameById(1001));
    }

//    @Test
//    public void findAll_finds_all_users() {
//        List<User> users = sut.findAll();
//
//        Assert.assertEquals(9, users.size());
//
//        assertUsersMatch(TEST_USER_2);
//    }

    private void assertUsersMatch(User expected, User actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getAuthorities(), actual.getAuthorities());
    }
}



