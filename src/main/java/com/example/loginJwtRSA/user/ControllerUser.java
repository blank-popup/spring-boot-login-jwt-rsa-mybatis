package com.example.loginJwtRSA.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class ControllerUser {
    private final ServiceUser serviceUser;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/base/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable(value="username") String username) {
        RequestInformation requestInformation = new RequestInformation();
        requestInformation.setUsername(username);
        return serviceUser.getUserByUsername(requestInformation);
    }

    @PostMapping("/base")
    public ResponseEntity<?> createUser(@RequestBody ModelInformation modelInformation) {
        return serviceUser.createUser(modelInformation);
    }

    @PutMapping("/base/{username}")
    public ResponseEntity<?> putUser(@PathVariable(value="username") String username, @RequestBody ModelInformation modelInformation) {
        modelInformation.setUsername(username);
        return  serviceUser.putUser(modelInformation);
    }

    @PatchMapping("/base/{username}")
    public ResponseEntity<?> patchUser(@PathVariable(value="username") String username, @RequestBody ModelInformation modelInformation) {
        modelInformation.setUsername(username);
        return serviceUser.patchUser(modelInformation);
    }

    @DeleteMapping("/base/{username}")
    public ResponseEntity<?> removeUser(@PathVariable(value="username") String username) {
        ModelInformation modelInformation = new ModelInformation();
        modelInformation.setUsername(username);
        return serviceUser.removeUser(modelInformation);
    }

    @Value("${directory.user.image}")
    private String directoryUserImage;

    @GetMapping(value="/image/{filenameServer}")
    public void downloadUserImage(HttpServletResponse response, @PathVariable(value="filenameServer") String filenameServer) {
        RequestImage requestImage = new RequestImage();
        requestImage.setFilenameServer(filenameServer);
        serviceUser.downloadUserImage(response, requestImage);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadUserImage(RequestImage requestImage, @RequestPart(value = "userImage", required = true) MultipartFile file) {
        return serviceUser.uploadUserImage(requestImage, file);
    }
}
