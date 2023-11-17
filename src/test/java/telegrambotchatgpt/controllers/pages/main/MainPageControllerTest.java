package telegrambotchatgpt.controllers.pages.main;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import telegrambotchatgpt.TestBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest(classes = TestBean.class)
class MainPageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${main.url}")
    private String mainUrl;


    @Sql(value = {"add_appuser_with_USER-role.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails(value = "user-test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void testGetAuthorizedAppUserRole() throws Exception {
        mockMvc.perform(get(mainUrl + "my-role"))
                .andExpectAll(
                        status().isOk(),
                        content().string("USER")
                );
    }

    @Test
    void testGetAuthorizedAppUserRole_Forbidden() throws Exception {
        mockMvc.perform(get(mainUrl + "my-role"))
                .andExpect(status().isForbidden());
    }
}