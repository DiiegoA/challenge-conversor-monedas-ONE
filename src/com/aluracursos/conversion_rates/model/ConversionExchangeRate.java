package com.aluracursos.conversion_rates.model;

import java.util.Map;

// Clase record que almacena un mapa de tasas de conversión
public record ConversionExchangeRate(Map<String, Double> conversion_rates) {
    // Método que devuelve la tasa de cambio para una moneda específica
    public double getConversionRate(String currency) {
        if (conversion_rates.containsKey(currency)) {
            return conversion_rates.get(currency);
        } else {
            // Lanza una excepción si la tasa de cambio no está disponible
            throw new NumberFormatException(String.format("La tasa de cambio para la moneda %s no está disponible.",currency));
        }
    }
}