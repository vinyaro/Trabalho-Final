package br.ufmg.coltec.journal.data.filter;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DateRangeFilterDecorator extends JournalFilterDecorator {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRangeFilterDecorator(JournalFilter filter, LocalDate startDate, LocalDate endDate) {
        super(filter);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<JournalEntry> filter() {
        return super.filter().stream()
                .filter(entry -> {
                    LocalDate date = entry.getDate();
                    if (date == null) return false;
                    
                    boolean matchesStart = (startDate == null) || !date.isBefore(startDate);
                    boolean matchesEnd = (endDate == null) || !date.isAfter(endDate);
                    
                    return matchesStart && matchesEnd;
                })
                .collect(Collectors.toList());
    }
}