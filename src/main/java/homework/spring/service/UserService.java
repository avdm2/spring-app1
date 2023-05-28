package homework.spring.service;

import homework.spring.domain.entity.UserRole;
import homework.spring.domain.entity.User;
import homework.spring.domain.repository.UserRepository;
import homework.spring.dto.auth.RegisterDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(RegisterDto registerDto, PasswordEncoder passwordEncoder, UserRole userRole) {
        User user = new User()
                .setEmail(registerDto.getEmail())
                .setUsername(registerDto.getUsername())
                .setPassword(passwordEncoder.encode(registerDto.getPassword()))
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .setUserRole(userRole);

        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
