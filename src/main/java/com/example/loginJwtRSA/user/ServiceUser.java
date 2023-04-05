package com.example.loginJwtRSA.user;

import com.example.loginJwtRSA.utils.ReturnValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ServiceUser {
    private final MapperUser mapperUser;
    private final PasswordEncoder passwordEncoder;
    @Value("${directory.user.image}")
    private String directoryUserImage;

    private boolean verifyNotEmptyUsernamePassword(RequestUser requestUser) {
        if (requestUser.getUsername() == null || "".equals(requestUser.getUsername()) == true
                || requestUser.getPassword() == null || "".equals(requestUser.getPassword()) == true) {
            return false;
        }

        return true;
    }

    private boolean verifyUsernamePassword(RequestUser requestUser) {
        if (requestUser.getUsername() == null || "".equals(requestUser.getUsername()) == true
                || requestUser.getPassword() == null || "".equals(requestUser.getPassword()) == true) {
            return false;
        }
        ResponseUser responseUser = mapperUser.getUserByUsername(requestUser.getUsername());
        if (passwordEncoder.matches(requestUser.getPassword(), responseUser.getPassword()) == false) {
            return false;
        }

        return true;
    }

    private boolean verifyCurrentUser(RequestUser requestUser) {
        if (requestUser.getUsername() == null || "".equals(requestUser.getUsername()) == true
                || requestUser.getPassword() == null || "".equals(requestUser.getPassword()) == true) {
            return false;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        log.info("Current username: [{}] [{}]", userDetails.getUsername(), userDetails.getPassword());
        if (requestUser.getUsername().equals(userDetails.getUsername()) == false
                || passwordEncoder.matches(requestUser.getPassword(), userDetails.getPassword()) == false) {
            return false;
        }

        return true;
    }

    public ResponseEntity<?> getUserAll() {
        List<ResponseUser> responseUsers = mapperUser.getUserAll();
        return ResponseEntity
                .ok(ReturnValues.createReturnListCount(responseUsers));
    }

    public ResponseEntity<?> getUserByUsername(RequestUser requestUser) {
        ResponseUser responseUser = mapperUser.getUserByUsername(requestUser.getUsername());
        return ResponseEntity
                .ok(responseUser);
    }

    @Transactional
    public ResponseEntity<?> createUser(RequestUser requestUser) {
        if (verifyNotEmptyUsernamePassword(requestUser) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }

        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        int count = mapperUser.createUser(requestUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> putUser(RequestUser requestUser) {
        if (verifyNotEmptyUsernamePassword(requestUser) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }
//        if (verifyCurrentUser(requestUser) == false) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ReturnValues.createReturnMessage("Other cannot modify user information"));
//        }

        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        int count = mapperUser.putUser(requestUser);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        count = mapperUser.createUser(requestUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> patchUser(RequestUser requestUser) {
        if (verifyNotEmptyUsernamePassword(requestUser) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }
//        if (verifyCurrentUser(requestUser) == false) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ReturnValues.createReturnMessage("Other cannot modify user information"));
//        }

        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        int count = mapperUser.patchUser(requestUser);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> removeUser(RequestUser requestUser) {
        int count = mapperUser.removeUser(requestUser.getUsername());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ReturnValues.createReturnCount(count));
    }

    public void downloadUserImage(HttpServletResponse response, RequestUserImage requestUserImage) {
        try {
            String path = directoryUserImage + "/" + requestUserImage.getFilenameServer();

            File file = new File(path);
            if (file.exists() == false || file.isDirectory() == true) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }

            String filenameClient = mapperUser.getFilenameClientByFilenameServer(requestUserImage.getFilenameServer());
            if (filenameClient == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filenameClient);

            FileInputStream fileInputStream = new FileInputStream(path);
            OutputStream out = response.getOutputStream();

            int read = 0;
            byte[] buffer = new byte[4096];
            while ((read = fileInputStream.read(buffer)) != -1) { // 1024바이트씩 계속 읽으면서 outputStream에 저장, -1이 나오면 더이상 읽을 파일이 없음
                out.write(buffer, 0, read);
            }

        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
        }
    }

    @Transactional
    public ResponseEntity uploadUserImage(RequestUserImage requestUserImage, MultipartFile file) {
        log.info("requestUserImage : {}", requestUserImage);
        String filenameClient = file.getOriginalFilename();
        log.info("file.getOriginalFilename() : {}", filenameClient);

        String filenameServer = UUID.randomUUID().toString();
        log.info("filenameServer : {}", filenameServer);

        requestUserImage.setFilenameServer(filenameServer);
        requestUserImage.setFilenameClient(filenameClient);

        try {
            Path directoryUpdate = Paths.get(directoryUserImage);
            Files.createDirectories(directoryUpdate);
            Path targetPath = directoryUpdate.resolve(filenameServer).normalize();
            file.transferTo(targetPath);
        } catch (Exception e) {
            log.warn("Exception uploadUserImage: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(ReturnValues.createReturnMessage(e.getMessage()));
        }

        int count = mapperUser.createUserImage(requestUserImage);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }
}
