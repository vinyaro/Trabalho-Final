package br.ufmg.coltec.journal.data.persistence;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JournalRepository {
    // Instância única instanciada estaticamente (Thread-safe)
    private static final JournalRepository instance = new JournalRepository();
    
    // Armazenamento em memória (Simples e eficiente para o escopo CLI)
    private final List<JournalEntry> entries;

    // Construtor privado impede instanciação externa externa via 'new'
    private JournalRepository() {
        this.entries = new ArrayList<>();
    }

    // Ponto de acesso global à instância do Singleton
    public static JournalRepository getInstance() {
        return instance;
    }

    /**
     * Adiciona uma nova entrada ao diário.
     */
    public void save(JournalEntry entry) {
        this.entries.add(entry);
    }

    /**
     * Retorna uma lista não modificável para proteger a consistência dos dados do Singleton.
     */
    public List<JournalEntry> getAll() {
        return Collections.unmodifiableList(this.entries);
    }
}