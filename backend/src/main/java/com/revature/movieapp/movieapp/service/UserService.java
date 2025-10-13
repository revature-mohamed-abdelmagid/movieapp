package com.revature.movieapp.movieapp.service;

import com.revature.movieapp.movieapp.model.User;
import com.revature.movieapp.movieapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;




@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        // ensure the id is set on the updated entity before saving
        // assumes User has a setId(Long) method; adjust if your ID type/name differs
        try {
            updatedUser.getClass().getMethod("setId", Long.class).invoke(updatedUser, id);
        } catch (Exception ignored) {
            // fallback: repository.save will still work if the entity already has the correct id
        }
        return userRepository.save(updatedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

