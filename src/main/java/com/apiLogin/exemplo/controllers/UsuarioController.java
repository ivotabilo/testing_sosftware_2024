package com.apiLogin.exemplo.controllers;

import com.apiLogin.exemplo.model.entities.Usuario;
import com.apiLogin.exemplo.model.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@RestController
public class UsuarioController {


    @Autowired
    UsuarioRepository usuarioRepository;
    /* 
      @GetMapping("/login")
    public String loginPage() {
        return "redirect:/loginForm.html"; // Redirige a la página estática
    }
    */
   
    

    @PostMapping(value = "/register")// Considera agregar los encabezados CORS a ambos métodos
    public ResponseEntity<Usuario> registerUsuario(@RequestBody Usuario usuarioRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        try {
            // Verifica si el usuario ya existe
            if (usuarioRepository.findByEmailOrUsername(usuarioRequest.getEmail(), usuarioRequest.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).headers(headers).build(); // Conflicto si el usuario ya existe
            }

            // Guarda el nuevo usuario
            Usuario newUser = usuarioRepository.save(usuarioRequest);
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(newUser); // Devuelve el nuevo usuario con 201 Created

        } catch (Exception e) {
            e.printStackTrace(); // Log del error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).build(); // Manejo de errores
        }
    }
    @PostMapping(value = "/login")
    public ResponseEntity loginUsuario(@RequestBody Usuario usuarioRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
    
        try {
            // Buscar el usuario por nombre de usuario o correo electrónico
            Optional<Usuario> usuario = usuarioRepository.findByEmailOrUsername(usuarioRequest.getUsername(), usuarioRequest.getUsername());
    
            if (usuario.isPresent()) {
                // Comparar la contraseña
                if (usuario.get().getSenha().equals(usuarioRequest.getSenha())) {
                    return ResponseEntity.ok().headers(headers).build(); // Autenticación exitosa
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build(); // Contraseña incorrecta
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).build(); // Usuario no encontrado
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log del error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).build(); // Manejo de errores
        }
    }
    
    

}
