package pl.mazurekIT.sii.service;

import pl.mazurekIT.sii.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    void deleteUser(Long id);

    void deleteUser(User user);

    User updateUser(User user);

    long countUsers();
}
