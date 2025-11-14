package com.example.testddd;

import com.example.testddd.card.Card;
import com.example.testddd.card.CardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedCards(CardRepository cardRepository) {
        return args -> {
            if (cardRepository.count() == 0) {
                List<Card> defaults = List.of(
                        new Card("Tallados en madera", "Detalles artesanales en piezas únicas trabajadas a mano.", "tallados"),
                        new Card("Cartelería en lona", "Impresiones resistentes para comunicar en interior y exterior.", "carteleria-lona"),
                        new Card("Murales", "Intervenciones artísticas que transforman cualquier espacio.", "murales"),
                        new Card("Letras corpóreas", "Volumen y presencia para resaltar marcas y mensajes.", "letras-corporeas"),
                        new Card("Diseño de logos", "Identidades visuales pensadas para conectar con tu público.", "diseno-logos"),
                        new Card("Rotulación vehicular", "Aplicaciones adhesivas que convierten vehículos en piezas de comunicación.", "rotulacion-vehicular"),
                        new Card("Señalética interior", "Sistemas de orientación claros y estéticos para cualquier tipo de edificio.", "senaletica-interior"),
                        new Card("Stands promocionales", "Estructuras desmontables para destacar marcas en eventos y ferias.", "stands-promocionales"),
                        new Card("Impresión en vinilo", "Gráficas personalizadas con acabados mate o brillantes de alta calidad.", "impresion-vinilo"),
                        new Card("Decoración de vidrieras", "Composiciones visuales que capturan la atención y generan impacto.", "decoracion-vidrieras"),
                        new Card("Páginas web", "Sitios modernos y responsivos para presentar servicios y captar clientes.", "paginas-web"),
                        new Card("Asistencia de redes sociales", "Gestión integral de contenidos y campañas para crecer en plataformas digitales.", "asistencia-redes")
                );
                defaults.forEach(cardRepository::save);
            }
        };
    }
}
