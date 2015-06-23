package urujdas.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import urujdas.config.WebConfig;
import urujdas.model.users.User;
import urujdas.service.UserService;
import urujdas.service.exception.UserAlreadyExistsException;
import urujdas.web.common.CommonExceptionHandler;
import urujdas.web.common.WebCommons;
import urujdas.web.user.RegistrationController;
import urujdas.web.user.model.RegisterUserRequest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(classes = {
        WebConfig.class,
        RegistrationControllerTest.LocalContext.class
})
public class RegistrationControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @AfterMethod
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(userService);
        reset(userService);
    }

    @Test
    public void register_hp() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .withUsername("username")
                .withPassword("password")
                .build();

        String requestInJson = toJson(registerUserRequest);

        mockMvc.perform(post(WebCommons.VERSION_PREFIX + "/register")
                .content(requestInJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        User expectedUser = User.builder()
                .withUsername(registerUserRequest.getUsername())
                .withPassword(registerUserRequest.getPassword())
                .build();

        verify(userService).register(eq(expectedUser));
    }

    @Test
    public void register_notValid() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .withUsername("username")
                .build();

        String requestInJson = toJson(registerUserRequest);

        mockMvc.perform(post(WebCommons.VERSION_PREFIX + "/register")
                .content(requestInJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void register_alreadyRegistered() throws Exception{
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .withUsername("username")
                .withPassword("password")
                .build();

        String requestInJson = toJson(registerUserRequest);

        doThrow(new UserAlreadyExistsException()).when(userService).register(any(User.class));

        mockMvc.perform(post(WebCommons.VERSION_PREFIX + "/register")
                .content(requestInJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorType").value("USER_ALREADY_EXISTS"));

        verify(userService).register(any(User.class));
    }

    private String toJson(Object value) throws Exception{
        return objectMapper.writeValueAsString(value);
    }

    @Configuration
    static class LocalContext {
        @Bean
        public RegistrationController registrationController() {
            return new RegistrationController();
        }

        @Bean
        public CommonExceptionHandler commonExceptionHandler() {
            return new CommonExceptionHandler();
        }

        @Bean
        public MockMvc mockMvc(WebApplicationContext ctx) {
            return MockMvcBuilders.webAppContextSetup(ctx)
                    .build();
        }

        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

}
