package com.example.loginJwtRSA.user;

import com.example.loginJwtRSA.utils.ReturnValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ControllerUser {
    @Autowired
    ServiceUser serviceUser;

    @PostMapping("/user/getAll")
    public ResponseEntity<?> getAllInfo() {
        List<ResponseUser> responseUsers = serviceUser.getAll();
        return ResponseEntity.ok(ReturnValues.createReturnListCount(responseUsers));
    }

    @PostMapping("/user/get")
    public ResponseEntity<?> getInfoByUsername(@RequestBody RequestUser requestUser) {
        ResponseUser responseUser = serviceUser.get(requestUser);
        return ResponseEntity.ok(responseUser);
    }

    @PostMapping("/user/create")
    public ResponseEntity<?> create(@RequestBody RequestUser requestUser) {
        int count = serviceUser.create(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCount(count));
    }

    @PostMapping("/user/modify")
    public ResponseEntity<?>  modify(@RequestBody RequestUser requestUser) {
        int count = serviceUser.modify(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCountMessage(count, "CANNOT update user[" + requestUser.getUsername() + "]"));
    }

    @PostMapping("/user/remove")
    public ResponseEntity<?> removeInfo(@RequestBody RequestUser requestUser) {
        int count = serviceUser.remove(requestUser);
        return ResponseEntity.ok(ReturnValues.createReturnCountMessage(count, "CANNOT delete user[" + requestUser.getUsername() + "]"));
    }
}
