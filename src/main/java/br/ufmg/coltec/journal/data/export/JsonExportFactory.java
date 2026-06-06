package br.ufmg.coltec.journal.data.export;

public class JsonExportFactory implements ExportFactory {
    @Override
    public Exporter createExporter() {
        return (entries, filepath) -> {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(java.time.LocalDate.class, 
                        (com.google.gson.JsonSerializer<java.time.LocalDate>) (src, typeOfSrc, context) -> 
                            new com.google.gson.JsonPrimitive(src.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                    .create();
            
            try (java.io.FileWriter writer = new java.io.FileWriter(filepath)) {
                gson.toJson(entries, writer);
            }
        };
    }
}