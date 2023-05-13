package com.example.loginJwtRSA.user;

import com.example.loginJwtRSA.security.UserDetailsCustom;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class ServiceUser {
    private final MapperUser mapperUser;
    private final PasswordEncoder passwordEncoder;
    @Value("${directory.user.image}")
    private String directoryUserImage;

    private boolean verifyNotEmptyUsernamePassword(ModelInformation modelInformation) {
        if (modelInformation.getUsername() == null || "".equals(modelInformation.getUsername()) == true
                || modelInformation.getPassword() == null || "".equals(modelInformation.getPassword()) == true) {
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

    public ResponseEntity<?> getUserByUsername(RequestInformation requestInformation) {
        ResponseInformation responseInformation = mapperUser.getUserByUsername(requestInformation.getUsername());
        return ResponseEntity
                .ok(responseInformation);
    }

    @Transactional
    public ResponseEntity<?> createUser(ModelInformation modelInformation) {
        if (verifyNotEmptyUsernamePassword(modelInformation) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }

        modelInformation.setPassword(passwordEncoder.encode(modelInformation.getPassword()));
        int count = mapperUser.createUser(modelInformation);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> putUser(ModelInformation modelInformation) {
        if (verifyNotEmptyUsernamePassword(modelInformation) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }
//        if (verifyCurrentUser(requestInformation) == false) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ReturnValues.createReturnMessage("Other cannot modify user information"));
//        }

        modelInformation.setPassword(passwordEncoder.encode(modelInformation.getPassword()));
        int count = mapperUser.putUser(modelInformation);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        count = mapperUser.createUser(modelInformation);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> patchUser(ModelInformation modelInformation) {
        if (verifyNotEmptyUsernamePassword(modelInformation) == false) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ReturnValues.createReturnMessage("Invalid username or password"));
        }
//        if (verifyCurrentUser(requestInformation) == false) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ReturnValues.createReturnMessage("Other cannot modify user information"));
//        }

        modelInformation.setPassword(passwordEncoder.encode(modelInformation.getPassword()));
        int count = mapperUser.patchUser(modelInformation);
        if (count > 0) {
            return ResponseEntity
                    .ok(ReturnValues.createReturnCount(count));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ReturnValues.createReturnCount(count));
    }

    @Transactional
    public ResponseEntity<?> removeUser(ModelInformation modelInformation) {
        int count = mapperUser.removeUser(modelInformation);
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
