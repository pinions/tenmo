package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface UserDao {

    List<User> findAll();

    List<String> findOtherUsernames(int id);

    User findByUsername(String username);

    String findUsernameById(int id);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
