package br.ufmg.coltec.journal.data.export;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class CsvExportFactory implements ExportFactory {
    @Override
    public Exporter createExporter() {
        return (entries, filepath) -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
                // Cabeçalho do arquivo CSV
                writer.write("Data;Categorias;Texto");
                writer.newLine();
                
                // Linhas de registros
                for (br.ufmg.coltec.journal.data.model.JournalEntry entry : entries) {
                    String dateStr = entry.getDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String categoriesStr = String.join("|", entry.getCategories());
                    
                    // Tratamento simples para evitar que quebras de linha ou aspas quebrem as colunas do CSV
                    String textStr = entry.getText().replace(";", ",").replace("\n", " ");
                    
                    writer.write(String.format("%s;%s;%s", dateStr, categoriesStr, textStr));
                    writer.newLine();
                }
            }
        };
    }
}