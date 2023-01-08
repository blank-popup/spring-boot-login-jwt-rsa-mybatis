package com.example.loginJwtRSA.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/security")
public class SecurityController {
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody ModelUser modelUser) {
        log.trace("$$$$$ RestController trace login start");
        log.debug("$$$$$ RestController debug login start");
        log.info("$$$$$ RestController info login start");
        log.warn("$$$$$ RestController warn login start");
        log.error("$$$$$ RestController error login start");
        if ("18".equals(modelUser.getPassword())) {
            log.info("$$$$$ RestController login success");
            modelUser.setUsername("ImPossible");
        }
        else {
            log.info("$$$$$ RestController login fialure");
            modelUser.setUsername("ImnotUser");
        }
        return ResponseEntity.status(HttpStatus.OK).body(modelUser);
    }
}
