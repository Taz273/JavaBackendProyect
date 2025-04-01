package com.example.Crud;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SqlUserService {
    private final SqlUserRepository userRepository;

    public List<SqlUser> getAllUsers() {
        return userRepository.findAll();
    }

    public List<SqlUser> searchByNameOrEmail(String keyword){
        return userRepository.searchByNameOrEmail(keyword);
    }
    public SqlUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Transactional
    public SqlUser createUser(@Valid SqlUser user) {
        return userRepository.save(user);
    }

    @Transactional
    public SqlUser updateUser(Long id, @Valid SqlUser updatedUser) {
        SqlUser user = getUserById(id);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
