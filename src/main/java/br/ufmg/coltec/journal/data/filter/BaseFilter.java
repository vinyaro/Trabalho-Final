package br.ufmg.coltec.journal.data.filter;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import br.ufmg.coltec.journal.data.persistence.JournalRepository;
import java.util.List;

public class BaseFilter implements JournalFilter {

    @Override
    public List<JournalEntry> filter() {
        // Ponto de partida: pega todas as entradas registradas no Singleton
        return JournalRepository.getInstance().getAll();
    }
}