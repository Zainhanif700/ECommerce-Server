package com.zain.controller;

import com.zain.Request.LoginUserDto;
import com.zain.Request.RegisterUserDto;
import com.zain.Request.VerifyUserDto;
import com.zain.Response.LoginResponse;
import com.zain.exception.UserException;
import com.zain.model.User;
import com.zain.repository.UserRepository;
import com.zain.service.AuthenticationService;
import com.zain.service.CartService;
import com.zain.service.CustomUserServiceImplementation;
import com.zain.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;
    private JwtService jwtProvider;
    private PasswordEncoder passwordEncoder;
    private CustomUserServiceImplementation customUserServiceImplementation;
    private CartService cartService;

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(CartService cartService, UserRepository userRepository, CustomUserServiceImplementation customUserServiceImplementation, PasswordEncoder passwordEncoder, JwtService jwtProvider, JwtService jwtService, AuthenticationService authenticationService){
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customUserServiceImplementation = customUserServiceImplementation;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<User> createUserHandler(@RequestBody RegisterUserDto user) throws UserException {

        User registeredUser = authenticationService.signup(user);
        cartService.createCart(registeredUser);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> loginUserHandler(@RequestBody LoginUserDto loginRequest) throws UserException {
        User authenticatedUser = authenticationService.authenticate(loginRequest);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
        if (userDetails == null) {
            throw  new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
