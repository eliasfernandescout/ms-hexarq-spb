package com.kraftbrains.mshexarqspb.domain.core;

import java.util.Objects;

/**
 * Objeto de valor que representa um endereço de entrega
 */
public class DeliveryAddress {
    private final String street;
    private final String number;
    private final String complement;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String reference;

    private DeliveryAddress(Builder builder) {
        // Validações de domínio
        if (builder.street == null || builder.street.trim().isEmpty()) {
            throw new IllegalArgumentException("Rua não pode ser vazia");
        }
        if (builder.number == null || builder.number.trim().isEmpty()) {
            throw new IllegalArgumentException("Número não pode ser vazio");
        }
        if (builder.neighborhood == null || builder.neighborhood.trim().isEmpty()) {
            throw new IllegalArgumentException("Bairro não pode ser vazio");
        }
        if (builder.city == null || builder.city.trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade não pode ser vazia");
        }
        if (builder.state == null || builder.state.trim().isEmpty()) {
            throw new IllegalArgumentException("Estado não pode ser vazio");
        }
        if (builder.zipCode == null || builder.zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }

        // Validar formato do CEP (apenas números, 8 dígitos)
        String cleanZipCode = builder.zipCode.replaceAll("\\D", "");
        if (cleanZipCode.length() != 8) {
            throw new IllegalArgumentException("CEP deve ter 8 dígitos");
        }

        this.street = builder.street;
        this.number = builder.number;
        this.complement = builder.complement;
        this.neighborhood = builder.neighborhood;
        this.city = builder.city;
        this.state = builder.state;
        this.zipCode = cleanZipCode;
        this.reference = builder.reference;
    }

    // Métodos de domínio
    public String getFormattedZipCode() {
        return zipCode.substring(0, 5) + "-" + zipCode.substring(5);
    }

    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(street).append(", ").append(number);

        if (complement != null && !complement.isEmpty()) {
            address.append(" - ").append(complement);
        }

        address.append(", ").append(neighborhood)
              .append(", ").append(city)
              .append(" - ").append(state)
              .append(", ").append(getFormattedZipCode());

        if (reference != null && !reference.isEmpty()) {
            address.append(" (").append(reference).append(")");
        }

        return address.toString();
    }

    // Getters
    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getReference() {
        return reference;
    }

    // Value Object deve ser imutável e implementar equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryAddress that = (DeliveryAddress) o;
        return Objects.equals(street, that.street) &&
                Objects.equals(number, that.number) &&
                Objects.equals(complement, that.complement) &&
                Objects.equals(neighborhood, that.neighborhood) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, complement, neighborhood, city, state, zipCode, reference);
    }

    // Builder para construção imutável
    public static class Builder {
        private String street;
        private String number;
        private String complement;
        private String neighborhood;
        private String city;
        private String state;
        private String zipCode;
        private String reference;

        public Builder withStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder withNumber(String number) {
            this.number = number;
            return this;
        }

        public Builder withComplement(String complement) {
            this.complement = complement;
            return this;
        }

        public Builder withNeighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public DeliveryAddress build() {
            return new DeliveryAddress(this);
        }
    }
}
