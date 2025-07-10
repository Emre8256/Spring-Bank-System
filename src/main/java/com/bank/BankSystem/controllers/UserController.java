package com.bank.BankSystem.controllers;

import com.bank.BankSystem.entity.User;
import com.bank.BankSystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserRepository userRepository;

    @GetMapping
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
        return ResponseEntity.ok("User successfully deleted!");
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User newUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (newUser.getName() != null) user.setName(newUser.getName());
        if (newUser.getSurname() != null) user.setSurname(newUser.getSurname());
        if (newUser.getAddress() != null) user.setAddress(newUser.getAddress());
        if (newUser.getPhone() != null) user.setPhone(newUser.getPhone());

        return userRepository.save(user);
    }
}
