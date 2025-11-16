package com.example.testddd.home;

import com.example.testddd.portal.PortalTileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PortalTileService portalTileService;

    public HomeController(PortalTileService portalTileService) {
        this.portalTileService = portalTileService;
    }

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        model.addAttribute("portalTiles", portalTileService.listEnabledTiles());
        return "index";
    }

    @GetMapping("/bitacora")
    public String bitacora() {
        // Usamos la misma lógica de secciones dinámicas para la bitácora
        return "forward:/seccion/bitacora";
    }
}
