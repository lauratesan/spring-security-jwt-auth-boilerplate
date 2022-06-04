package com.lauratesan.jwt.service.auth;

import com.lauratesan.jwt.dto.JwtResponse;
import com.lauratesan.jwt.dto.LoginRequest;
import com.lauratesan.jwt.dto.MessageResponse;
import com.lauratesan.jwt.dto.SignupRequest;
import com.lauratesan.jwt.exceptions.EmailAlreadyExistException;
import com.lauratesan.jwt.exceptions.EmailConfirmException;
import com.lauratesan.jwt.exceptions.PasswordConfirmException;
import com.lauratesan.jwt.exceptions.UserNotFoundException;
import com.lauratesan.jwt.model.auth.ERole;
import com.lauratesan.jwt.model.auth.Role;
import com.lauratesan.jwt.model.auth.User;
import com.lauratesan.jwt.repository.auth.RoleRepository;
import com.lauratesan.jwt.repository.auth.UserRepository;
import com.lauratesan.jwt.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;



    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return getJwtResponse(authentication);
    }

    private ResponseEntity<JwtResponse> getJwtResponse(Authentication authentication) {
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    public ResponseEntity<JwtResponse> getJwtResponse() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getJwtResponse(authentication);
    }

    public ResponseEntity<MessageResponse> registerUser(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        UUID uuid = UUID.randomUUID();

        User user = new User(signupRequest.getEmail(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()),
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                roles,
                uuid.toString());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private void validateSignupRequest(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistException("Email already exist! Please use another email.");
        }

        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new PasswordConfirmException("Please retype your password!");
        }

        if (!signupRequest.getEmail().equals(signupRequest.getConfirmEmail())) {
            throw new EmailConfirmException("Please retype your email!");
        }
    }

    public User getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User could not be found!"));
    }
}
