package com.example.loginJwtRSA.user;

import com.example.loginJwtRSA.security.JwtTokenProvider;
import com.example.loginJwtRSA.utils.CommonTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles="test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class ControllerUserTest {
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private String keyAuthorization = "Authorization";
    private String valueAuthorization;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        valueAuthorization = "Bearer " + jwtTokenProvider.createToken(
                1L,
                null,
                null
        );

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(
//                                CommonTest.removeHeadersRequest(),
                                prettyPrint()
                        )
                        .withResponseDefaults(
//                                CommonTest.removeHeadersResponse(),
                                prettyPrint()
                        ))
                .build();
    }

    @Test
    @DisplayName("Get User All")
    public void getUserAll() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = new LinkedHashMap<>();
//        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/user/information")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/GetUserAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
//                        responseHeaders(),
//                        pathParameters(),
//                        requestFields(),
//                        requestParameters(),
//                        requestParts(),
                        responseFields(
                                CommonTest.createResponseFields(
                                        "data",
                                        JsonFieldType.ARRAY,
                                        "The array of member information",
                                        false,
                                        null,
                                        null
                                ),
                                CommonTest.createResponseFields(
                                        "data[].id",
                                        JsonFieldType.NUMBER,
                                        "The unique number of user",
                                        false,
                                        null,
                                        1
                                ),
                                CommonTest.createResponseFields(
                                        "data[].username",
                                        JsonFieldType.STRING,
                                        "ID of user",
                                        false,
                                        null,
                                        "username"
                                ),
                                CommonTest.createResponseFields(
                                        "data[].password",
                                        JsonFieldType.STRING,
                                        "Password of user",
                                        false,
                                        "Encrypted password",
                                        "$2a$10$ieKR2IiozV2KLfs5bp3JOe2tkjmYmHGDP8jlwXjO2fYrirO.Zd9YW"
                                ),
                                CommonTest.createResponseFields(
                                        "data[].email",
                                        JsonFieldType.STRING,
                                        "E-mail of user",
                                        true,
                                        "",
                                        "example@gmail.com"
                                ),
                                CommonTest.createResponseFields(
                                        "data[].phone",
                                        JsonFieldType.STRING,
                                        "Phone of user",
                                        true,
                                        "",
                                        "000-0000-0000"
                                ),
                                CommonTest.createResponseFields(
                                        "data[].createdAt",
                                        JsonFieldType.STRING,
                                        "When creating user",
                                        false,
                                        "",
                                        "2020-04-01T04:01:00"
                                ),
                                CommonTest.createResponseFields(
                                        "data[].updatedAt",
                                        JsonFieldType.STRING,
                                        "When updating user",
                                        false,
                                        "",
                                        "2020-04-01T04:01:00"
                                ),
                                CommonTest.createResponseFields(
                                        "count",
                                        JsonFieldType.NUMBER,
                                        "The count of user",
                                        false,
                                        null,
                                        8
                                )
                        )
                ));
    }

    @Test
    @DisplayName("Get User By Username")
    void getUserByUsername() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = new LinkedHashMap<>();
//        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/user/{username}/information", "nonuser")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/GetUserByUsername",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
//                        responseHeaders(),
                        pathParameters(
                                CommonTest.createPathParameter(
                                        "username",
                                        "ID to select",
                                        false,
                                        null,
                                        "username"
                                )
                        ),
