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
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new LinkedHashMap<>();
        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/userAll")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/GetUserAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
                        responseHeaders(),
                        pathParameters(),
                        requestFields(),
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
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("username", "nonuser");
        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/user")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/GetUserByUsername",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
                        responseHeaders(),
                        pathParameters(),
                        requestParameters(),
                        requestFields(
                                CommonTest.createRequestFields(
                                        "username",
                                        JsonFieldType.STRING,
                                        "ID to select",
                                        false,
                                        null,
                                        "username"
                                )
                        ),
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
                        RestDocumentationRequestBuilders.post("/api/v1/user")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/CreateUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
                        responseHeaders(),
                        pathParameters(),
                        requestParameters(),
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
                                )
                        ),
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
    @DisplayName("Modify User")
    void modifyUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("username", "nonuser");
        map.put("password", "newpass");
        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put("/api/v1/user")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/ModifyUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
                        responseHeaders(),
                        pathParameters(),
                        requestParameters(),
                        requestFields(
                                CommonTest.createRequestFields(
                                        "username",
                                        JsonFieldType.STRING,
                                        "ID to modify",
                                        false,
                                        null,
                                        "username"
                                ),
                                CommonTest.createRequestFields(
                                        "password",
                                        JsonFieldType.STRING,
                                        "Password to modify",
                                        false,
                                        "Plain password",
                                        "newpass"
                                )
                        ),
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
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("username", "nonuser");
        String content = mapper.writeValueAsString(map);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/user")
                                .header(keyAuthorization, valueAuthorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(document("/user/RemoveUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(CommonTest.createRequestHeadersCommon()),
                        responseHeaders(),
                        pathParameters(),
                        requestParameters(),
                        requestFields(
                                CommonTest.createRequestFields(
                                        "username",
                                        JsonFieldType.STRING,
                                        "ID to remove",
                                        false,
                                        null,
                                        "username"
                                )
                        ),
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

//    @Test
//    void testTemplat() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("username", "newuser");
//        map.put("password", "newpass");
//        map.put("roles", new ArrayList<String>(Arrays.asList("ROLE_USER", "ROLE_MANAGER")));
//        String content = mapper.writeValueAsString(map);
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders.get("/api/v1/user/{username}", "amuuser")
//                                .with(user("admin").password("pass").roles("USER","ADMIN"))
//                                .header(keyAuthorization, valueAuthorization)
////                                .param("param1", "12")
////                                .param("param2", "charley")
////                                .with(user("username").password("0000").roles("USER", "ADMIN", "MANAGER"))
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
//                )
//                .andExpect(status().isOk())
//                .andDo(document("/user/GetUserByUsername",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(CommonTest.createRequestHeadersCommon());
//                        responseHeaders(),
//                        pathParameters(
//                                CommonTest.createRequestParameter(
//                                        "username",
//                                        "ID to modify",
//                                        false,
//                                        null,
//                                        "username"
//                                )
//                        ),
//                        requestParameters(
//                                CommonTest.createRequestParameter(
//                                        "username",
//                                        "ID to modify",
//                                        false,
//                                        null,
//                                        "username"
//                                )
//                        ),
//                        requestFields(
//                                CommonTest.createRequestFields(
//                                        "username",
//                                        JsonFieldType.STRING,
//                                        "ID to modify",
//                                        false,
//                                        null,
//                                        "username"
//                                ),
//                                CommonTest.createRequestFields(
//                                        "password",
//                                        JsonFieldType.STRING,
//                                        "Password to modify",
//                                        false,
//                                        "Plain password",
//                                        "newpass"
//                                )
//                        ),
//                        responseFields(
//                                CommonTest.createResponseFields(
//                                        "id",
//                                        JsonFieldType.NUMBER,
//                                        "The unique number of user",
//                                        false,
//                                        null,
//                                        1
//                                ),
//                                CommonTest.createResponseFields(
//                                        "username",
//                                        JsonFieldType.STRING,
//                                        "ID of user",
//                                        false,
//                                        null,
//                                        "username"
//                                ),
//                                CommonTest.createResponseFields(
//                                        "password",
//                                        JsonFieldType.STRING,
//                                        "Password of user",
//                                        false,
//                                        "Encrypted password",
//                                        "$2a$10$ieKR2IiozV2KLfs5bp3JOe2tkjmYmHGDP8jlwXjO2fYrirO.Zd9YW"
//                                )
//                        )
//                ));
//    }
}