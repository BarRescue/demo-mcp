package nl.expertlycoded.cursusmcp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import nl.expertlycoded.cursusmcp.Cursus.Niveau;

/**
 * Hardcoded cursusdata voor de demo. Geen database nodig.
 */
@Repository
public class CursusRepository {

    private static final List<Cursus> CURSUSSEN = List.of(
            new Cursus("JAVA-101", "Java Fundamentals", "Programmeren", Niveau.BEGINNER,
                    "Sanne de Vries", 3, new BigDecimal("1195.00"), LocalDate.of(2026, 11, 16),
                    "Utrecht", 8,
                    "Leer de basis van Java: syntax, OOP, collections en records."),
            new Cursus("SPRING-201", "Spring Boot 4 in de praktijk", "Programmeren", Niveau.GEVORDERD,
                    "Rens Manders", 2, new BigDecimal("1495.00"), LocalDate.of(2026, 11, 24),
                    "Den Bosch", 4,
                    "Bouw productierijpe REST API's met Spring Boot 4, observability en testen."),
            new Cursus("AI-301", "AI-agents bouwen met MCP", "AI", Niveau.EXPERT,
                    "Rens Manders", 1, new BigDecimal("895.00"), LocalDate.of(2026, 12, 3),
                    "Online", 12,
                    "Koppel je eigen systemen aan LLM's via het Model Context Protocol."),
            new Cursus("AI-101", "Prompt engineering voor professionals", "AI", Niveau.BEGINNER,
                    "Lotte Jansen", 1, new BigDecimal("495.00"), LocalDate.of(2026, 11, 20),
                    "Online", 20,
                    "Haal meer uit Claude en andere LLM's met gestructureerde prompts."),
            new Cursus("DEVOPS-201", "Docker & Kubernetes", "DevOps", Niveau.GEVORDERD,
                    "Mehmet Yilmaz", 2, new BigDecimal("1295.00"), LocalDate.of(2026, 12, 8),
                    "Amsterdam", 0,
                    "Containeriseer applicaties en deploy ze op Kubernetes."),
            new Cursus("WEB-101", "Moderne webdevelopment met Next.js", "Webdevelopment", Niveau.BEGINNER,
                    "Sanne de Vries", 3, new BigDecimal("1095.00"), LocalDate.of(2027, 1, 12),
                    "Eindhoven", 10,
                    "Van React-basis tot een gedeployde Next.js-applicatie."));

    public List<Cursus> zoek(@Nullable String categorie, @Nullable Niveau niveau, @Nullable String zoekterm) {
        return CURSUSSEN.stream()
                .filter(c -> categorie == null || c.categorie().equalsIgnoreCase(categorie))
                .filter(c -> niveau == null || c.niveau() == niveau)
                .filter(c -> zoekterm == null || bevat(c, zoekterm))
                .toList();
    }

    public Optional<Cursus> vindOpId(String id) {
        return CURSUSSEN.stream().filter(c -> c.id().equalsIgnoreCase(id)).findFirst();
    }

    public List<String> categorieen() {
        return CURSUSSEN.stream().map(Cursus::categorie).distinct().sorted().toList();
    }

    private static boolean bevat(Cursus c, String zoekterm) {
        var term = zoekterm.toLowerCase(Locale.ROOT);
        return (c.titel() + " " + c.omschrijving() + " " + c.docent()).toLowerCase(Locale.ROOT).contains(term);
    }
}
