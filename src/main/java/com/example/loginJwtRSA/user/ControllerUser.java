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
@RequestMapping("/api/v1/users")
public class ControllerUser {
    private final ServiceUser serviceUser;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/base/{username}")
    public ResponseEntity<?> getUser(@PathVariable(value="username") String username) {
        RequestUserBase requestUserBase = new RequestUserBase();
        requestUserBase.setUsername(username);
        return serviceUser.getUser(requestUserBase);
    }

    @PostMapping("/base")
    public ResponseEntity<?> createUser(@RequestBody ModelUserBase modelUserBase) {
        return serviceUser.createUser(modelUserBase);
    }

    @PutMapping("/base/{username}")
    public ResponseEntity<?> putUser(@PathVariable(value="username") String username, @RequestBody ModelUserBase modelUserBase) {
        modelUserBase.setUsername(username);
        return  serviceUser.putUser(modelUserBase);
    }

    @PatchMapping("/base/{username}")
    public ResponseEntity<?> patchUser(@PathVariable(value="username") String username, @RequestBody ModelUserBase modelUserBase) {
        modelUserBase.setUsername(username);
        return serviceUser.patchUser(modelUserBase);
    }

    @DeleteMapping("/base/{username}")
    public ResponseEntity<?> removeUser(@PathVariable(value="username") String username) {
        ModelUserBase modelUserBase = new ModelUserBase();
        modelUserBase.setUsername(username);
        return serviceUser.removeUser(modelUserBase);
    }

    @Value("${directory.user.image}")
    private String directoryUserImage;

    @GetMapping(value="/image/{filenameServer}")
    public void downloadUserImage(HttpServletResponse response, @PathVariable(value="filenameServer") String filenameServer) {
        RequestUserImage requestUserImage = new RequestUserImage();
        requestUserImage.setFilenameServer(filenameServer);
        serviceUser.downloadUserImage(response, requestUserImage);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadUserImage(RequestUserImage requestUserImage, @RequestPart(value = "userImage", required = true) MultipartFile file) {
        return serviceUser.uploadUserImage(requestUserImage, file);
    }
}
