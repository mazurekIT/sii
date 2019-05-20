package pl.mazurekIT.sii.service;

import pl.mazurekIT.sii.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User getUserByName(String name);

    List<User> getAllUsers();

    List<String> getAllUsersNames();

}
