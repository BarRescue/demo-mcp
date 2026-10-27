package nl.expertlycoded.cursusmcp;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Cursus(
        String id,
        String titel,
        String categorie,
        Niveau niveau,
        String docent,
        int duurInDagen,
        BigDecimal prijsExclBtw,
        LocalDate startdatum,
        String locatie,
        int beschikbarePlekken,
        String omschrijving) {

    public enum Niveau { BEGINNER, GEVORDERD, EXPERT }
}
