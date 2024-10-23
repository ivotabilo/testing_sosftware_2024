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
public class UsuarioControllerTestIntegracion {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        usuarioRepository.deleteAll(); // Limpiar la base de datos antes de cada prueba
    }

    @Test
    /*Se registra un nuevo usuario mediante una solicitud POST a /register.
    Se verifica que el usuario se haya creado correctamente con un estado 201 Created.
    Luego, se intenta iniciar sesión con el usuario creado mediante otra solicitud POST a /login.
    Se espera que la respuesta sea un estado 200 OK, indicando que la autenticación fue exitosa.
    esto es lo q hace */
    public void testRegisterAndLoginIntegration() throws Exception {
        // Registro de un nuevo usuario
        String usuarioJson = "{\"username\":\"integrationUser\", \"senha\":\"integrationPassword\", \"email\":\"integration@example.com\"}";

        // Realizar el registro
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated());

        // Intentar iniciar sesión con el usuario creado
        String loginJson = "{\"username\":\"integrationUser\", \"senha\":\"integrationPassword\"}";

        // Realizar el login
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk());
    }

    @Test
    /*Registra un usuario y luego intenta registrarlo nuevamente.
    Se espera que la segunda llamada devuelva un estado 409 Conflict, indicando que el usuario ya existe. */
    public void testRegisterDuplicateUser() throws Exception {
        // Registro de un nuevo usuario
        String usuarioJson = "{\"username\":\"duplicateUser\", \"senha\":\"password\", \"email\":\"duplicate@example.com\"}";

        // Realizar el registro
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated());

        // Intentar registrar el mismo usuario nuevamente
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isConflict()); // Debería devolver 409 Conflict
    }

    @Test
    /*Intenta iniciar sesión con un nombre de usuario que no ha sido registrado.
    Se espera que la respuesta sea un estado 404 Not Found, ya que el usuario no existe en la base de datos. */
    public void testLoginWithUnregisteredUser() throws Exception {
        // Intentar iniciar sesión con un usuario no registrado
        String loginJson = "{\"username\":\"nonExistentUser\", \"senha\":\"somePassword\"}";

        // Realizar el login
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isNotFound()); // Debería devolver 404 Not Found
    }
}

