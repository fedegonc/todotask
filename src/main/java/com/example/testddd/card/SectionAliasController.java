package com.example.testddd.card;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Alias simple para secciones dinámicas.
 *
 * Permite que URLs como /asd o /def actúen como alias de /seccion/asd y /seccion/def,
 * sin interferir con rutas conocidas (admin, servicios, bitacora, seccion, recursos estáticos, etc.).
 */
@Controller
public class SectionAliasController {

    @GetMapping("/{slug:^(?!admin$|servicios$|bitacora$|seccion$|css$|js$|images$|webjars$|h2-console$).+}")
    public String aliasToSection(@PathVariable String slug) {
        // Hacemos forward interno a /seccion/{slug} para reutilizar la lógica existente
        return "forward:/seccion/" + slug;
    }
}
