package com.example.loginJwtRSA.user;

import com.example.loginJwtRSA.utils.OID;
import com.example.loginJwtRSA.utils.ReturnValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private boolean verifyNotEmptyUsernamePassword(RequestInformation requestInformation) {
        if (requestInformation.getUsername() == null || "".equals(requestInformation.getUsername()) == true
                || requestInformation.getPassword() == null || "".equals(requestInformation.getPassword()) == true) {
            return false;
        }

        return true;
    }

    private boolean verifyUsernamePassword(RequestInformation requestInformation) {
        if (requestInformation.getUsername() == null || "".equals(requestInformation.getUsername()) == true
                || requestInformation.getPassword() == null || "".equals(requestInformation.getPassword()) == true) {
            return false;
        }
        ResponseInformation responseInformation = mapperUser.getUserByUsername(requestInformation.getUsername());
        if (passwordEncoder.matches(requestInformation.getPassword(), responseInformation.getPassword()) == false) {
            return false;
        }

        return true;
    }

    private boolean verifyCurrentUser(RequestInformation requestInformation) {
        if (requestInformation.getUsername() == null || "".equals(requestInformation.getUsername()) == true
                || requestInformation.getPassword() == null || "".equals(requestInformation.getPassword()) == true) {
            return false;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        log.info("Current username: [{}] [{}]", userDetails.getUsername(), userDetails.getPassword());
        if (requestInformation.getUsername().equals(userDetails.getUsername()) == false
                || passwordEncoder.matches(requestInformation.getPassword(), userDetails.getPassword()) == false) {
            return false;
        }

        return true;
    }

    public ResponseEntity<?> getUserAll() {
        List<ResponseInformation> responseInformations = mapperUser.getUserAll();
        return ResponseEntity
                .ok(ReturnValues.createReturnListCount(responseInformations));
    }

    public ResponseEntity<?> getUserByUsername(RequestInformation requestInformation) {
        ResponseInformation responseInformation = mapperUser.getUserByUsername(requestInformation.getUsername());
        return ResponseEntity
                .ok(responseInformation);
    }

    @Transactional
    public ResponseEntity<?> createUser(RequestInformation requestInformation) {
        if (verifyNotEmptyUsernamePassword(requestInformation) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }

        requestInformation.setPassword(passwordEncoder.encode(requestInformation.getPassword()));
        int count = mapperUser.createUser(requestInformation);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> putUser(RequestInformation requestInformation) {
        if (verifyNotEmptyUsernamePassword(requestInformation) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }
//        if (verifyCurrentUser(requestInformation) == false) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ReturnValues.createReturnMessage("Other cannot modify user information"));
//        }

        requestInformation.setPassword(passwordEncoder.encode(requestInformation.getPassword()));
        int count = mapperUser.putUser(requestInformation);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        count = mapperUser.createUser(requestInformation);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> patchUser(RequestInformation requestInformation) {
        if (verifyNotEmptyUsernamePassword(requestInformation) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }
//        if (verifyCurrentUser(requestInformation) == false) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ReturnValues.createReturnMessage("Other cannot modify user information"));
//        }

        requestInformation.setPassword(passwordEncoder.encode(requestInformation.getPassword()));
        int count = mapperUser.patchUser(requestInformation);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> removeUser(RequestInformation requestInformation) {
        int count = mapperUser.removeUser(requestInformation.getUsername());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ReturnValues.createReturnCount(count));
    }

    public void downloadUserImage(HttpServletResponse response, RequestImage requestImage) {
        try {
            String path = directoryUserImage + "/" + requestImage.getFilenameServer();

            File file = new File(path);
            if (file.exists() == false || file.isDirectory() == true) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }

            String filenameClient = mapperUser.getFilenameClientByFilenameServer(requestImage.getFilenameServer());
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
    public ResponseEntity uploadUserImage(RequestImage requestImage, MultipartFile file) {
        log.info("requestImage : {}", requestImage);
        String filenameClient = file.getOriginalFilename();
        log.info("file.getOriginalFilename() : {}", filenameClient);

        String filenameServer = OID.generateType1UUID().toString();
        log.info("filenameServer : {}", filenameServer);

        requestImage.setFilenameServer(filenameServer);
        requestImage.setFilenameClient(filenameClient);

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

        int count = mapperUser.createUserImage(requestImage);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }
}
