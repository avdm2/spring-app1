package homework.spring.service;

import homework.spring.domain.entity.Session;
import homework.spring.domain.entity.User;
import homework.spring.domain.repository.SessionRepository;
import homework.spring.security.JWTGenerator;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class SessionService {

    public final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void saveSession(User user, JWTGenerator jwtGenerator) {
        Session session = new Session()
                .setUser(user)
                .setSessionToken(jwtGenerator.generateToken(user.getId().toString()))
                .setExpiresAt(jwtGenerator.expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        sessionRepository.save(session);
    }
}
