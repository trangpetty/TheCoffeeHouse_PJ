package com.example.thecoffeehouse.Utils.service;

import com.example.thecoffeehouse.entity.user.User;
import com.example.thecoffeehouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointsService {

    @Autowired
    private UserRepository userRepository;

    public void updatePoints(Long userId, int newPoints) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPoint(newPoints);

        // Cập nhật hạng
        user.setMembershipLevel(determineMembershipLevel(newPoints));

        userRepository.save(user);
    }

    private String determineMembershipLevel(int points) {
        if (points > 5000000) return "Diamond";
        if (points > 3000000) return "Gold";
        if (points > 1000000) return "Silver";
        return "Basic";
    }
}
