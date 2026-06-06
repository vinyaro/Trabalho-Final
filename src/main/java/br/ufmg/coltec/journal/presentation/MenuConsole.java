package br.ufmg.coltec.journal.presentation;

import br.ufmg.coltec.journal.business.JournalBusiness;
import br.ufmg.coltec.journal.data.model.JournalEntry;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuConsole {

    private final JournalBusiness business;
    private final Scanner scanner;
    // Guarda o último resultado de busca na memória da sessão para permitir exportações contextuais
    private List<JournalEntry> lastSearchResults;

    public MenuConsole() {
        this.business = new JournalBusiness();
        this.scanner = new Scanner(System.in);
        this.lastSearchResults = null;
    }

    /**
     * Apresenta o menu textual principal na tela conforme o formato exigido.
     */
    public void displayMainMenu() {
        System.out.println("\n=======================================");
        System.out.println("Seja bem vindo ao Journaling.");
        System.out.println("=======================================");
        System.out.println("Selecione uma das opções abaixo:");
        System.out.println("1. Nova entrada");
        System.out.println("2. Filtrar entradas");
        System.out.println("3. Exportar entradas");
        System.out.println("4. Sair");
        System.out.print("Sua opção: ");
    }

    /**
     * Procedimento específico para a Funcionalidade 1: Inserção de Novas Entradas
     */
    public void handleNewEntry() {
        System.out.println("\n--- [ NOVA ENTRADA DO DIÁRIO ] ---");
        System.out.print("Digite o texto da nota: ");
        String text = scanner.nextLine();

        System.out.print("Digite a data (dd/MM/yyyy) ou aperte [Enter] para usar hoje: ");
        String dateStr = scanner.nextLine();

        System.out.print("Digite as categorias separadas por vírgula (Ex: Estudo, Java, UFMG): ");
        String categoriesInput = scanner.nextLine();

        // Processa as categorias quebrando por vírgula e removendo espaços inúteis
        List<String> categories = Arrays.stream(categoriesInput.split(","))
                .map(String::trim)
                .filter(cat -> !cat.isEmpty())
                .collect(Collectors.toList());

        try {
            business.createEntry(text, dateStr, categories);
            System.out.println("🟢 Sucesso: Entrada salva com sucesso no repositório!");
        } catch (IllegalArgumentException e) {
            System.out.println("🔴 Erro de validação: " + e.getMessage());
        }
    }

    /**
     * Procedimento específico para a Funcionalidade 2: Filtragem Interativa
     */
    public void handleFilterEntries() {
        System.out.println("\n--- [ FILTRAR ENTRADAS (Deixe em branco para ignorar um filtro) ] ---");
        
        System.out.print("Filtrar por trecho de texto (Substring): ");
        String substring = scanner.nextLine();

        System.out.print("Filtrar a partir da data de início (dd/MM/yyyy): ");
        String startDateStr = scanner.nextLine();

        System.out.print("Filtrar até a data de término (dd/MM/yyyy): ");
        String endDateStr = scanner.nextLine();

        System.out.print("Filtrar por categoria específica: ");
        String category = scanner.nextLine();

        try {
            // Orquestra a montagem do Decorator na camada de negócio e guarda os resultados
            lastSearchResults = business.searchEntries(substring, startDateStr, endDateStr, category);

            System.out.println("\n--- [ RESULTADO DA BUSCA ] ---");
            if (lastSearchResults.isEmpty()) {
                System.out.println("⚠️ Nenhuma entrada encontrada para os critérios informados.");
            } else {
                System.out.println("Foram encontradas " + lastSearchResults.size() + " entrada(s):");
                lastSearchResults.forEach(entry -> System.out.println(" -> " + entry));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("🔴 Erro nos parâmetros de busca: " + e.getMessage());
        }
    }

    /**
     * Procedimento específico para a Funcionalidade 3: Exportação via Abstract Factory
     */
    public void handleExportEntries() {
        System.out.println("\n--- [ EXPORTAR ENTRADAS ] ---");
        
        // Se o usuário não filtrou nada antes, usamos todas as entradas registradas no sistema
        List<String> emptyFilters = Arrays.asList("", "", "", "");
        List<JournalEntry> entriesToExport = (lastSearchResults != null) ? lastSearchResults : business.searchEntries("", "", "", "");

        if (entriesToExport.isEmpty()) {
            System.out.println("❌ Erro: Não há nenhuma nota registrada no diário para ser exportada.");
            return;
        }

        System.out.print("Escolha o formato de destino (json / csv): ");
        String type = scanner.nextLine().trim().toLowerCase();

        System.out.print("Digite o nome ou caminho do arquivo final (Ex: meu_diario.json ou notas.csv): ");
        String path = scanner.nextLine();

        try {
            business.exportEntries(type, path, entriesToExport);
            System.out.println("🟢 Sucesso: Arquivo exportado com êxito para '" + path + "'!");
        } catch (IllegalArgumentException e) {
            System.out.println("🔴 Erro de parâmetro: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("🔴 Erro crítico de I/O ao gravar o arquivo físico: " + e.getMessage());
        }
    }
}