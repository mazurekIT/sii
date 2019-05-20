package pl.mazurekIT.sii.service;

import pl.mazurekIT.sii.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User getUserById(Long id);

    User getUserByName(String name);

    List<User> getAllUsers();

    List<String> getAllUsersNames();

    void deleteUser(Long id);

    void deleteUser(User user);

    long countUsers();
}
