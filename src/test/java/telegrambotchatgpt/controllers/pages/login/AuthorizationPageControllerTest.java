package telegrambotchatgpt.controllers.pages.login;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import telegrambotchatgpt.TestBean;
import telegrambotchatgpt.dto.authorization.request.LoginRequest;
import telegrambotchatgpt.dto.authorization.request.RegistrationRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest(classes = TestBean.class)
class AuthorizationPageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${authorization.url}")
    private String authorizationUrl;


    @Transactional
    @Sql(value = {"add_unregistered_appuser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @CsvSource({
            "qwerty123, qwerty123",
            "TEST123123, TEST123123",
            "password1, password1"
    })
    @ParameterizedTest
    void testRegister(String password, String confirmPassword) throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("user");
        registrationRequest.setPassword(password);
        registrationRequest.setConfirmPassword(confirmPassword);
        mockMvc.perform(post(authorizationUrl + "registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registrationRequest)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Sql(value = {"add_unregistered_appuser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @CsvSource({
            "qwerty, qwerty",
            "qwerty1234, qwerty4321",
            "Q1233121, "
    })
    @ParameterizedTest
    void testRegister_UnprocessableEntity(String password, String confirmPassword) throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("user");
        registrationRequest.setPassword(password);
        registrationRequest.setConfirmPassword(confirmPassword);
        mockMvc.perform(post(authorizationUrl + "registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registrationRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Transactional
    @Sql(value = {"add_registered_appuser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @CsvSource({
            "qwerty111, qwerty111",
            "Qwerty1234, Qwerty1234",
            "Q1233121, Q1233121"
    })
    @ParameterizedTest
    void testRegister_Unauthorized(String password, String confirmPassword) throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("user");
        registrationRequest.setPassword(password);
        registrationRequest.setConfirmPassword(confirmPassword);
        mockMvc.perform(post(authorizationUrl + "registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registrationRequest)))
                .andExpectAll(status().isUnauthorized());
    }

    @CsvSource({
            "new_user, qwerty111, qwerty111",
            "user_test, Qwerty1234, Qwerty1234",
            "1 ,Q1233121, Q1233121"
    })
    @ParameterizedTest
    void testRegister_NotFound(String username, String password, String confirmPassword) throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername(username);
        registrationRequest.setPassword(password);
        registrationRequest.setConfirmPassword(confirmPassword);
        mockMvc.perform(post(authorizationUrl + "registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registrationRequest)))
                .andExpectAll(status().isNotFound());
    }


    @Transactional
    @Sql(value = {"add_registered_appuser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("NewPassword11");
        mockMvc.perform(post(authorizationUrl + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Transactional
    @Sql(value = {"add_registered_appuser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @CsvSource({
            "user, NewPassword110",
            "user, ",
            "user, Qf1"
    })
    @ParameterizedTest
    void testLogin_Unauthorized(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        mockMvc.perform(post(authorizationUrl + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @CsvSource({
            "test_user1, qwerty111",
            "test_user1, ",
            "test_user2, Qf1"
    })
    @ParameterizedTest
    void testLogin_NotFound(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        mockMvc.perform(post(authorizationUrl + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isNotFound());
    }



    private static String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
