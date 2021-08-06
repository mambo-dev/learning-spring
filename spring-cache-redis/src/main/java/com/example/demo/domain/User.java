package com.example.demo.domain;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 1384295212965556694L;

    private String username;
    private String password;
    private int sex;
}
