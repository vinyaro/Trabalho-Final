package br.ufmg.coltec.journal.data.model;

import java.time.LocalDate;
import java.util.List;

public class JournalEntry {
    private String text;
    private LocalDate date;
    private List<String> categories;

    public JournalEntry(String text, LocalDate date, List<String> categories) {
        this.text = text;
        this.date = date;
        this.categories = categories;
    }

    // Getters e Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "[" + date + "] (" + String.join(", ", categories) + ") - " + text;
    }
}