package nl.expertlycoded.cursusmcp;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpTool.McpAnnotations;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import nl.expertlycoded.cursusmcp.Cursus.Niveau;

/**
 * De MCP-tools die Claude kan aanroepen. Spring AI genereert het JSON-schema
 * automatisch uit de methodeparameters; de descriptions helpen Claude kiezen
 * wanneer welke tool nodig is.
 */
@Component
public class CursusTools {

    private final CursusRepository repository;

    public CursusTools(CursusRepository repository) {
        this.repository = repository;
    }

    @McpTool(name = "zoek_cursussen",
            description = """
                    Zoekt in het cursusaanbod. Alle filters zijn optioneel; zonder filters \
                    komen alle cursussen terug. Bevat prijs, startdatum, locatie, docent \
                    en het aantal beschikbare plekken.""",
            annotations = @McpAnnotations(readOnlyHint = true, destructiveHint = false, idempotentHint = true))
    public List<Cursus> zoekCursussen(
            @McpToolParam(required = false, description = "Categorie, bijv. 'AI', 'Programmeren', 'DevOps', 'Webdevelopment'")
            @Nullable String categorie,
            @McpToolParam(required = false, description = "Niveau: BEGINNER, GEVORDERD of EXPERT")
            @Nullable Niveau niveau,
            @McpToolParam(required = false, description = "Vrije zoekterm op titel, omschrijving of docent")
            @Nullable String zoekterm) {
        return repository.zoek(blankToNull(categorie), niveau, blankToNull(zoekterm));
    }

    @McpTool(name = "cursus_details",
            description = "Geeft alle details van één cursus op basis van de cursuscode, bijv. 'SPRING-201'.",
            annotations = @McpAnnotations(readOnlyHint = true, destructiveHint = false, idempotentHint = true))
    public Cursus cursusDetails(@McpToolParam(description = "De cursuscode, bijv. 'AI-301'") String cursusId) {
        return repository.vindOpId(cursusId)
                .orElseThrow(() -> new IllegalArgumentException("Geen cursus gevonden met code " + cursusId));
    }

    @McpTool(name = "cursus_categorieen",
            description = "Geeft de lijst met beschikbare cursuscategorieën.",
            annotations = @McpAnnotations(readOnlyHint = true, destructiveHint = false, idempotentHint = true))
    public List<String> categorieen() {
        return repository.categorieen();
    }

    private static @Nullable String blankToNull(@Nullable String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
