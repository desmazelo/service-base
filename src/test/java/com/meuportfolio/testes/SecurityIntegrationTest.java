package com.meuportfolio.testes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meuportfolio.domain.UserRole;
import com.meuportfolio.dtos.AuthenticationRequest;
import com.meuportfolio.dtos.RegisterRequest;
import com.meuportfolio.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SecurityIntegrationTest {

    @Autowired private  MockMvc mockMvc;
    @Autowired private  ObjectMapper objectMapper;
    @Autowired private  UserRepository userRepository;

    private String adminUsername = "admin";
    private String adminPassword = "admin123";
    private String PROTECTED_URL = "/test/protected"; // URL que criaremos para teste

    /**
     * Endpoint de teste simples.
     * Precisamos de um endpoint protegido para testar o SecurityFilter.
     * Você deve criar este Controller no código de produção para o teste funcionar.
     */
    // Crie este controller no seu pacote de controllers (com.meuportfolio.controllers)
    // @RestController
    // @RequestMapping("/test")
    // public class TestController {
    //     @GetMapping("/protected")
    //     public ResponseEntity<String> protectedEndpoint() {
    //         return ResponseEntity.ok("Acesso Autorizado!");
    //     }
    // }

    // --- TESTES DE AUTENTICAÇÃO (LOGIN) ---

    @Test
    void shouldAuthenticateAndReturnToken() throws Exception {
        // Objeto DTO de requisição de autenticação
        AuthenticationRequest request = new AuthenticationRequest(adminUsername, adminPassword);

        // Simula a chamada POST para /auth/login
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Espera status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty()); // Espera um token JWT
    }

    @Test
    void shouldReturn403WhenCredentialsAreInvalid() throws Exception {
        // Senha propositalmente errada
        AuthenticationRequest request = new AuthenticationRequest(adminUsername, "senhaErrada123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // O Spring Security retorna 403 Forbidden para BadCredentialsException
                .andExpect(status().isForbidden());
    }

    // --- TESTES DE AUTORIZAÇÃO (JWT FILTER) ---

    @Test
    void shouldReturn403WhenAccessingProtectedWithoutToken() throws Exception {
        // Tenta acessar a URL protegida sem nenhum token
        mockMvc.perform(get(PROTECTED_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                // Espera 403 Forbidden (ou 401 Unauthorized, dependendo da configuração exata do Filter)
                .andExpect(status().isForbidden());
    }

    // O teste para acesso com token requer que o token real seja extraído do teste de login
    // e reutilizado, o que é mais complexo. Por enquanto, focaremos nos testes de Login e Acesso Negado.

    // --- Próxima Fase: Testar autorização por Role ---

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldAccessProtectedEndpointWithMockedUser() throws Exception {
        // Usa a anotação @WithMockUser para simular um usuário autenticado
        mockMvc.perform(get(PROTECTED_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shoulRegisterNewUserSucessfully() throws Exception {
        RegisterRequest newUserRequest = new RegisterRequest(
                "novo_usuario_test",
                "senhaForte123",
                "novo.user@teste.com",
                UserRole.USER
        );
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("novo_usuario_test"));

        Assertions.assertNotNull(userRepository.findByUsername("novo_usuario_test"),"O usuario deve ser salvo no banco após o registro");
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringExistingUsername() throws Exception{
        RegisterRequest existingUserRequest = new RegisterRequest(
                adminUsername, // "admin"
                "qualquerSenha",
                "admin_repetido@teste.com",
                UserRole.USER
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUserRequest)))

                .andExpect(status().isBadRequest());
    }

}