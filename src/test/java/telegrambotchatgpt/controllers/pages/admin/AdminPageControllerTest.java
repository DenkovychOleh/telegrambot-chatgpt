package telegrambotchatgpt.controllers.pages.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import telegrambotchatgpt.TestBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest(classes = TestBean.class)
class AdminPageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${admin.url}")
    private String adminUrl;


    @Transactional
    @Sql(value = {"add_appuser_with_ADMIN-role.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails(value = "user-admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void testOpenSecurePage() throws Exception {
        mockMvc.perform(get(adminUrl + "my-info"))
                .andExpect(status().isOk());
    }

    @Transactional
    @Sql(value = {"add_appuser_with_USER-role.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void testOpenSecurePage_Forbidden() throws Exception {
        mockMvc.perform(get(adminUrl + "my-info"))
                .andExpect(status().isForbidden());
    }


    @CsvSource({
            "admin, user-admin ",
            "AdMin, user-admin",
            "USER, user-admin",
            "user, user-admin"
    })
    @Transactional
    @Sql(value = {"add_appuser_with_ADMIN-role.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails(value = "user-admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @ParameterizedTest
    void testUpdateRole(String newRole, String username) throws Exception {
        mockMvc.perform(put(adminUrl + "username/" + username +
                        "/update-role/" + newRole))
                .andExpect(status().isOk());
    }

    @CsvSource({
            "admins, user-admin ",
            "Users, user-admin",
            ", user-admin"
    })
    @Transactional
    @Sql(value = {"add_appuser_with_ADMIN-role.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails(value = "user-admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @ParameterizedTest
    void testUpdateRole_UnprocessableEntity(String newRole, String username) throws Exception {
        mockMvc.perform(put(adminUrl + "username/" + username +
                        "/update-role/" + newRole))
                .andExpect(status().isUnprocessableEntity());
    }


    @Transactional
    @Sql(value = {"add_appuser_with_ADMIN-role.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails(value = "user-admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void testGetChatByUsername() throws Exception {
        mockMvc.perform(get(adminUrl + "chat/username/" + "user-admin"))
                .andExpect(status().isOk());
    }

}