package com.example.loginJwtRSA.faculty.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ControllerUser {
    private final ServiceUser serviceUser;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable(value="username") String username) {
        User.Getting getting = new User.Getting();
        getting.setUsername(username);
        return serviceUser.getUser(getting);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User.Creating creating) {
        return serviceUser.createUser(creating);
    }

    @PutMapping("/user/{username}")
    public ResponseEntity<?> putUser(@PathVariable(value="username") String username, @RequestBody User.Putting putting) {
        putting.setUsername(username);
        return  serviceUser.putUser(putting);
    }

    @PatchMapping("/user/{username}")
    public ResponseEntity<?> patchUser(@PathVariable(value="username") String username, @RequestBody User.Patching patching) {
        patching.setUsername(username);
        return serviceUser.patchUser(patching);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> removeUser(@PathVariable(value="username") String username) {
        User.Removing removing = new User.Removing();
        removing.setUsername(username);
        return serviceUser.removeUser(removing);
    }

    @Value("${directory.user.image}")
    private String directoryUserImage;

    @GetMapping(value="/user-image/{filenameServer}")
    public void downloadUserImage(HttpServletResponse response, @PathVariable(value="filenameServer") String filenameServer) {
        UserImage.Downloading downloading = new UserImage.Downloading();
        downloading.setFilenameServer(filenameServer);
        serviceUser.downloadUserImage(response, downloading);
    }

    @PostMapping("/user-image")
    public ResponseEntity<?> uploadUserImage(UserImage.Uploading uploading, @RequestPart(value="userImage", required = true) MultipartFile file) {
        return serviceUser.uploadUserImage(uploading, file);
    }
}
