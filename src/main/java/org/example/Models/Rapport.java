package org.example.Models;
import java.util.Date;

enum Appreciation {
    PASBON,
    BON,
    TRESBON
}

public record Rapport (Appreciation appreciation, String message, Date dateAjout) {
    // definir un toString() ...
}