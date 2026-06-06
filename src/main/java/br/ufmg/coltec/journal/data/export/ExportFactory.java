package br.ufmg.coltec.journal.data.export;

public interface ExportFactory {
    Exporter createExporter();
}