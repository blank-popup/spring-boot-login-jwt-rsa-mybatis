package com.example.loginJwtRSA.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ControllerUser {
    private final ServiceUser serviceUser;

    @GetMapping("/user/information")
    public ResponseEntity<?> getUserAll() {
        return serviceUser.getUserAll();
    }

    @GetMapping("/user/{username}/information")
    public ResponseEntity<?> getUserByUsername(@PathVariable(value="username") String username) {
        RequestUser requestUser = new RequestUser();
        requestUser.setUsername(username);
        return  serviceUser.getUserByUsername(requestUser);
    }

    @PostMapping("/user/information")
    public ResponseEntity<?> createUser(@RequestBody RequestUser requestUser) {
        return  serviceUser.createUser(requestUser);
    }

    @PutMapping("/user/{username}/information")
    public ResponseEntity<?>  putUser(@PathVariable(value="username") String username, @RequestBody RequestUser requestUser) {
        requestUser.setUsername(username);
        return  serviceUser.putUser(requestUser);
    }

    @PatchMapping("/user/{username}/information")
    public ResponseEntity<?>  patchUser(@PathVariable(value="username") String username, @RequestBody RequestUser requestUser) {
        requestUser.setUsername(username);
        return  serviceUser.patchUser(requestUser);
    }

    @DeleteMapping("/user/{username}/information")
    public ResponseEntity<?> removeUser(@PathVariable(value="username") String username) {
        RequestUser requestUser = new RequestUser();
        requestUser.setUsername(username);
        return  serviceUser.removeUser(requestUser);
    }

    @Value("${directory.user.image}")
    private String directoryUserImage;
    @GetMapping(value="/user/image/{filenameServer}")
    public void downloadUserImage(HttpServletResponse response, @PathVariable(value="filenameServer") String filenameServer) {
        RequestUserImage requestUserImage = new RequestUserImage();
        requestUserImage.setFilenameServer(filenameServer);
        serviceUser.downloadUserImage(response, requestUserImage);
    }

    @PostMapping("/user/image")
    public ResponseEntity<?> uploadUserImage(RequestUserImage requestUserImage, @RequestPart(value = "userImage", required = true) MultipartFile file) {
        return serviceUser.uploadUserImage(requestUserImage, file);
    }
}
