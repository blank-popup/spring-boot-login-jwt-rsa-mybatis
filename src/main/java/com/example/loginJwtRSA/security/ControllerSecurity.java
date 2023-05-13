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
    public ResponseEntity signup(@RequestBody RequestSignUp requestSignUp) {
        ResponseEntity responseEntity = null;
        try {
            ResponseSignUp responseSignUp = serviceAuth.signup(requestSignUp);

            responseEntity = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseSignUp);
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
    public ResponseEntity signin(@RequestBody RequestSignIn requestSignIn) {
        ResponseEntity responseEntity = null;
        try {
            ResponseSignIn responseSignIn = serviceAuth.signin(requestSignIn);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + responseSignIn.getToken());

            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body(responseSignIn);
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
    public ResponseEntity registerApiKey(@RequestBody RequestApiKey requestApiKey) {
        ResponseApiKey responseApiKey = serviceAuth.registerApiKey(requestApiKey);
        if (responseApiKey != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(responseApiKey);
        }

        log.warn("Fail to register API Key of user {}", requestApiKey.getIdUser());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(ReturnValues.createReturnMessage("Fail to register API Key"));
    }
}
