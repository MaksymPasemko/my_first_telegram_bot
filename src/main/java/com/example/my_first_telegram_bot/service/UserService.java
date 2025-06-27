package com.example.my_first_telegram_bot.service;

import com.example.my_first_telegram_bot.model.User;
import com.example.my_first_telegram_bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createOrUpdateUser(User user) {
        userRepository.save(user);
    }
}
