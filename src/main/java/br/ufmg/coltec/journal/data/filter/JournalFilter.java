package br.ufmg.coltec.journal.data.filter;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import java.util.List;

public interface JournalFilter {
    List<JournalEntry> filter();
}