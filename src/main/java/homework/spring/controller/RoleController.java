package homework.spring.controller;

import homework.spring.domain.entity.User;
import homework.spring.domain.entity.UserRole;
import homework.spring.domain.repository.UserRepository;
import homework.spring.dto.auth.RoleDto;
import homework.spring.service.UserService;
import homework.spring.utils.RoleValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    public UserService userService;

    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("change")
    @PreAuthorize("hasRole('CHEF')")
    @Transactional
    public ResponseEntity<String> changeRole(@RequestBody RoleDto roleDto) {
        if (roleDto.getUsername() == null || roleDto.getUserRole() == null) {
            return new ResponseEntity<>("Ошибка! Поля не заполнены!", HttpStatus.BAD_REQUEST);
        }
        UserRole userRole;
        try {
            userRole = RoleValidator.validate(roleDto.getUserRole());
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Некорректная роль!", HttpStatus.BAD_REQUEST);
        }

        User user = userService
                .findByUsername(roleDto.getUsername())
                .setUserRole(userRole);
        userService.saveUser(user);

        user.setUpdatedAt(LocalDateTime.now());

        return new ResponseEntity<>(
                "Роль " + userRole.toString() +
                        " пользователю с никнеймом " + roleDto.getUsername() +
                        " успешно выдана!", HttpStatus.OK);
    }
}
