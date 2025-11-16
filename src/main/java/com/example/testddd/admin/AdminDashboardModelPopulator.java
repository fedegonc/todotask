package com.example.testddd.admin;

import com.example.testddd.card.CardRepository;
import com.example.testddd.portal.PortalTileService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class AdminDashboardModelPopulator {

    private final CardRepository cardRepository;
    private final PortalTileService portalTileService;

    public AdminDashboardModelPopulator(CardRepository cardRepository,
                                        PortalTileService portalTileService) {
        this.cardRepository = cardRepository;
        this.portalTileService = portalTileService;
    }

    public void populate(Model model) {
        model.addAttribute("cards", cardRepository.findAll(Sort.by("title")));
        model.addAttribute("portalTiles", portalTileService.listAllTiles());
    }
}
