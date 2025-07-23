package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller that provides balance query functionality
 */

@RestController
public class BalanceController {

    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam Long userId) {
        // Look up user by ID in the database
        UserRecord user = userRepository.findById(userId.longValue());

        // Return 0 balance if user not found
        if (user == null) {
            return new Balance(0.0f);
        }
        // Return actual user balance
        return new Balance(user.getBalance());
    }
}