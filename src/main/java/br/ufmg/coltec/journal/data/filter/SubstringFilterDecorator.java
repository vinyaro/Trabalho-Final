package br.ufmg.coltec.journal.data.filter;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import java.util.List;
import java.util.stream.Collectors;

public class SubstringFilterDecorator extends JournalFilterDecorator {
    private final String query;

    public SubstringFilterDecorator(JournalFilter filter, String query) {
        super(filter);
        this.query = query != null ? query.toLowerCase() : "";
    }

    @Override
    public List<JournalEntry> filter() {
        // Obtém o resultado do filtro anterior e aplica o filtro por substring
        return super.filter().stream()
                .filter(entry -> entry.getText() != null && entry.getText().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }
}