package com.example.loginJwtRSA.user;

import com.example.loginJwtRSA.security.UserDetailsCustom;
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

    private boolean verifyNotEmptyUsernamePassword(ModelUserBase modelUserBase) {
        if (modelUserBase.getUsername() == null || "".equals(modelUserBase.getUsername()) == true
                || modelUserBase.getPassword() == null || "".equals(modelUserBase.getPassword()) == true) {
            return false;
        }

        return true;
    }

//    private boolean verifyUsernamePassword(RequestInformation requestInformation) {
//        if (requestInformation.getUsername() == null || "".equals(requestInformation.getUsername()) == true
//                || requestInformation.getPassword() == null || "".equals(requestInformation.getPassword()) == true) {
//            return false;
//        }
//        ResponseInformation responseInformation = mapperUser.getUserByUsername(requestInformation.getUsername());
//        if (passwordEncoder.matches(requestInformation.getPassword(), responseInformation.getPassword()) == false) {
//            return false;
//        }
//
//        return true;
//    }
//
//    private boolean verifyCurrentUser(ModelInformation modelInformation) {
//        if (modelInformation.getUsername() == null || "".equals(modelInformation.getUsername()) == true
//                || modelInformation.getPassword() == null || "".equals(modelInformation.getPassword()) == true) {
//            return false;
//        }
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserDetails userDetails = (UserDetails)principal;
//        log.info("Current username: [{}] [{}]", userDetails.getUsername(), userDetails.getPassword());
//        if (modelInformation.getUsername().equals(userDetails.getUsername()) == false
//                || passwordEncoder.matches(modelInformation.getPassword(), userDetails.getPassword()) == false) {
//            return false;
//        }
//
//        return true;
//    }

    public ResponseEntity<?> getUser(RequestUserBase requestUserBase) {
        ResponseUserBase responseUserBase = mapperUser.getUser(requestUserBase.getUsername());
        return ResponseEntity
                .ok(responseUserBase);
    }

    @Transactional
    public ResponseEntity<?> createUser(ModelUserBase modelUserBase) {
        if (verifyNotEmptyUsernamePassword(modelUserBase) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }

        modelUserBase.setPassword(passwordEncoder.encode(modelUserBase.getPassword()));
        int count = mapperUser.createUser(modelUserBase);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> putUser(ModelUserBase modelUserBase) {
        if (verifyNotEmptyUsernamePassword(modelUserBase) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }
//        if (verifyCurrentUser(requestInformation) == false) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ReturnValues.createReturnMessage("Other cannot modify user information"));
//        }

        modelUserBase.setPassword(passwordEncoder.encode(modelUserBase.getPassword()));
        int count = mapperUser.putUser(modelUserBase);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        count = mapperUser.createUser(modelUserBase);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> patchUser(ModelUserBase modelUserBase) {
        if (verifyNotEmptyUsernamePassword(modelUserBase) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }
//        if (verifyCurrentUser(requestInformation) == false) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ReturnValues.createReturnMessage("Other cannot modify user information"));
//        }

        modelUserBase.setPassword(passwordEncoder.encode(modelUserBase.getPassword()));
        int count = mapperUser.patchUser(modelUserBase);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> removeUser(ModelUserBase modelUserBase) {
        int count = mapperUser.removeUser(modelUserBase);
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
        log.info("requestImage : {}", requestUserImage);
        String filenameClient = file.getOriginalFilename();
        log.info("file.getOriginalFilename() : {}", filenameClient);

        String filenameServer = OID.generateType1UUID().toString();
        log.info("filenameServer : {}", filenameServer);

        requestUserImage.setFilenameServer(filenameServer);
        requestUserImage.setFilenameClient(filenameClient);

        try {
            Path directoryUpdate = Paths.get(directoryUserImage);
            log.error("$$$$$ directoryUpdate : {}", directoryUpdate);
            Files.createDirectories(directoryUpdate);
            log.error("$$$$$ created directory : {}", directoryUpdate);
            Path targetPath = directoryUpdate.resolve(filenameServer).normalize();
            log.error("$$$$$ targetPath : {}", targetPath);
            file.transferTo(targetPath);
            log.error("$$$$$ transfered");
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
    
    public boolean validateMethodForSelf(String uriForAuthorization, String method, String uriWithoutContextPath, UserDetailsCustom userDetails) {
        String username = userDetails.getUsername();
        String[] uries = uriWithoutContextPath.split("/");
        if (uries.length >= 6) {
            if (username != null && username.equals(uries[5]) == true) {
                return true;
            }
        }

        return false;
    }
}
