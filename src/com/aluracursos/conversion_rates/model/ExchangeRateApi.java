package com.aluracursos.conversion_rates.model;

import com.aluracursos.conversion_rates.logger.LoggerBase;
import com.aluracursos.conversion_rates.logger.LoggerBaseImpl;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// Clase que contiene la lógica para manejar la API de conversión de tasas de cambio
public class ExchangeRateApi {

    // Atributo LOGGER para registrar eventos en el sistema
    private static final LoggerBase LOGGER = new LoggerBaseImpl(ExchangeRateApi.class.getName()); // Inicialización estática

    // Instancia del servicio que maneja la conversión de divisas
    private final CurrencyConversionService service;

    // Lista que almacena el historial de conversiones
    private final List<String> history;

    // Constructor de la clase ExchangeRateApi, inicializa el logger y el historial
    public ExchangeRateApi() {
        this.history = new ArrayList<>(); // Crea una lista para guardar el historial de conversiones
        this.service = new CurrencyConversionService(history); // Pasa el historial al servicio de conversión
    }

    // Método principal que contiene la lógica del programa
    public void run() {
        Scanner scanner = new Scanner(System.in); // Objeto Scanner para leer la entrada del usuario
        boolean running = true; // Variable para controlar el ciclo del menú

        // Bucle que permite al usuario realizar conversiones hasta que decida salir
        while (running) {
            try {
                printMenu(); // Imprime el menú de opciones

                // Lee la opción ingresada por el usuario
                int choice = scanner.nextInt();

                // Verifica si la opción está fuera del rango permitido (1-14)
                if (choice < 1 || choice > 14) {
                    LOGGER.logInfo("Opción no válida, por favor intenta de nuevo."); // Informa sobre opción inválida
                    continue; // Vuelve a mostrar el menú si la opción no es válida
                }

                // Opción 13: mostrar historial de conversiones
                if (choice == 13) {
                    service.showHistory(); // Llama al método del servicio para mostrar el historial de conversiones
                    continue; // Vuelve a mostrar el menú tras mostrar el historial
                }

                // Opción 14: salir del programa
                if (choice == 14) {
                    running = false; // Detiene el ciclo del menú
                    LOGGER.logInfo("Gracias por usar el conversor de monedas. ¡Hasta luego!"); // Mensaje de despedida
                    break; // Sale del bucle y finaliza el programa
                }

                // Solicita el valor a convertir
                LOGGER.logInfo("Ingrese el valor que deseas convertir: ");
                String amountStr = scanner.next().replace(',', '.'); // Reemplaza la coma por un punto para manejo de decimales
                double amount = Double.parseDouble(amountStr); // Convierte el valor ingresado a tipo double

                // Llama al servicio para procesar la conversión
                service.processConversion(choice, amount); // Procesa la conversión basada en la opción y el monto ingresado

            } catch (InputMismatchException e) {
                LOGGER.logInfo("Entrada no válida. Por favor, ingrese un número."); // Captura errores por tipo de entrada no numérica
                scanner.next(); // Limpia el buffer del scanner
            } catch (NumberFormatException e) {
                LOGGER.logInfo("Entrada no válida. Por favor, ingrese un número válido."); // Captura errores de formato numérico inválido
            } catch (Exception e) {
                LOGGER.logInfo("Ocurrió un error inesperado: " + e.getMessage()); // Captura y registra cualquier otro error inesperado
            }
        }
    }

    // Método que imprime el menú de opciones de conversión
    private static void printMenu() {
        // Usa el logger para imprimir el menú de opciones
        LOGGER.logInfo(String.format("%n********************************************%n" +
                "Sea bienvenido/a al Conversor de Moneda =]%n" + // Mensaje de bienvenida
                "1) Dólar => Peso argentino%n" + // Opción 1: Convertir de USD a ARS
                "2) Peso argentino => Dólar%n" + // Opción 2: Convertir de ARS a USD
                "3) Dólar => Real brasileño%n" + // Opción 3: Convertir de USD a BRL
                "4) Real brasileño => Dólar%n" + // Opción 4: Convertir de BRL a USD
                "5) Dólar => Peso colombiano%n" + // Opción 5: Convertir de USD a COP
                "6) Peso colombiano => Dólar%n" + // Opción 6: Convertir de COP a USD
                "7) Dólar => Peso mexicano%n" + // Opción 7: Convertir de USD a MXN
                "8) Peso mexicano => Dólar%n" + // Opción 8: Convertir de MXN a USD
                "9) Dólar => Peso boliviano%n" + // Opción 9: Convertir de USD a BOB
                "10) Peso boliviano => Dólar%n" + // Opción 10: Convertir de BOB a USD
                "11) Dólar => Peso chileno%n" + // Opción 11: Convertir de USD a CLP
                "12) Peso chileno => Dólar%n" + // Opción 12: Convertir de CLP a USD
                "13) Ver historial de conversiones%n" + // Opción 13: Mostrar historial de conversiones
                "14) Salir%n" + // Opción 14: Salir del programa
                "Elija una opción válida:%n" +
                "*********************************************%n")); // Cierra el menú de opciones
    }
}