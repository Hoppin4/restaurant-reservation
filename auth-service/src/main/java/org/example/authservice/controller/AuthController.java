package org.example.authservice.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.example.authservice.dto.*;
import org.example.authservice.entity.User;
import org.example.authservice.service.auth.AuthService;
import org.example.authservice.service.auth.JwtService;
import org.example.authservice.service.auth.RefreshTokenService;
import org.example.authservice.service.auth.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtUtils;

    public AuthController(UserService userService,
                          AuthService authService,
                          RefreshTokenService refreshTokenService,
                          JwtService jwtUtils) {
        this.userService = userService;
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            User user = authService.login(authRequest.getEmail(), authRequest.getPassword());
            String accessToken = jwtUtils.generateAccessToken(user);
            String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(Duration.ofDays(30))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(new AuthResponse(accessToken, "bearer",user.getEmail(),user.getFullName()));
        } catch (ResponseStatusException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            throw ex;
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    User user = token.getUser();

                    if (user == null) {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");}
                    refreshTokenService.deleteByUserId(token.getUser().getId().toString());

                    String newAccessToken = jwtUtils.generateAccessToken(token.getUser());
                    String newRefreshToken = refreshTokenService.createRefreshToken(token.getUser()).getToken();

                    ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .sameSite("Lax")
                            .maxAge(Duration.ofDays(30))
                            .build();

                    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                    return ResponseEntity.ok(new AuthResponse(newAccessToken, "bearer",user.getEmail(),user.getFullName()));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean logout( @CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            refreshTokenService.deleteByRefreshToken(refreshToken);
        }else{
            return false;
        }

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return true;
    }
    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestBody VerifyRequest request) {
        userService.verifyEmail(request.getToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password/request")
    public ResponseEntity<Void> requestPasswordChange(@RequestBody ChangePasswordRequest request) {
        userService.requestPasswordChange(request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-password/confirm")
    public ResponseEntity<Void> confirmPasswordChange(@RequestBody ConfirmPasswordRequest request) {
        userService.confirmPasswordChange(request.getToken());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody ForgotPasswordResetRequest request) {
        userService.resetPassword(request.getEmail(), request.getToken(),request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}

