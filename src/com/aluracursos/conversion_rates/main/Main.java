package com.aluracursos.conversion_rates.main;

import com.aluracursos.conversion_rates.model.ExchangeRateApi;

public class Main {
    public static void main(String[] args) {

        // Crea una instancia de ExchangeRateApi que manejará las conversiones de moneda
        ExchangeRateApi exchange = new ExchangeRateApi();
        // Llama al método run() de la instancia ExchangeRateApi para iniciar el proceso
        exchange.run();
    }
}