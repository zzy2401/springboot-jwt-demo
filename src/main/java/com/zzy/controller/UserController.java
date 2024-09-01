package com.zzy.controller;

import com.zzy.utils.JwtUtil;
import com.auth0.jwt.JWT;
import com.zzy.entity.Results;
import com.zzy.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @PostMapping("/login")
    public Results login(@RequestBody User user) {
        String token = JwtUtil.generateTokenByNames(user.getUsername());
        return Results.success().Data("token", token);
    }

    @GetMapping("/info")
    public Results info(String token) {
        String username = JWT.decode(token).getSubject();
        return Results.success().Data("username", username);
    }
}
