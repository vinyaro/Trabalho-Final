package br.ufmg.coltec.journal.data.export;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import java.io.IOException;
import java.util.List;

public interface Exporter {
    void export(List<JournalEntry> entries, String filepath) throws IOException;
}