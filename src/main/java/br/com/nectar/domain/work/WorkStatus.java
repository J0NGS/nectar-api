package br.com.nectar.domain.work;

public enum WorkStatus {
    CONCLUDED("Concluído"),
    IN_PROGRESS("Em andamento"),
    CANCELED("Cancelado");

    private final String description;

    WorkStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
