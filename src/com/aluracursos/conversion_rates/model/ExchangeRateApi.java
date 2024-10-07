package com.aluracursos.conversion_rates.model;

import com.aluracursos.conversion_rates.logger.LoggerBase;
import com.aluracursos.conversion_rates.logger.LoggerBaseImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

// Clase que contiene la lógica para manejar la API de conversión de tasas de cambio
public class ExchangeRateApi {

    // Atributo final que almacena el logger para registrar mensajes en la consola
    private final LoggerBase logger;

    // Instancia del servicio que maneja la conversión de divisas
    CurrencyConversionService service = new CurrencyConversionService();

    // Constructor de la clase ExchangeRateApi, inicializa el logger con el nombre de la clase
    public ExchangeRateApi() {
        this.logger = new LoggerBaseImpl(ExchangeRateApi.class.getName());
    }

    // Método que contiene la lógica principal del programa
    public void run() {
        // Objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        // Bandera para controlar el ciclo del menú
        boolean running = true;

        // Bucle que permite al usuario realizar múltiples conversiones hasta que decida salir
        while (running) {
            try {
                // Llama al método que imprime el menú
                printMenu();

                // Lee la opción ingresada por el usuario
                int choice = scanner.nextInt();

                // Verifica si la opción está fuera del rango permitido (1-9)
                if (choice < 1 || choice > 9) {
                    // Si la opción no es válida, registra un mensaje y continúa el ciclo
                    logger.logInfo("Opción no válida, por favor intenta de nuevo.");
                    continue;
                }

                // Si el usuario elige la opción 9, se cierra el programa
                if (choice == 9) {
                    // Cambia la bandera para salir del bucle
                    running = false;
                    // Registra un mensaje de despedida
                    logger.logInfo("Gracias por usar el conversor de monedas. ¡Hasta luego!");
                    // Rompe el bucle
                    break;
                }

                // Solicita el valor a convertir al usuario
                logger.logInfo("Ingrese el valor que deseas convertir: ");

                // Lee el valor ingresado y reemplaza comas por puntos (para decimales)
                String amountStr = scanner.next().replace(',', '.');

                // Convierte la entrada de string a double
                double amount = Double.parseDouble(amountStr);

                // Llama al servicio para procesar la conversión según la opción y el monto ingresado
                service.processConversion(choice, amount);
            } catch (InputMismatchException e) {
                // Captura entradas no válidas como letras y registra un mensaje de error
                logger.logInfo("Entrada no válida. Por favor, ingrese un número.");
                scanner.next(); // Limpia el buffer
            } catch (NumberFormatException e) {
                // Captura errores de conversión numérica y muestra un mensaje
                logger.logInfo("Entrada no válida. Por favor, ingrese un número válido.");
            } catch (Exception e) {
                // Captura cualquier otra excepción y registra el error
                logger.logInfo("Ocurrió un error inesperado: " + e.getMessage());
            }
        }
    }

    // Método que imprime el menú de opciones de conversión
    private void printMenu() {
        // Usa el logger para mostrar el menú de opciones
        logger.logInfo(String.format("""
                %n********************************************
                Sea bienvenido/a al Conversor de Moneda =]
                1) Dólar => Peso argentino
                2) Peso argentino => Dólar
                3) Dólar => Real brasileño
                4) Real brasileño => Dólar
                5) Dólar => Peso colombiano
                6) Peso colombiano => Dólar
                7) Dólar => Peso mexicano
                8) Peso mexicano => Dólar
                9) Salir
                Elija una opción válida:
                *********************************************
                """));
    }
}

