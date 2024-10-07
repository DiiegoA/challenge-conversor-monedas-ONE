
# Currency Conversion API Application

## Overview
This Java-based currency conversion application allows users to convert between USD and several Latin American currencies in real-time using data fetched from [ExchangeRate-API](https://www.exchangerate-api.com/). It supports both direct and inverse conversions with user-friendly prompts and logging functionality to track user interactions and errors.

### Features
- Supports currency conversion between USD and:
    - Argentine Peso (ARS)
    - Brazilian Real (BRL)
    - Colombian Peso (COP)
    - Mexican Peso (MXN)
    - Bolivian Boliviano (BOB)
    - Chilean Peso (CLP)
- Allows both direct conversion (USD to target currency) and inverse conversion (target currency to USD).
- Fetches real-time exchange rates from ExchangeRate-API.
- Provides a command-line interface (CLI) with simple prompts for user input.
- Logs user interactions and system events (e.g., invalid input, exceptions).
- Handles exceptions and errors like invalid input types and network issues.

## Requirements
- Java 11 or higher
- Internet connection to fetch exchange rates from the API

## How to Run

### Step 1: Clone the Repository
Clone the repository containing the source files.
```bash
git clone https://github.com/DiiegoA/challenge-conversor-monedas-ONE.git
```

### Step 2: Compile the Java Files
Compile the source files using the following command:
```bash
javac -d bin src/com/aluracursos/conversion_rates/**/*.java
```

### Step 3: Run the Application
Run the application using the command:
```bash
java -cp bin com.aluracursos.conversion_rates.main.Main
```

### Step 4: Using the Application
Once you run the application, you will be presented with a menu to select the desired conversion type and input the amount to be converted. Based on the user's selection, the system will fetch live exchange rates and display the converted amount.

## Project Structure

### **Main.java**
- This is the entry point of the application. It creates an instance of `ExchangeRateApi` and runs the main logic by calling the `run()` method.

### **ExchangeRateApi.java**
- This class contains the core functionality of the application.
- It handles user interaction through a menu and allows users to input an amount for conversion.
- It manages the conversion process by calling `CurrencyConversionService` for real-time exchange rates.
- It logs all events and user interactions.

### **CurrencyConversionService.java**
- This class is responsible for interacting with the ExchangeRate-API.
- It sends HTTP requests to the API to fetch the current exchange rates for USD to other currencies.
- It includes methods for converting between USD and the supported currencies both directly and inversely.
- It handles exceptions related to network issues or invalid responses from the API.

### **ConversionExchangeRate.java**
- This is a model class used to represent the JSON response from the ExchangeRate-API.
- It contains a map of currency conversion rates and a method to retrieve a specific rate based on the currency code.

### **LoggerBase.java**
- This is an interface that defines logging methods used across the application.
- It includes methods like `logInfo()` for information-level messages and `logWarning()` for warning messages.

### **LoggerBaseImpl.java**
- Implements the `LoggerBase` interface using Java's built-in logging framework (`java.util.logging`).
- It configures a console handler to display logs in a formatted way and ensures only the message is printed without additional details.
- It logs events such as information messages or warnings for invalid input.

## Error Handling
The application implements several layers of error handling:
- **InputMismatchException**: Caught when the user enters a non-numeric value where a number is expected.
- **NumberFormatException**: Handles incorrect number formats when parsing inputs (e.g., malformed double values).
- **IOException**: Handles issues related to network communication with the ExchangeRate-API, including malformed URLs and failed API requests.
- **General Exception**: A fallback catch-all for any unexpected errors to prevent the program from crashing.

## API Integration
This application integrates with [ExchangeRate-API](https://www.exchangerate-api.com/) to provide live exchange rates. Ensure that the API URL and key are correctly configured in `CurrencyConversionService.java`.

Example API URL:
```java
private static final String API_URL = "https://v6.exchangerate-api.com/v6/your-api-key/latest/USD";
```

## Sample User Interaction

1. The user is presented with the following menu:
    ```
    ********************************************
    Welcome to the Currency Converter!
    1) Dólar => Peso argentino
    2) Peso argentino => Dólar
    3) Dólar => Real brasileño
    4) Real brasileño => Dólar
    5) Dólar => Peso colombiano
    6) Peso colombiano => Dólar
    7) Dólar => Peso mexicano
    8) Peso mexicano => Dólar
    9) Dólar => Peso boliviano
    10) Peso boliviano => Dólar
    11) Dólar => Peso chileno
    12) Peso chileno => Dólar
    13) Ver historial de conversiones
    14) Salir
    ********************************************
    Please choose an option:
    ```

2. The user selects an option and inputs the amount to be converted.

3. The system fetches the exchange rate, performs the conversion, and displays the result.

4. Logs are generated for each user interaction and potential errors.

## Logging
All events, including user interactions, errors, and exceptions, are logged using the `LoggerBase` interface and its implementation `LoggerBaseImpl`. The logs help in debugging and tracking user behavior.

Example log output:
```
INFO: User selected option 1: USD to Argentine Peso (ARS)
INFO: User entered amount: 100
INFO: The converted amount is: 14100.00 ARS
```

<<<<<<< HEAD
---

Happy converting!
=======
>>>>>>> 8a135fe0afe6bc3861f480b1bdc5c3bc15a2d908
