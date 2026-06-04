package br.ufmg.coltec.journal.data.filter;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import java.util.List;

public abstract class JournalFilterDecorator implements JournalFilter {
    // Referência protegida para que os filhos herdem o filtro encapsulado
    protected final JournalFilter decoratedFilter;

    public JournalFilterDecorator(JournalFilter filter) {
        this.decoratedFilter = filter;
    }

    @Override
    public List<JournalEntry> filter() {
        // Por padrão, delega a execução para o filtro que ele está envolvendo
        return this.decoratedFilter.filter();
    }
}