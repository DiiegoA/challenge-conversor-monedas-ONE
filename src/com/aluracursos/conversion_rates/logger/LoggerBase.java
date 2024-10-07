package com.aluracursos.conversion_rates.logger;

// Interfaz que define métodos para registrar logs de información y advertencias
public interface LoggerBase {
    void logInfo(Object message); // Registra información
    void logWarning(String message); // Registra advertencias
}

