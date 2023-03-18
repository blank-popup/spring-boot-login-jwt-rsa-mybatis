package com.example.loginJwtRSA.security;

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
    public ResponseEntity signup(@RequestBody RequestAuthSignUp requestAuthSignUp) {
        ResponseEntity responseEntity = null;
        try {
            ResponseAuthSignUp responseAuthSignUp = serviceAuth.signup(requestAuthSignUp);

            responseEntity = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseAuthSignUp);
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
    public ResponseEntity signin(@RequestBody RequestAuthSignIn requestAuthSignIn) {
        ResponseEntity responseEntity = null;
        try {
            ResponseAuthSignIn responseAuthSignIn = serviceAuth.signin(requestAuthSignIn);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + responseAuthSignIn.getToken());

            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body(responseAuthSignIn);
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
}
