package br.ufmg.coltec.journal.presentation;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MenuConsole console = new MenuConsole();
        Scanner menuScanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            console.displayMainMenu();
            String option = menuScanner.nextLine().trim();

            switch (option) {
                case "1":
                    console.handleNewEntry();
                    break;
                case "2":
                    console.handleFilterEntries();
                    break;
                case "3":
                    console.handleExportEntries();
                    break;
                case "4":
                    System.out.println("\nEncerrando o Diário em Linha de Comando. Até logo!");
                    running = false;
                    break;
                default:
                    System.out.println("\n❌ Opção inválida! Por favor, selecione um número de 1 a 4.");
                    break;
            }
        }
        menuScanner.close();
    }
}