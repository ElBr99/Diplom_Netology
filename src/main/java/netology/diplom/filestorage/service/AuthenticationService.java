package netology.diplom.filestorage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netology.diplom.filestorage.dto.UserDto;
import netology.diplom.filestorage.entity.User;
import netology.diplom.filestorage.exception.IncorrectDataEntry;
import netology.diplom.filestorage.exception.UserNotFoundException;
import netology.diplom.filestorage.dto.TokenDto;
import netology.diplom.filestorage.repository.UserRepository;
import netology.diplom.filestorage.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public TokenDto authenticationLogin(UserDto userDto) {
        log.info("Поиск пользователя в базе данных по логину: {}", userDto.getLogin());
        final User userFromDatabase = userRepository.findUserByLogin(userDto.getLogin()).orElseThrow(()
                -> new UserNotFoundException("Пользователь не найден", 0));

        if (passwordEncoder.matches(userDto.getPassword(), userFromDatabase.getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            return new TokenDto(jwt);
        } else {
            throw new IncorrectDataEntry("Неправильный пароль", 0);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userEntity = userRepository.findUserByLogin(auth.getPrincipal().toString()).orElseThrow(()
                -> new UserNotFoundException("Пользователь не найден", 0));
        log.info("Пользователь начал процедуру выхода из системы: {}", userEntity.getLogin());
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, auth);

    }
}
