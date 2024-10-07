package com.aluracursos.conversion_rates.model;


import com.google.gson.Gson;
import com.aluracursos.conversion_rates.logger.LoggerBase;
import com.aluracursos.conversion_rates.logger.LoggerBaseImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Clase que maneja las conversiones y la comunicación con la API de tasas de cambio
public class CurrencyConversionService {

    // Atributo LOGGER para registrar eventos en el sistema
    private static final LoggerBase LOGGER = new LoggerBaseImpl(CurrencyConversionService.class.getName()); // Inicialización estática

    // Cliente HTTP para realizar solicitudes a la API
    private final HttpClient client;

    // Lista que almacena el historial de conversiones
    private final List<String> history;

    // URL de la API que provee las tasas de cambio
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/7278242838ed23899ff69393/latest/USD";

    // Constructor que inicializa el logger, el cliente HTTP y la lista de historial
    public CurrencyConversionService(List<String> history) {
        this.client = HttpClient.newHttpClient(); // Crea una nueva instancia de HttpClient
        this.history = history; // Inicializa la lista de historial
    }

    // Método que procesa la conversión basada en la opción del usuario y el monto
    public void processConversion(int choice, double amount) {
        try {
            boolean isUsdToCurrency = isUsdToCurrency(choice); // Determina si la conversión es de USD a otra moneda o viceversa
            String currency = getCurrencyCode(choice); // Obtiene el código de la moneda según la opción elegida

            // Verifica si el código de la moneda es válido
            if (currency != null) {
                // Realiza la conversión dependiendo de la dirección (USD -> Moneda o Moneda -> USD)
                if (isUsdToCurrency) {
                    convertUsdToCurrency(amount, currency); // Convierte de USD a otra moneda
                } else {
                    convertCurrencyToUsd(amount, currency); // Convierte de otra moneda a USD
                }
            } else {
                LOGGER.logInfo("Opción no válida."); // Informa al usuario si la opción es inválida
            }
        } catch (Exception e) {
            LOGGER.logInfo("Error en la conversión: " + e.getMessage()); // Captura y registra cualquier error
        }
    }

    // Método que convierte un monto de USD a una moneda específica
    private void convertUsdToCurrency(double amount, String targetCurrency) {
        try {
            double rate = getExchangeRate(targetCurrency); // Obtiene la tasa de cambio desde la API

            if (rate == 0) { // Verifica si la tasa de cambio es válida
                LOGGER.logInfo("No se pudo obtener la tasa de cambio."); // Informa si no se puede obtener la tasa
                return;
            }

            double convertedAmount = round(amount * rate, 2); // Realiza la conversión y redondea el resultado

            // Muestra el resultado de la conversión
            LOGGER.logInfo(String.format("%.2f USD corresponde a %.2f %s", amount, convertedAmount, targetCurrency));

            // Registra la conversión con una marca de tiempo en el historial
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            history.add(String.format("[%s] %.2f USD => %.2f %s", timestamp, amount, convertedAmount, targetCurrency));

        } catch (IOException e) {
            LOGGER.logInfo("Error de conexión o IO durante la conversión: " + e.getMessage()); // Manejo de errores de entrada/salida o conexión
        } catch (InterruptedException  e) {
            LOGGER.logInfo("La operación fue interrumpida: " + e.getMessage()); // Maneja interrupciones en el hilo de ejecución
        }
    }

    /// Método que convierte un monto de una moneda específica a USD
    private void convertCurrencyToUsd(double amount, String sourceCurrency) {
        try {
            double rate = getExchangeRate(sourceCurrency); // Obtiene la tasa de cambio desde la API

            if (rate == 0) { // Verifica si la tasa de cambio es válida
                LOGGER.logInfo("No se pudo obtener la tasa de cambio."); // Informa si no se puede obtener la tasa
                return;
            }

            double convertedAmount = round(amount / rate, 2); // Realiza la conversión inversa y redondea el resultado

            // Muestra el resultado de la conversión inversa
            LOGGER.logInfo(String.format("%.2f %s corresponde a %.2f USD", amount, sourceCurrency, convertedAmount));

            // Registra la conversión con una marca de tiempo en el historial
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            history.add(String.format("[%s] %.2f %s => %.2f USD", timestamp, amount, sourceCurrency, convertedAmount));

        } catch (IOException e) {
            LOGGER.logInfo("Error de conexión o IO durante la conversión: " + e.getMessage()); // Manejo de errores de entrada/salida o conexión
        } catch (InterruptedException  e) {
            LOGGER.logInfo("La operación fue interrumpida: " + e.getMessage()); // Maneja interrupciones en el hilo de ejecución
        }
    }

    // Método que obtiene la tasa de cambio desde la API
    private double getExchangeRate(String currency) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).GET().build(); // Construye una solicitud HTTP GET
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); // Envía la solicitud y recibe la respuesta

        if (response.statusCode() != 200) { // Verifica si la respuesta fue exitosa
            throw new IOException("Error al obtener respuesta: " + response.statusCode()); // Lanza una excepción si falla
        }

        Gson gson = new Gson(); // Crea una instancia de Gson para manejar JSON
        ConversionExchangeRate exchangeRate = gson.fromJson(response.body(), ConversionExchangeRate.class); // Convierte el JSON en un objeto Java
        return exchangeRate.getConversionRate(currency); // Retorna la tasa de cambio
    }

    // Método que obtiene el código de la moneda basado en la opción del usuario
    private static String getCurrencyCode(int choice) {
        return switch (choice) { // Asocia la opción ingresada con un código de moneda
            case 1, 2 -> "ARS"; // Peso argentino
            case 3, 4 -> "BRL"; // Real brasileño
            case 5, 6 -> "COP"; // Peso colombiano
            case 7, 8 -> "MXN"; // Peso mexicano
            case 9, 10 -> "BOB"; // Peso boliviano
            case 11, 12 -> "CLP"; // Peso chileno
            default -> null; // Si no es válida, retorna null
        };
    }

    // Método que determina si la conversión es de USD a la moneda o de la moneda a USD
    private static boolean isUsdToCurrency(int choice) {
        return switch (choice) { // Verifica si la conversión es de USD a otra moneda
            case 1, 3, 5, 7, 9, 11 -> true; // USD a moneda
            case 2, 4, 6, 8, 10, 12 -> false; // Moneda a USD
            default -> false; // Opción inválida
        };
    }

    // Método para redondear los valores a una precisión específica
    private static double round(double value, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException(); // Lanza una excepción si los lugares decimales son inválidos

        BigDecimal bd = BigDecimal.valueOf(value); // Convierte el valor en BigDecimal para mayor precisión
        bd = bd.setScale(decimals, RoundingMode.HALF_UP); // Redondea el valor según los lugares decimales especificados
        return bd.doubleValue(); // Retorna el valor redondeado
    }

    // Método que muestra el historial de conversiones
    public void showHistory() {
        if (history.isEmpty()) { // Verifica si el historial está vacío
            LOGGER.logInfo("No hay conversiones en el historial."); // Informa que no hay historial
        } else {
            LOGGER.logInfo("Historial de Conversiones:"); // Muestra el historial de conversiones
            history.forEach(LOGGER::logInfo); // Itera sobre el historial y lo muestra en la consola
        }
    }
}