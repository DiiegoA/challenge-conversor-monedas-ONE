package com.aluracursos.conversion_rates.logger;


import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

// Implementación de LoggerBase que maneja los logs en consola
public class LoggerBaseImpl implements LoggerBase {

    // Logger de java.util.logging, declarado como una constante estática
    private static final Logger LOGGER = Logger.getLogger(LoggerBaseImpl.class.getName());

    // Constructor que inicializa el manejador de consola para el logger
    public LoggerBaseImpl(String className) {
        // Verifica si ya hay manejadores de log antes de agregar uno nuevo
        if (LOGGER.getHandlers().length == 0) {
            ConsoleHandler handler = new ConsoleHandler();
            // Establece un formateador que solo imprime el mensaje sin detalles adicionales
            handler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return formatMessage(record) + "\n";  // Solo imprime el mensaje
                }
            });
            // Agrega el manejador a LOGGER
            LOGGER.addHandler(handler);
        }

        // Desactiva los manejadores padres por defecto
        LOGGER.setUseParentHandlers(false);
    }

    // Método para registrar mensajes de información
    @Override
    public void logInfo(Object message) {
        LOGGER.info(String.valueOf(message)); // Registra el mensaje como información
    }

    // Método para registrar advertencias
    @Override
    public void logWarning(String message) {
        LOGGER.warning(message); // Registra el mensaje como advertencia
    }
}