//                        requestFields(),
//                        requestParameters(),
//                        requestParts(),
                        responseFields(
                                CommonTest.createResponseFields(
                                        "id",
                                        JsonFieldType.NUMBER,
                                        "The unique number of user",
                                        false,
                                        null,
                                        1
                                ),
                                CommonTest.createResponseFields(
                                        "username",
                                        JsonFieldType.STRING,
                                        "ID of user",
                                        false,
                                        null,
                                        "username"
                                ),
                                CommonTest.createResponseFields(
                                        "password",
                                        JsonFieldType.STRING,
                                        "Password of user",
                                        false,
                                        "Encrypted password",
                                        "$2a$10$ieKR2IiozV2KLfs5bp3JOe2tkjmYmHGDP8jlwXjO2fYrirO.Zd9YW"
                                ),
                                CommonTest.createResponseFields(
                                        "email",
                                        JsonFieldType.STRING,
                                        "E-mail of user",
                                        true,
                                        "",
                                        "example@gmail.com"
                                ),
                                CommonTest.createResponseFields(
                                        "phone",
                                        JsonFieldType.STRING,
                                        "Phone of user",
                                        true,
                                        "",
                                        "000-0000-0000"
                                ),
                                CommonTest.createResponseFields(
                                        "createdAt",
                                        JsonFieldType.STRING,
                                        "When creating user",
                                        false,
                                        "",
                                        "2020-04-01T04:01:00"
                                ),
                                CommonTest.createResponseFields(
                                        "updatedAt",
                                        JsonFieldType.STRING,
                                        "When updating user",
                                        false,
                                        "",
                                        "2020-04-01T04:01:00"
                                )
                        )
                ));
    }

    @Test
    @DisplayName("Create User")
    void createUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("username", "newuser");
        map.put("password", "newpass");
        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/user/information")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(document("/user/CreateUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
//                        responseHeaders(),
//                        pathParameters(),
                        requestFields(
                                CommonTest.createRequestFields(
                                        "username",
                                        JsonFieldType.STRING,
                                        "ID to create",
                                        false,
                                        null,
                                        "newuser"
                                ),
                                CommonTest.createRequestFields(
                                        "password",
                                        JsonFieldType.STRING,
                                        "Password to create",
                                        false,
                                        "Plain password",
                                        "newpass"
                                ),
                                CommonTest.createRequestFields(
                                        "email",
                                        JsonFieldType.STRING,
                                        "E-mail of user to create",
                                        true,
                                        "",
                                        "example@gmail.com"
                                ),
                                CommonTest.createRequestFields(
                                        "phone",
                                        JsonFieldType.STRING,
                                        "Phone of user to create",
                                        true,
                                        "Phone number",
                                        "000-0000-0000"
                                )
                        ),
//                        requestParameters(),
//                        requestParts(),
                        responseFields(
                                CommonTest.createResponseFields(
                                        "count",
                                        JsonFieldType.NUMBER,
                                        "The count of user created",
                                        false,
                                        null,
                                        1
                                )
                        )
                ));
    }

    @Test
    @DisplayName("Put User")
    void putUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("password", "newpass");
        map.put("email", "testPutUser@gmail.com");
        map.put("phone", "000-1111-9999");
        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put("/api/v1/user/{username}/information", "nonuser")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/PutUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
//                        responseHeaders(),
                        pathParameters(
                                CommonTest.createPathParameter(
                                        "username",
                                        "ID to select",
                                        false,
                                        null,
                                        "username"
                                )
                        ),
                        requestFields(
                                CommonTest.createRequestFields(
                                        "password",
                                        JsonFieldType.STRING,
                                        "Password to modify",
                                        false,
                                        "Plain password",
                                        "newpass"
                                ),
                                CommonTest.createRequestFields(
                                        "email",
                                        JsonFieldType.STRING,
                                        "E-mail of user",
                                        true,
                                        "",
                                        "example@gmail.com"
                                ),
                                CommonTest.createRequestFields(
                                        "phone",
                                        JsonFieldType.STRING,
                                        "Phone of user",
                                        true,
                                        "Phone number",
                                        "000-0000-0000"
                                )
                        ),
//                        requestParameters(),
//                        requestParts(),
                        responseFields(
                                CommonTest.createResponseFields(
                                        "count",
                                        JsonFieldType.NUMBER,
                                        "The count of user modified",
                                        false,
                                        null,
                                        1
                                )
                        )
                ));
    }

    @Test
    @DisplayName("Patch User")
    void patchUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("password", "newpass");
        map.put("email", "testPutUser@gmail.com");
        map.put("phone", "000-1111-9999");
        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/api/v1/user/{username}/information", "nonuser")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/PatchUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
