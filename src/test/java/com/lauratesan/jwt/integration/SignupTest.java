//package com.lauratesan.jwt.integration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lauratesan.jwt.dto.LoginRequest;
//import com.lauratesan.jwt.dto.SignupRequest;
//import com.lauratesan.jwt.exceptions.EmailAlreadyExistException;
//import com.lauratesan.jwt.exceptions.EmailConfirmException;
//import com.lauratesan.jwt.exceptions.PasswordConfirmException;
//import com.lauratesan.jwt.exceptions.UserNotFoundException;
//import com.lauratesan.jwt.mapper.UserMapper;
//import com.lauratesan.jwt.repository.auth.RoleRepository;
//import com.lauratesan.jwt.repository.auth.UserRepository;
//import com.lauratesan.jwt.security.jwt.AuthEntryPointJwt;
//import com.lauratesan.jwt.security.jwt.JwtUtils;
//import com.lauratesan.jwt.service.auth.AuthService;
//import com.lauratesan.jwt.service.auth.UserDetailsServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class SignupTest {
//
//    private static final String EMAIL2 = "nonexistentuser@gmail.com";
//    private static final String CORRECT_PASSWORD = "maycraftscodes";
//    private static final String INCORRECT_CONFIRM_PASSWORD = "incorrectpassword";
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private RoleRepository roleRepository;
//
//    @MockBean
//    private AuthService authService;
//
//    @MockBean
//    private UserMapper userMapper;
//
//    @MockBean
//    private UserDetailsServiceImpl userDetailsService;
//
//    @MockBean
//    private AuthEntryPointJwt authEntryPointJwt;
//
//    @MockBean
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @Test
//    public void when_valid_user_logsIn_should_return200_response() throws Exception {
//        LoginRequest mockLogin = new LoginRequest(EMAIL2, CORRECT_PASSWORD);
//        String json = objectMapper.writeValueAsString(mockLogin);
//        mockMvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isOk());
//    }
//    @Test
//    public void when_empty_password_logsIn_should_return_badCredentailsException() throws Exception {
//        LoginRequest mockLogin = new LoginRequest(EMAIL2, "");
//        String json = objectMapper.writeValueAsString(mockLogin);
//        when(authService.authenticateUser(mockLogin)).thenThrow(new BadCredentialsException("Invalid Password!"));
//        mockMvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Invalid email or password!"))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
//    }
//    @Test
//    public void when_empty_email_logsIn_should_return_badCredentailsException() throws Exception {
//        LoginRequest mockLogin = new LoginRequest("", CORRECT_PASSWORD);
//        String json = objectMapper.writeValueAsString(mockLogin);
//        when(authService.authenticateUser(mockLogin)).thenThrow(new BadCredentialsException("Invalid Email!"));
//        mockMvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Invalid email or password!"))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
//    }
//    @Test
//    public void when_valid_login_request_Is_made_but_user_not_found_then_should_throw_userNotFoundException() throws Exception {
//        LoginRequest mockLogin = new LoginRequest(EMAIL2, CORRECT_PASSWORD);
//        String json = objectMapper.writeValueAsString(mockLogin);
//        when(authService.authenticateUser(mockLogin)).thenThrow(new UserNotFoundException("User not found!"));
//        mockMvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("User not found!"))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
//    }
//
//    @Test
//    void should_return_error_when_signup_failed_given_email_already_exist() throws Exception {
//        SignupRequest mockSignUpRequest = new SignupRequest(EMAIL2, EMAIL2, CORRECT_PASSWORD, CORRECT_PASSWORD, "Mock", "User");
//        String json = objectMapper.writeValueAsString(mockSignUpRequest);
//        when(authService.registerUser(mockSignUpRequest)).thenThrow(new EmailAlreadyExistException("Email already exist! Please use another email."));
//        mockMvc.perform(post("/api/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Email already exist! Please use another email."))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
//    }
//    @Test
//    public void whenEmailsDoNotMatchShouldReturnEmailConfirmException() throws Exception {
//        SignupRequest mockSignUpRequest = new SignupRequest(EMAIL2, EMAIL2, CORRECT_PASSWORD, CORRECT_PASSWORD, "Mock", "User");
//        String json = objectMapper.writeValueAsString(mockSignUpRequest);
//        when(authService.registerUser(mockSignUpRequest)).thenThrow(new EmailConfirmException("Please retype your email."));
//        mockMvc.perform(post("/api/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Please retype your email."))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
//    }
//    @Test
//    void should_return_error_when_signup_failed_given_incorrect_confirm_password() throws Exception {
//        SignupRequest mockSignUpRequest = new SignupRequest(EMAIL2, EMAIL2, CORRECT_PASSWORD, INCORRECT_CONFIRM_PASSWORD, "Mock", "User");
//        String json = objectMapper.writeValueAsString(mockSignUpRequest);
//        when(authService.registerUser(mockSignUpRequest)).thenThrow(new PasswordConfirmException("Please retype your password!"));
//        mockMvc.perform(post("/api/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Please retype your password!"))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
//    }
//
//    @Test
//    @WithMockUser(roles = {"USER"})
//    public void whenUserRequestsJwtInfoShouldReturn200OK() throws Exception {
//        mockMvc.perform(get("/api/auth/getJwtInfo"))
//                .andExpect(status().isOk());
//    }
//    @Test
//    @WithMockUser(roles = {"USER"})
//    public void whenUserRequestsUserInfoShouldReturn200OK() throws Exception {
//        mockMvc.perform(get("/api/auth/getUserInfo"))
//                .andExpect(status().isOk());
//    }
//
//}
