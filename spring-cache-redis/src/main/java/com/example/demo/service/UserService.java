package com.example.demo.service;

import com.example.demo.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CacheConfig
@Slf4j
@Service
public class UserService {

    @Cacheable(cacheNames = {"users"}, key = "#offset + #limit")
    public List<User> getUsers(int offset, int limit) {
        List<User> users = new ArrayList<>();
        User.UserBuilder userBuilder = User.builder();

        for(int i = 0; i < limit; i++) {
            User user = userBuilder.username(UUID.randomUUID().toString())
                    .password(null)
                    .sex(i / 2)
                    .build();
            users.add(user);
        }

        log.info("getUsers[{}, {}]", offset, limit);
        return users;
    }
}
