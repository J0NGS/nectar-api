package br.com.nectar.domain.job;

public enum JobsStatus {
    CONCLUDED("Concluído"),
    IN_PROGRESS("Em andamento"),
    CANCELED("Cancelado");

    private final String description;

    JobsStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
