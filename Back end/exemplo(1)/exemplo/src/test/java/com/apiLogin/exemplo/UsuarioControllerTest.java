package com.apiLogin.exemplo;
import com.apiLogin.exemplo.model.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        usuarioRepository.deleteAll(); // Limpiar la base de datos antes de cada prueba
    }

    @Test
    public void testRegisterAndLogin() throws Exception {
        // Registro de un nuevo usuario
        String usuarioJson = "{\"username\":\"testuser\", \"senha\":\"password\", \"email\":\"test@example.com\"}";

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated());

        // Intentar iniciar sesión con el usuario creado
        String loginJson = "{\"username\":\"testuser\", \"senha\":\"password\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginWithIncorrectPassword() throws Exception {
        // Registro de un nuevo usuario
        String usuarioJson = "{\"username\":\"testuser\", \"senha\":\"password\", \"email\":\"test@example.com\"}";

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated());

        // Intentar iniciar sesión con una contraseña incorrecta
        String loginJson = "{\"username\":\"testuser\", \"senha\":\"wrongpassword\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }
}




