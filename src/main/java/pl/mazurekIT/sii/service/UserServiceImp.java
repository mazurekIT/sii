package pl.mazurekIT.sii.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mazurekIT.sii.dal.UserRepository;
import pl.mazurekIT.sii.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public List<String> getAllUsersNames() {
        List<String> allUsersNames = new ArrayList<>();
        for (User x : userRepository.findAll()) {
            allUsersNames.add(x.getName());
        }
        return allUsersNames;
    }

}
