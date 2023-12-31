package com.example.loginJwtRSA.setting.security;

import com.example.loginJwtRSA.utils.ReturnValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ControllerSecurity {
    private final ServiceAuth serviceAuth;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody Sign.SigningUp signingUp) {
        ResponseEntity<?> responseEntity = null;
        try {
            Sign.SignedUp signedUp = serviceAuth.signup(signingUp);

            responseEntity = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(signedUp);
        } catch (RuntimeException exception) {
            log.warn(exception.getMessage());
            Map<String, Object> response = ReturnValues
                    .createReturnMessage(
                            exception.getMessage()
                    );

            responseEntity = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        return responseEntity;
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<?> signin(@RequestBody Sign.SigningIn signingIn) {
        ResponseEntity<?> responseEntity = null;
        try {
            Sign.SignedIn signedIn = serviceAuth.signin(signingIn);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + signedIn.getToken());

            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body(signedIn);
        } catch (RuntimeException exception) {
            log.warn(exception.getMessage());
            Map<String, Object> response = ReturnValues
                    .createReturnMessage(
                            exception.getMessage()
                    );

            responseEntity = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        return responseEntity;
    }

    @PostMapping("/auth/apikey")
    public ResponseEntity<?> registerApiKey(@RequestBody Sign.RegisteringApiKey registeringApiKey) {
        Sign.RegisteredApiKey registeredApiKey = serviceAuth.registerApiKey(registeringApiKey);
        if (registeredApiKey != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(registeredApiKey);
        }

        log.warn("Fail to register API Key of user {}", registeringApiKey.getIdUser());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(ReturnValues.createReturnMessage("Fail to register API Key"));
    }
}
