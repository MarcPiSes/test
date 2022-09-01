package cat.udg.tfg.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping("/exists")
    public ResponseEntity<Object> exists() throws Exception {
        return ResponseEntity.ok().build();
    }
}
