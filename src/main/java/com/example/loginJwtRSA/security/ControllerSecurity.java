package com.example.loginJwtRSA.security;

import com.example.loginJwtRSA.utils.ReturnValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/security")
public class ControllerSecurity {
    @Value("${str.common}")
    private String strCommon;
    @Value("${str.individual}")
    private String strIndivudual;

    @Autowired ServiceUser serviceUser;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody RequestUser requestUser) {
        log.trace("$$$$$ RestController trace login start {} {}", strCommon, strIndivudual);
        log.debug("$$$$$ RestController debug login start {} {}", strCommon, strIndivudual);
        log.info("$$$$$ RestController info login start {} {}", strCommon, strIndivudual);
        log.warn("$$$$$ RestController warn login start {} {}", strCommon, strIndivudual);
        log.error("$$$$$ RestController error login start {} {}", strCommon, strIndivudual);
        if ("18".equals(requestUser.getPassword())) {
            log.info("$$$$$ RestController login success");
            requestUser.setUsername("ImPossible");
        }
        else {
            log.warn("$$$$$ RestController login fialure");
            requestUser.setUsername("ImnotUser");
        }
        return ResponseEntity.status(HttpStatus.OK).body(requestUser);
    }

    @PostMapping("/getAllUserInfo")
    public ResponseEntity<?> getAllUserInfo() {
        List<ResponseUser> responseUsers = serviceUser.getAllUserInfo();
        return ResponseEntity.ok(ReturnValues.createListCount(responseUsers));
    }

    @PostMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(@RequestBody RequestUser requestUser) {
        ResponseUser responseUser = serviceUser.getUserInfo(requestUser);
        return ResponseEntity.ok(responseUser);
    }

    @PostMapping("/createUserInfo")
    public ResponseEntity<?> createUserInfo(@RequestBody RequestUser requestUser) {
        int count = serviceUser.createUserInfo(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCount(count));
    }

    @PostMapping("/modifyUserPassword")
    public ResponseEntity<?>  modifyUserPassword(@RequestBody RequestUser requestUser) {
        int count = serviceUser.modifyUserPassword(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCount(count, "CANNOT update user[" + requestUser.getUsername() + "]"));
    }

    @PostMapping("/removeUserInfo")
    public ResponseEntity<?> removeUserInfo(@RequestBody RequestUser requestUser) {
        int count = serviceUser.removeUserInfo(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCount(count, "CANNOT delete user[" + requestUser.getUsername() + "]"));
    }
}
