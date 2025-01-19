
import java.util.Date;

enum TypeNotification {
    PLANTATION,
    ABATTAGE,
    CLASSIFICATION,
    PROPOSITION_DE_CLASSIFICATION
}

public record Notification(TypeNotification type, Entite emetteur, Entite recepteur, String message, Date date) {
    public Notification {
        if(emetteur.equals(recepteur)) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous envoyez une notification.");
        }
    }
}
