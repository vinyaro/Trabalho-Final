package br.ufmg.coltec.journal.data.filter;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryFilterDecorator extends JournalFilterDecorator {
    private final String targetCategory;

    public CategoryFilterDecorator(JournalFilter filter, String targetCategory) {
        super(filter);
        this.targetCategory = targetCategory != null ? targetCategory.trim().toLowerCase() : "";
    }

    @Override
    public List<JournalEntry> filter() {
        return super.filter().stream()
                .filter(entry -> entry.getCategories() != null && entry.getCategories().stream()
                        .anyMatch(cat -> cat.trim().toLowerCase().equals(targetCategory)))
                .collect(Collectors.toList());
    }
}