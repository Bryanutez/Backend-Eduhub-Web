package com.eduhubpro.eduhubpro.Security.Auth.Controller;

import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserDto;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserRepository;
import com.eduhubpro.eduhubpro.Security.Auth.Model.AuthRequest;
import com.eduhubpro.eduhubpro.Security.Auth.Model.AuthResponse;
import com.eduhubpro.eduhubpro.Security.Jwt.JwtUtil;
import com.eduhubpro.eduhubpro.Security.Jwt.UserDetailsServiceImpl;
import com.eduhubpro.eduhubpro.Util.Enum.TypesResponse;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*@PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        Optional<Usuario> userOptional = userRepository.findByCorreo(authRequest.getEmail());
        if (userOptional.isEmpty()) {
            return new AuthResponse("", 0L, "Usuario no encontrado", 0, Rol.STUDENT);
        }

        Usuario user = userOptional.get();
        if (user.getEstado() != Estado.ACTIVE) {
            return new AuthResponse("", 0L, "El usuario está inactivo", 0, Rol.STUDENT);
        }

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getContrasena())) {
            return new AuthResponse("", 0L, "Correo o contraseña incorrectos", 0, Rol.STUDENT);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails, user.getUsuarioId());

        if (authService.isTokenInvalid(jwt)) {
            return new AuthResponse("", 0L, "Sesión expirada", 0, Rol.STUDENT);
        }

        long expirationTime = jwtUtil.getExpirationTime();

        return new AuthResponse(jwt, user.getUsuarioId(), user.getCorreo(), expirationTime, user.getRol());
    }*/

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Usuario o contraseña incorrectos", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new Exception("Usuario o contraseña incorrectos");
        }

        final String jwt = jwtUtil.generateToken(userDetails, user.getUserId().toString());

        long expirationTime = jwtUtil.getExpirationTime();

        return new AuthResponse(jwt, user.getEmail(), expirationTime, user.getRole());
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Message> logout(@Validated(UserDto.Consult.class) @RequestBody UserDto dto) {
        if (jwtUtil.isTokenInvalid(dto.getUserId())) {
            return new ResponseEntity<>(new Message("Sesión inválida", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        jwtUtil.invalidateToken(dto.getUserId());

        return new ResponseEntity<>(new Message("Cierre de sesión exitoso", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}