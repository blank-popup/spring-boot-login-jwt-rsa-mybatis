package com.example.loginJwtRSA.setting.security;

import com.example.loginJwtRSA.utils.CommonTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles="test_server")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class ControllerSecurityTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
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

//     @Test
//     @DisplayName("Sign Up")
//     @Order(1)
//     public void signup() throws Exception {
//         ObjectMapper mapper = new ObjectMapper();
//         Map<String, Object> map = new LinkedHashMap<>();
//         map.put("username", "newuser");
//         map.put("password", "newpass");
//         map.put("roles", new ArrayList<String>(Arrays.asList("ROLE_USER", "ROLE_MANAGER")));
//         String content = mapper.writeValueAsString(map);
//         mockMvc.perform(
//                         RestDocumentationRequestBuilders.post("/api/v1/auth/signup")
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 )
//                 .andExpect(status().isCreated())
//                 .andDo(document("auth/SignUp",
//                         preprocessRequest(prettyPrint()),
//                         preprocessResponse(prettyPrint()),
//                         requestHeaders(CommonTest.createRequestHeadersWithoutAuthorization()),
// //                        responseHeaders(),
// //                        pathParameters(),
//                         requestFields(
//                                 CommonTest.createRequestFields("username", JsonFieldType.STRING, "ID to sign up", false, null, "newuser"),
//                                 CommonTest.createRequestFields("password", JsonFieldType.STRING, "Password to sign up", false, "Plain password", "newpass"),
//                                 CommonTest.createRequestFields("roles", JsonFieldType.ARRAY, "The array of role", false, "ROLE_ADMIN, ROLE_MANAGER, ROLE_USER", "[ROLE_USER]")
//                         ),
// //                        requestParameters(),
// //                        requestParts(),
//                         responseFields(
//                                 CommonTest.createResponseFields("id", JsonFieldType.NUMBER, "The unique number of user signed up", false, null, 1),
//                                 CommonTest.createResponseFields("username", JsonFieldType.STRING, "ID signed up", false, null, "newuser"),
//                                 CommonTest.createResponseFields("roles", JsonFieldType.ARRAY, "The array of role", false, "ROLE_ADMIN, ROLE_MANAGER, ROLE_USER", "[ROLE_USER]")
//                         )
//                 ));
//     }

//     @Test
//     @DisplayName("Sign In")
//     @Order(2)
//     public void signin() throws Exception {
//         ObjectMapper mapper = new ObjectMapper();
//         Map<String, Object> map = new LinkedHashMap<>();
//         map.put("username", "uuser");
//         map.put("password", "0000");
//         String content =  mapper.writeValueAsString(map);
//         mockMvc.perform(
//                         RestDocumentationRequestBuilders.post("/api/v1/auth/signin")
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 )
//                 .andExpect(status().isOk())
//                 .andDo(document("auth/SignIn",
//                         preprocessRequest(prettyPrint()),
//                         preprocessResponse(prettyPrint()),
//                         requestHeaders(CommonTest.createRequestHeadersWithoutAuthorization()),
//                         responseHeaders(),
// //                        pathParameters(),
//                         requestFields(
//                                 CommonTest.createRequestFields("username", JsonFieldType.STRING, "ID to sign in", false, null, "username"),
//                                 CommonTest.createRequestFields("password", JsonFieldType.STRING, "Password to sign in", false, "Plain password", "password")
//                         ),
// //                        requestParameters(),
// //                        requestParts(),
//                         responseFields(
//                                 CommonTest.createResponseFields("id", JsonFieldType.NUMBER, "The unique number of user signed in", false, null, 1),
//                                 CommonTest.createResponseFields("username", JsonFieldType.STRING, "ID signed in", false, null, "username"),
//                                 CommonTest.createResponseFields("token", JsonFieldType.STRING, "Json Web Token to use until being expired", false, "Bearer (Base64 Code).(Base64 Code).(Base64 Code)", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjc4NjIyODMzLCJleHAiOjE2Nzg3MDkyMzN9.CmiZeCJn1cZBehZ8SRcFNgovhvwgLuXjeS61i2RFdYc"),
//                                 CommonTest.createResponseFields("roles", JsonFieldType.ARRAY, "The array of role", false, "ROLE_ADMIN, ROLE_MANAGER, ROLE_USER", "[{\"id\": 3, \"name\": \"ROLE_USER\"}]"),
//                                 CommonTest.createResponseFields("roles[].id", JsonFieldType.NUMBER, "The unique number of role", true, null, 3),
//                                 CommonTest.createResponseFields("roles[].name", JsonFieldType.STRING, "The name of role", true, "ROLE_ADMIN, ROLE_MANAGER, ROLE_USER", "ROLE_USER")
//                         )
//                 ));
//     }

//     @Test
//     @DisplayName("Register API Key")
//     @Order(3)
//     public void registerApiKey() throws Exception {
//         ObjectMapper mapper = new ObjectMapper();
//         Map<String, Object> map = new LinkedHashMap<>();
//         map.put("idUser", 1);
//         map.put("terms", 86400000);
//         String content =  mapper.writeValueAsString(map);
//         mockMvc.perform(
//                         RestDocumentationRequestBuilders.post("/api/v1/auth/apikey")
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 )
//                 .andExpect(status().isOk())
//                 .andDo(document("auth/RegisterApiKey",
//                         preprocessRequest(prettyPrint()),
//                         preprocessResponse(prettyPrint()),
//                         requestHeaders(CommonTest.createRequestHeadersWithoutAuthorization()),
//                         responseHeaders(),
// //                        pathParameters(),
//                         requestFields(
//                                 CommonTest.createRequestFields("idUser", JsonFieldType.NUMBER, "ID to register API Key", false, "ID not existing already", 1),
//                                 CommonTest.createRequestFields("terms", JsonFieldType.NUMBER, "Terms in milli-second", false, null, 86400000)
//                         ),
// //                        requestParameters(),
// //                        requestParts(),
//                         responseFields(
//                                 CommonTest.createRequestFields("id", JsonFieldType.NUMBER, "The unique number of API Key registered", false, null, 1),
//                                 CommonTest.createResponseFields("idUser", JsonFieldType.NUMBER, "The unique number of user registered API Key", false, null, 1),
//                                 CommonTest.createResponseFields("apiKey", JsonFieldType.STRING, "API Key available to user", false, null, "mqn6nH0/6hJkOOVrJlzuVL9p8bxGPNySp4gKdTwT5hjD+pIWmwGBS5FJJG0o1Vg02tXq1T2ak+mWu7B5M5t13tUxBWQNtTuHoD2/pIpLc2Q="),
//                                 CommonTest.createResponseFields("expireAt", JsonFieldType.STRING, "Datetime to be expired at", false, "Datetime format", "2025-04-02T12:44:44.731"),
//                                 CommonTest.createResponseFields("createdAt", JsonFieldType.STRING, "Datetime created at", false, "Datetime format", "2025-04-01T12:44:44.731"),
//                                 CommonTest.createResponseFields("updatedAt", JsonFieldType.STRING, "Datetime updated at", false, "Datetime format", "2025-04-01T12:44:44.731")
//                         )
//                 ));
//     }
}
