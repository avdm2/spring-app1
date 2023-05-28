package homework.spring.controller;

import homework.spring.dto.LoginDto;
import homework.spring.security.JWTGenerator;
import homework.spring.domain.entity.Role;
import homework.spring.domain.entity.Session;
import homework.spring.domain.entity.User;
import homework.spring.domain.repository.SessionRepository;
import homework.spring.domain.repository.UserRepository;
import homework.spring.dto.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDateTime;
import java.time.ZoneId;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          SessionRepository sessionRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.findByEmail(registerDto.getEmail()) != null) {
            return new ResponseEntity<>("Аккаунт с такой почтой уже зарегистрирован!", HttpStatus.BAD_REQUEST);
        } else if (userRepository.findByUsername(registerDto.getUsername()) != null) {
            return new ResponseEntity<>("Аккаунт с таким именем пользователя уже зарегистрирован!", HttpStatus.BAD_REQUEST);
        }

        String roleStr = registerDto.getRole().toUpperCase();
        if (!roleStr.equals("CUSTOMER") && !roleStr.equals("CHEF") && !roleStr.equals("MANAGER")) {
            return new ResponseEntity<>("Неверная роль!", HttpStatus.BAD_REQUEST);
        }
        Role role = Role.valueOf(roleStr);

        if (!EmailValidator.getInstance().isValid(registerDto.getEmail())) {
            return new ResponseEntity<>("Почтовый адрес некорректен!", HttpStatus.BAD_REQUEST);
        }

        User user = new User()
                .setEmail(registerDto.getEmail())
                .setUsername(registerDto.getUsername())
                .setPassword(passwordEncoder.encode(registerDto.getPassword()))
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .setRole(role);

        userRepository.save(user);

        JWTGenerator jwtGenerator = new JWTGenerator();
        Session session = new Session()
                .setUser(user)
                .setSessionToken(jwtGenerator.generateToken(user.getId().toString()))
                .setExpiresAt(jwtGenerator.expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        sessionRepository.save(session);
        return new ResponseEntity<>("Пользователь зарегистрирован успешно!", HttpStatus.OK);
    }

    // TODO
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("Вы вошли!", HttpStatus.OK);
    }
}
