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
    public ResponseEntity signup(@RequestBody UserDetailsCustom userDetailsCustom) {
        ResponseEntity responseEntity = null;
        try {
            UserDetailsCustom newUser = serviceAuth.signup(userDetailsCustom);

            responseEntity = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newUser);
        } catch (RuntimeException exception) {
            log.debug(exception.getMessage());
            Map<String, Object> response = ReturnValues
                    .createReturnStatusMessage(
                            "Fail",
                            exception.getMessage()
                    );

            responseEntity = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        return responseEntity;
    }

    @PostMapping("/auth/signin")
    public ResponseEntity signin(@RequestBody RequestAuth requestAuth) {
        ResponseEntity responseEntity = null;
        try {
            ResponseAuth responseAuth = serviceAuth.signin(requestAuth);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + responseAuth.getToken());

            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body(responseAuth);
        } catch (RuntimeException exception) {
            log.debug(exception.getMessage());
            Map<String, Object> response = ReturnValues
                    .createReturnStatusMessage(
                            "Fail",
                            exception.getMessage()
                    );

            responseEntity = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        return responseEntity;
    }
}