//                        responseHeaders(),
                        pathParameters(
                                CommonTest.createPathParameter(
                                        "username",
                                        "ID to select",
                                        false,
                                        null,
                                        "username"
                                )
                        ),
                        requestFields(
                                CommonTest.createRequestFields(
                                        "password",
                                        JsonFieldType.STRING,
                                        "Password to modify",
                                        false,
                                        "Plain password",
                                        "newpass"
                                ),
                                CommonTest.createRequestFields(
                                        "email",
                                        JsonFieldType.STRING,
                                        "E-mail of user",
                                        true,
                                        "",
                                        "example@gmail.com"
                                ),
                                CommonTest.createRequestFields(
                                        "phone",
                                        JsonFieldType.STRING,
                                        "Phone of user",
                                        true,
                                        "Phone number",
                                        "000-0000-0000"
                                )
                        ),
//                        requestParameters(),
//                        requestParts(),
                        responseFields(
                                CommonTest.createResponseFields(
                                        "count",
                                        JsonFieldType.NUMBER,
                                        "The count of user modified",
                                        false,
                                        null,
                                        1
                                )
                        )
                ));
    }

    @Test
    @DisplayName("Remove User")
    void removeUser() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = new LinkedHashMap<>();
//        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/user/{username}/information", "nonuser")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
                )
                .andExpect(status().isNoContent())
                .andDo(document("/user/RemoveUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
//                        responseHeaders(),
                        pathParameters(
                                CommonTest.createPathParameter(
                                        "username",
                                        "ID to select",
                                        false,
                                        null,
                                        "username"
                                )
                        ),
//                        requestFields(),
//                        requestParameters(),
//                        requestParts(),
                        responseFields(
                                CommonTest.createResponseFields(
                                        "count",
                                        JsonFieldType.NUMBER,
                                        "The count of member removed",
                                        false,
                                        null,
                                        1
                                )
                        )
                ));
    }

    @Test
    @DisplayName("Download User Image")
    void downloadUserImage() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/user/image/{filenameServer}", "6318d0ae-873b-4bd8-a2c5-868597e2bc2a")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/DownloadUserImage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
//                        responseHeaders(),
                        pathParameters(
                                CommonTest.createPathParameter(
                                        "filenameServer",
                                        "Filename in server storage",
                                        false,
                                        null,
                                        "6318d0ae-873b-4bd8-a2c5-868597e2bc2a"
                                )
                        )
//                        requestFields(),
//                        requestParameters(),
//                        requestParts(),
//                        responseFields()
                ));
    }

    @Test
    @DisplayName("Upload User Image")
    void uploadUserImage() throws Exception {
        MockMultipartFile requestFile = new MockMultipartFile(
                "userImage",
                "image.png",
                "image/png",
                "<<<< Image Data >>>>".getBytes()
        );
//        MockMultipartFile requestFile = new MockMultipartFile(
//                "userImage",
//                "image.png",
//                "image/png",
//                new FileInputStream("C:/Users/syjeon/Downloads/image.png")
//        );
        mockMvc.perform(
                        RestDocumentationRequestBuilders.multipart("/api/v1/user/image")
                                .file(requestFile)
                                .param("id", "6")
                                .param("description", "description for image")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isCreated())
                .andDo(document("/user/UploadUserImage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
//                        responseHeaders(),
//                        pathParameters(),
//                        requestFields(),
                        requestParameters(
                                CommonTest.createRequestParameter(
                                        "id",
                                        "The unique number of user",
                                        false,
                                        null,
                                        3
                                ),
                                CommonTest.createRequestParameter(
                                        "description",
                                        "description for image",
                                        true,
                                        null,
                                        "selfie"
                                )
                        ),
                        requestParts(
                                CommonTest.createRequestPart(
                                        "userImage",
                                        "User Image",
                                        false,
                                        null,
                                        "Image file selected"
                                )
                        ),
                        responseFields(
                                CommonTest.createResponseFields(
                                        "count",
                                        JsonFieldType.NUMBER,
                                        "The count of image uploaded",
                                        false,
                                        null,
                                        1
                                )
                        )
                ));
    }
}
