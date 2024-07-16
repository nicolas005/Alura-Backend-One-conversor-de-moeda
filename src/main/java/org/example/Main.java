package org.example;

//Nicolau Manuel António - Alura ONE Backend Dev
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String apiKey = "c15697c5f3a45c28935f0981";
        String baseCurrency = "USD";
        String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;

        try {
            // Fazer a solicitação HTTP
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Converter resposta para JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            // Verificar se a solicitação foi bem-sucedida
            String reqResult = jsonobj.get("result").getAsString();
            if (!"success".equals(reqResult)) {
                System.out.println("Erro ao obter as taxas de câmbio.");
                return;
            }

            // Obter as taxas de câmbio
            JsonObject rates = jsonobj.getAsJsonObject("conversion_rates");

            // Opções de conversão
            String[] currencies = {"EUR", "GBP", "CAD", "JPY", "AUD", "BRL"};

            while (true) {
                // Mostrar opções de conversão
                System.out.println("Escolha a conversão de moeda:");
                for (int i = 0; i < currencies.length; i++) {
                    System.out.println((i + 1) + ". USD para " + currencies[i]);
                }
                System.out.println((currencies.length + 1) + ". Sair");

                int choice = scanner.nextInt();

                if (choice < 1 || choice > currencies.length + 1) {
                    System.out.println("Escolha inválida.");
                    continue;
                }

                if (choice == currencies.length + 1) {
                    System.out.println("Saindo...");
                    break;
                }

                String targetCurrency = currencies[choice - 1];
                double rate = rates.get(targetCurrency).getAsDouble();

                System.out.print("Digite o valor em USD: ");
                double valueInUSD = scanner.nextDouble();
                double convertedValue = valueInUSD * rate;

                System.out.printf("Valor convertido: %.2f %s%n", convertedValue, targetCurrency);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao acessar a API.");
        }
    }
}
