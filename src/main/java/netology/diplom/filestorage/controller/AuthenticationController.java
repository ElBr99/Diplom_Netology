package netology.diplom.filestorage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netology.diplom.filestorage.config.AuthConstants;
import netology.diplom.filestorage.dto.UserDto;
import netology.diplom.filestorage.dto.TokenDto;
import netology.diplom.filestorage.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authenticationLogin(@RequestBody UserDto userDto) {
        TokenDto token = authenticationService.authenticationLogin(userDto);
        log.info("Пользователь: {} успешно вошел в систему. Auth-token: {}", userDto, token.getToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = AuthConstants.AUTH_TOKEN) String authToken,
                                       HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
