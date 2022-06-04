package com.lauratesan.jwt.integration;

import com.google.gson.Gson;

import com.lauratesan.jwt.dto.JwtResponse;
import com.lauratesan.jwt.model.auth.ERole;
import com.lauratesan.jwt.model.auth.Role;
import com.lauratesan.jwt.model.auth.User;
import com.lauratesan.jwt.repository.auth.RoleRepository;
import com.lauratesan.jwt.repository.auth.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {
    private static final String EMAIL = "maytest@test.com";
    private static final String CORRECT_PASSWORD = "maycraftscodes";
    private static final String INCORRECT_PASSWORD = "incorrectpassword1";

    @Autowired
    protected MockMvc mockMvc;
    protected Gson gson = new Gson();
    protected User user;
    protected String token;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected abstract void afterEach();

    protected abstract void beforeEach();

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        afterEach();
    }

    @BeforeEach
    void setup() throws Exception {
        user = createUserAccount();

        String loginRequestAsJson = "{\n" +
                "    \"email\": \"" + EMAIL + "\",\n" +
                "    \"password\": \"" + CORRECT_PASSWORD + "\"\n" +
                "}";
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestAsJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = gson.fromJson(response, JwtResponse.class);
        token = jwtResponse.getToken();
    }

    private User createUserAccount() {
        Role userRole = new Role();
        userRole.setName(ERole.USER);
        roleRepository.save(userRole);


        Role adminRole = new Role();
        adminRole.setName(ERole.ADMIN);
        roleRepository.save(adminRole);

        Set<Role> roles = new HashSet<>();
        Role userRoleDb = roleRepository.
                findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRoleDb);

        Role adminRoleDb = roleRepository.
                findByName(ERole.ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        roles.add(userRoleDb);
        roles.add(adminRoleDb);

        User user = new User(EMAIL, EMAIL,
                passwordEncoder.encode(CORRECT_PASSWORD),
                "May",
                "Sanejo",
                roles,
                "fdacad9e-b98c-4b16-bede-280e1e6a446c");
        return userRepository.save(user);
    }
}
