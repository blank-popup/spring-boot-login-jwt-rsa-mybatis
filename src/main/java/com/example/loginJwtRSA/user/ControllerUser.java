package com.example.loginJwtRSA.user;

import com.example.loginJwtRSA.utils.ReturnValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ControllerUser {
    private final ServiceUser serviceUser;

    @GetMapping("/userAll")
    public ResponseEntity<?> getUserAll() {
        List<ResponseUser> responseUsers = serviceUser.getUserAll();
        return ResponseEntity.ok(ReturnValues.createReturnListCount(responseUsers));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByUsername(@RequestBody RequestUser requestUser) {
        ResponseUser responseUser = serviceUser.getUserByUsername(requestUser);
        return ResponseEntity.ok(responseUser);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody RequestUser requestUser) {
        int count = serviceUser.createUser(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCount(count));
    }

    @PutMapping("/user")
    public ResponseEntity<?>  modifyUser(@RequestBody RequestUser requestUser) {
        int count = serviceUser.modifyUser(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCount(count));
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> removeUser(@RequestBody RequestUser requestUser) {
        int count = serviceUser.removeUser(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCount(count));
    }
}
