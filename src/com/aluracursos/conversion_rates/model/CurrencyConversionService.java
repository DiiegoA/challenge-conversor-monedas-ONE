package com.aluracursos.conversion_rates.model;


import com.google.gson.Gson;
import com.aluracursos.conversion_rates.logger.LoggerBase;
import com.aluracursos.conversion_rates.logger.LoggerBaseImpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

// Clase que maneja las conversiones y la comunicación con la API de tasas de cambio
public class CurrencyConversionService {

    // Atributo logger para registrar eventos de la aplicación
    private final LoggerBase logger;

    // Constructor que inicializa el logger
    public CurrencyConversionService() {
        this.logger = new LoggerBaseImpl(CurrencyConversionService.class.getName());
    }

    // URL de la API que proporciona las tasas de cambio
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/7278242838ed23899ff69393/latest/USD";

    // Método para procesar la conversión basado en la elección del usuario
    public void processConversion(int choice, double amount) {
        try {
            // Procesa las opciones del menú para la conversión
            switch (choice) {
                case 1:
                    convertCurrency(amount, "ARS"); // Dólar a Peso argentino
                    break;
                case 2:
                    convertCurrencyInverse(amount, "ARS"); // Peso argentino a Dólar
                    break;
                case 3:
                    convertCurrency(amount, "BRL"); // Dólar a Real brasileño
                    break;
                case 4:
                    convertCurrencyInverse(amount, "BRL"); // Real brasileño a Dólar
                    break;
                case 5:
                    convertCurrency(amount, "COP"); // Dólar a Peso colombiano
                    break;
                case 6:
                    convertCurrencyInverse(amount, "COP"); // Peso colombiano a Dólar
                    break;
                case 7:
                    convertCurrency(amount, "MXN"); // Dólar a Peso mexicano
                    break;
                case 8:
                    convertCurrencyInverse(amount, "MXN"); // Peso mexicano a Dólar
                    break;
                default:
                    // Si la opción no es válida, registra un mensaje
                    logger.logInfo("Opción no válida, por favor intenta de nuevo.");
            }
        } catch (NumberFormatException e) {
            // Captura errores al formatear números y muestra un mensaje de error
            logger.logInfo("Error al convertir la moneda: valor de tasa no válido.");
        } catch (Exception e) {
            // Captura cualquier otro error durante la conversión
            logger.logInfo("Error al procesar la conversión: " + e.getMessage());
        }
    }

    // Método para convertir de USD a la moneda de destino
    public void convertCurrency(double amount, String targetCurrency) {
        try {
            // Obtiene la tasa de cambio para la moneda de destino
            double rate = getExchangeRate(targetCurrency);
            if (rate == 0) {
                // Si no se encuentra la tasa, muestra un mensaje de error
                logger.logInfo("No se pudo obtener la tasa de cambio para la moneda solicitada.");
                return;
            }
            // Realiza la conversión
            double convertedAmount = amount * rate;
            // Registra el resultado de la conversión
            logger.logInfo(String.format("El valor %.2f [USD] corresponde al valor final de >>> %.2f [%s]", amount, convertedAmount, targetCurrency));
        } catch (IOException e) {
            // Maneja errores de entrada/salida durante la obtención de la tasa
            logger.logInfo("Error al obtener la tasa de cambio: " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otro error
            logger.logInfo("Ocurrió un error al convertir la moneda: " + e.getMessage());
        }
    }

    // Método para convertir desde una moneda de origen a USD
    public void convertCurrencyInverse(double amount, String sourceCurrency) {
        try {
            // Obtiene la tasa de cambio para la moneda de origen
            double rate = getExchangeRate(sourceCurrency);
            if (rate == 0) {
                logger.logInfo("No se pudo obtener la tasa de cambio para la moneda solicitada.");
                return;
            }
            // Realiza la conversión inversa
            double convertedAmount = amount / rate;
            // Registra el resultado de la conversión
            logger.logInfo(String.format("El valor %.2f [%s] corresponde al valor final de >>> %.2f [USD]", amount, sourceCurrency, convertedAmount));
        } catch (IOException e) {
            logger.logInfo("Error al obtener la tasa de cambio: " + e.getMessage());
        } catch (Exception e) {
            logger.logInfo("Ocurrió un error al convertir la moneda: " + e.getMessage());
        }
    }

    // Método que obtiene la tasa de cambio desde la API
    private double getExchangeRate(String currency) throws IOException {
        try {
            // Crea una URL para conectarse a la API
            URL url = new URL(API_URL);
            HttpURLConnection conect = (HttpURLConnection) url.openConnection(); // Abre la conexión HTTP
            conect.setRequestMethod("GET"); // Establece el método GET
            conect.connect(); // Conecta a la API

            // Verifica si la respuesta fue exitosa (código 200)
            int responseCode = conect.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("HTTP Response Code: " + responseCode);
            } else {
                // Lee la respuesta de la API
                Scanner scanner = new Scanner(conect.getInputStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                // Usa Gson para convertir el JSON a un objeto Java
                Gson gson = new Gson();
                ConversionExchangeRate response = gson.fromJson(inline.toString(), ConversionExchangeRate.class);
                // Retorna la tasa de cambio para la moneda solicitada
                return response.getConversionRate(currency);
            }
        } catch (MalformedURLException e) {
            throw new IOException("URL malformada: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Error de conexión: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new IOException("Error al parsear la tasa de cambio: " + e.getMessage());
        }
    }
}