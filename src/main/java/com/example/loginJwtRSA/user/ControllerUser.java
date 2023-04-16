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

    @GetMapping("/userAll/information")
    public ResponseEntity<?> getUserAll() {
        return serviceUser.getUserAll();
    }

    @GetMapping("/user/{username}/information")
    public ResponseEntity<?> getUserByUsername(@PathVariable(value="username") String username) {
        RequestInformation requestInformation = new RequestInformation();
        requestInformation.setUsername(username);
        return  serviceUser.getUserByUsername(requestInformation);
    }

    @PostMapping("/user/information")
    public ResponseEntity<?> createUser(@RequestBody RequestInformation requestInformation) {
        return  serviceUser.createUser(requestInformation);
    }

    @PutMapping("/user/{username}/information")
    public ResponseEntity<?>  putUser(@PathVariable(value="username") String username, @RequestBody RequestInformation requestInformation) {
        requestInformation.setUsername(username);
        return  serviceUser.putUser(requestInformation);
    }

    @PatchMapping("/user/{username}/information")
    public ResponseEntity<?>  patchUser(@PathVariable(value="username") String username, @RequestBody RequestInformation requestInformation) {
        requestInformation.setUsername(username);
        return  serviceUser.patchUser(requestInformation);
    }

    @DeleteMapping("/user/{username}/information")
    public ResponseEntity<?> removeUser(@PathVariable(value="username") String username) {
        RequestInformation requestInformation = new RequestInformation();
        requestInformation.setUsername(username);
        return  serviceUser.removeUser(requestInformation);
    }

    @Value("${directory.user.image}")
    private String directoryUserImage;

    @GetMapping(value="/user/image/{filenameServer}")
    public void downloadUserImage(HttpServletResponse response, @PathVariable(value="filenameServer") String filenameServer) {
        RequestImage requestImage = new RequestImage();
        requestImage.setFilenameServer(filenameServer);
        serviceUser.downloadUserImage(response, requestImage);
    }

    @PostMapping("/user/image")
    public ResponseEntity<?> uploadUserImage(RequestImage requestImage, @RequestPart(value = "userImage", required = true) MultipartFile file) {
        return serviceUser.uploadUserImage(requestImage, file);
    }
}
