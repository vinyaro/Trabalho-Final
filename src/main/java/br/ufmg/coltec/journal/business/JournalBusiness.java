package br.ufmg.coltec.journal.business;

import br.ufmg.coltec.journal.data.filter.*;
import br.ufmg.coltec.journal.data.model.JournalEntry;
import br.ufmg.coltec.journal.data.persistence.JournalRepository;

import java.time.LocalDate;
import java.util.List;

public class JournalBusiness {

    private final JournalRepository repository;

    public JournalBusiness() {
        this.repository = JournalRepository.getInstance();
    }

    /**
     * Regra 1: Insere uma nova entrada convertendo os dados adequadamente
     */
    public void createEntry(String text, String dateStr, List<String> categories) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("O texto do diário não pode estar vazio.");
        }
        
        LocalDate date = DateParserUtil.parse(dateStr);
        if (date == null) {
            date = LocalDate.now(); // Fallback caso não seja informada data
        }

        JournalEntry entry = new JournalEntry(text, date, categories);
        repository.save(entry);
    }

    /**
     * Regra 2: Combina dinamicamente os Decorators de filtragem com base nos inputs preenchidos
     */
    public List<JournalEntry> searchEntries(String substring, String startDateStr, String endDateStr, String category) {
        // Começa sempre com o filtro base (todas as entradas)
        JournalFilter filterChain = new BaseFilter();

        // Se o usuário preencheu a categoria, decora a corrente
        if (category != null && !category.trim().isEmpty()) {
            filterChain = new CategoryFilterDecorator(filterChain, category);
        }

        // Se preencheu alguma das datas, decora com o filtro de período
        if ((startDateStr != null && !startDateStr.trim().isEmpty()) || 
            (endDateStr != null && !endDateStr.trim().isEmpty())) {
            LocalDate start = DateParserUtil.parse(startDateStr);
            LocalDate end = DateParserUtil.parse(endDateStr);
            filterChain = new DateRangeFilterDecorator(filterChain, start, end);
        }

        // Se preencheu a substring, decora por último
        if (substring != null && !substring.trim().isEmpty()) {
            filterChain = new SubstringFilterDecorator(filterChain, substring);
        }

        // Executa a cadeia montada recursivamente
        return filterChain.filter();
    }

    /**
     * Regra 3: Exporta as entradas do diário para um arquivo externo utilizando Abstract Factory
     */
    public void exportEntries(String type, String path, List<JournalEntry> entriesToExport) throws java.io.IOException {
        if (entriesToExport == null || entriesToExport.isEmpty()) {
            throw new IllegalArgumentException("Não existem entradas na busca atual para serem exportadas.");
        }

        br.ufmg.coltec.journal.data.export.ExportFactory factory;

        // O switch escolhe a fábrica concreta com base na decisão da interface de usuário (CLI)
        switch (type.trim().toLowerCase()) {
            case "json":
                factory = new br.ufmg.coltec.journal.data.export.JsonExportFactory();
                break;
            case "csv":
                factory = new br.ufmg.coltec.journal.data.export.CsvExportFactory();
                break;
            default:
                throw new IllegalArgumentException("Formato de exportação não suportado: " + type);
        }

        // Fabricação e execução polimórfica (Sem expor detalhes de implementação de I/O)
        br.ufmg.coltec.journal.data.export.Exporter exporter = factory.createExporter();
        exporter.export(entriesToExport, path);
    }
}