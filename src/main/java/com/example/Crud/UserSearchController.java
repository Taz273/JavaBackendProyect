package com.example.Crud;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserSearchController {
    private final CustomUserRepository customUserRepository;
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String keyword){
        return customUserRepository.searchByNameOrEmail(keyword);
    }
}
