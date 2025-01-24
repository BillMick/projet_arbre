package model;

public enum NotificationType {
    PLANTATION("Notification de plantation"),
    CLASSIFICATION("Notification de classification"),
    ABATTAGE("Notification d'abattage");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
