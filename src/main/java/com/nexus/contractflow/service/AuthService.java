package com.nexus.contractflow.service;

import com.nexus.contractflow.dto.request.LoginRequest;
import com.nexus.contractflow.dto.request.RegisterRequest;
import com.nexus.contractflow.dto.response.AuthResponse;
import com.nexus.contractflow.entity.Usuario;
import com.nexus.contractflow.entity.enums.Role;
import com.nexus.contractflow.exception.RecursoDuplicadoException;
import com.nexus.contractflow.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RecursoDuplicadoException(
                    "Já existe um usuário cadastrado com o e-mail " + request.getEmail() + ".");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(request.getRole() == null ? Role.USER : request.getRole())
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Novo usuário registrado: email={}, role={}", salvo.getEmail(), salvo.getRole());

        return montarResposta(salvo);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow();
        log.info("Login bem-sucedido para: {}", usuario.getEmail());
        return montarResposta(usuario);
    }

    private AuthResponse montarResposta(Usuario usuario) {
        String token = jwtService.gerarToken(usuario);
        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .expiresAt(jwtService.getExpiracao())
                .build();
    }
}
