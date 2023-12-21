package com.example.loginJwtRSA.faculty.user;

import com.example.loginJwtRSA.utils.OID;
import com.example.loginJwtRSA.utils.ReturnValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Service
public class ServiceUser {
    private final MapperUser mapperUser;
    private final PasswordEncoder passwordEncoder;
    @Value("${directory.user.image}")
    private String directoryUserImage;

    private boolean verifyNotEmptyUsernamePassword(String username, String password) {
        if (username == null || "".equals(username) == true || password == null || "".equals(password) == true) {
            return false;
        }

        return true;
    }

    public ResponseEntity<?> getUser(User.Getting getting) {
        User.Gotten gotten = mapperUser.getUser(getting);
        return ResponseEntity
                .ok()
                .body(gotten);
    }

    @Transactional
    public ResponseEntity<?> createUser(User.Creating creating) {
        String username = creating.getUsername();
        String password = creating.getPassword();
        if (verifyNotEmptyUsernamePassword(username, password) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }

        creating.setPassword(passwordEncoder.encode(creating.getPassword()));
        int count = mapperUser.createUser(creating);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> putUser(User.Putting putting) {
        String username = putting.getUsername();
        String password = putting.getPassword();
        if (verifyNotEmptyUsernamePassword(username, password) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }

        putting.setPassword(passwordEncoder.encode(putting.getPassword()));
        int count = mapperUser.putUser(putting);
        return ResponseEntity
                .ok(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> patchUser(User.Patching patching) {
        String username = patching.getUsername();
        String password = patching.getPassword();
        if (verifyNotEmptyUsernamePassword(username, password) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }

        patching.setPassword(passwordEncoder.encode(patching.getPassword()));
        int count = mapperUser.patchUser(patching);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> removeUser(User.Removing removing) {
        int count = mapperUser.removeUser(removing);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ReturnValues.createReturnCount(count));
    }

    public void downloadUserImage(HttpServletResponse response, UserImage.Downloading downloading) {
        try {
            String path = directoryUserImage + "/" + downloading.getFilenameServer();

            File file = new File(path);
            if (file.exists() == false || file.isDirectory() == true) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }

            UserImage.Downloaded fileDownload = mapperUser.getFileDownload(downloading);
            if (fileDownload == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }
            String filenameClient = fileDownload.getFilenameClient();
            if (filenameClient == null || "".equals(filenameClient) == true) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filenameClient);

            FileInputStream fileInputStream = new FileInputStream(path);
            OutputStream out = response.getOutputStream();

            int read = 0;
            byte[] buffer = new byte[4096];
            while ((read = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            fileInputStream.close();

        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
        }
    }

    @Transactional
    public ResponseEntity<?> uploadUserImage(UserImage.Uploading uploading, MultipartFile file) {
        log.info("UserImage.Uploading : {}", uploading);
        String filenameClient = file.getOriginalFilename();
        log.info("file.getOriginalFilename() : {}", filenameClient);

        String filenameServer = OID.generateType1UUID().toString();
        log.info("filenameServer : {}", filenameServer);

        uploading.setFilenameServer(filenameServer);
        uploading.setFilenameClient(filenameClient);

        try {
            Path directoryUpdate = Paths.get(directoryUserImage);
            Files.createDirectories(directoryUpdate);
            Path targetPath = directoryUpdate.resolve(filenameServer).normalize();
            log.info("targetPath : {}", targetPath);
            file.transferTo(targetPath);
        } catch (Exception e) {
            log.warn("Exception uploadUserImage: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(ReturnValues.createReturnMessage(e.getMessage()));
        }

        int count = mapperUser.createUserImage(uploading);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }
}
