package br.com.nectar.domain.job;

public enum ProductType {
    HONEY("Mel"),
    POLLEN("Pólen"),
    PROPOLIS("Própolis"),
    WAX("Cera");

    private final String description;

    ProductType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
