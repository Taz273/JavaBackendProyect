package com.example.Crud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sql/users")
@RequiredArgsConstructor
public class SqlUserController {
    private final SqlUserService userService;

    @GetMapping
    public ResponseEntity<List<SqlUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SqlUser> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SqlUser>> searchUsers(@RequestParam String keyword){
        return ResponseEntity.ok(userService.searchByNameOrEmail(keyword));
    }

    @PostMapping
    public ResponseEntity<SqlUser> createUser(@RequestBody SqlUser user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SqlUser> updateUser(@PathVariable Long id, @RequestBody SqlUser user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}