package homework.spring.controller;

import homework.spring.dto.auth.LoginDto;
import homework.spring.dto.auth.UserInfoDto;
import homework.spring.security.JWTGenerator;
import homework.spring.domain.entity.UserRole;
import homework.spring.domain.entity.User;
import homework.spring.dto.auth.RegisterDto;
import homework.spring.service.SessionService;
import homework.spring.service.UserService;
import homework.spring.utils.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.validator.routines.EmailValidator;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                          UserService userService, SessionService sessionService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (registerDto.getUserRole() == null || registerDto.getUsername() == null ||
                registerDto.getEmail() == null || registerDto.getPassword() == null) {
            return new ResponseEntity<>("Ошибка! Не все поля заполены!", HttpStatus.BAD_REQUEST);
        }
        if (userService.findByEmail(registerDto.getEmail()) != null) {
            return new ResponseEntity<>("Аккаунт с такой почтой уже зарегистрирован!", HttpStatus.BAD_REQUEST);
        } else if (userService.findByUsername(registerDto.getUsername()) != null) {
            return new ResponseEntity<>("Аккаунт с таким именем пользователя уже зарегистрирован!", HttpStatus.BAD_REQUEST);
        }

        UserRole userRole;
        try {
            userRole = RoleValidator.validate(registerDto.getUserRole());
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Некорректная роль!", HttpStatus.BAD_REQUEST);
        }

        if (!EmailValidator.getInstance().isValid(registerDto.getEmail())) {
            return new ResponseEntity<>("Почтовый адрес некорректен!", HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(registerDto, passwordEncoder, userRole);
        sessionService.saveSession(userService.findByEmail(registerDto.getEmail()), new JWTGenerator());

        return new ResponseEntity<>("Пользователь зарегистрирован успешно!", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        if (loginDto.getEmail() == null || loginDto.getPassword() == null) {
            return new ResponseEntity<>("Ошибка! Не все поля заполнены!", HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>("Вы вошли!", HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Неверные учетные данные!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("get_info")
    public ResponseEntity<String> getInfo(@RequestBody UserInfoDto userInfoDto) {
        if (userInfoDto.getUsername() == null) {
            return new ResponseEntity<>("Ошибка! Пустое имя пользователя!", HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByUsername(userInfoDto.getUsername());
        if (user == null) {
            return new ResponseEntity<>("Пользователь с указанным ником не зарегистрирован!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "ID = " + user.getId() +
                    "; USERNAME = " + user.getUsername() +
                    "; EMAIL = " + user.getEmail() +
                    "; ROLE = " + user.getUserRole().toString() +
                    "; CREATED_AT = " + user.getCreatedAt() +
                    ";", HttpStatus.OK);
        }
    }
}
