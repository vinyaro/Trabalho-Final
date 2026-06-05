package br.ufmg.coltec.journal.data.filter;

import br.ufmg.coltec.journal.data.model.JournalEntry;
import br.ufmg.coltec.journal.data.persistence.JournalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JournalFilterTest {

    private JournalRepository repository;

    // Definição de datas fixas para facilitar as faixas de teste
    private final LocalDate date1 = LocalDate.of(2026, 5, 1);
    private final LocalDate date2 = LocalDate.of(2026, 5, 15);
    private final LocalDate date3 = LocalDate.of(2026, 6, 1);

    @BeforeEach
    public void setUp() throws Exception {
        this.repository = JournalRepository.getInstance();
        
        // Técnica de Reflection para limpar o Singleton antes de cada teste
        // Isso impede que os dados de um teste interfiram no resultado do outro
        Field instanceField = JournalRepository.class.getDeclaredField("entries");
        instanceField.setAccessible(true);
        List<?> entries = (List<?>) instanceField.get(repository);
        entries.clear();

        // Inserção da massa de dados controlada para os testes
        repository.save(new JournalEntry("Estudando padrões de projeto em Java", date1, Arrays.asList("Estudo", "Java")));
        repository.save(new JournalEntry("Trabalho final de Engenharia de Software concluído", date2, Arrays.asList("Trabalho", "Faculdade")));
        repository.save(new JournalEntry("Revisão de código da arquitetura em camadas", date3, Arrays.asList("Trabalho", "Java")));
    }

    @Test
    public void testFilterBySubstringOnly() {
        JournalFilter filterChain = new SubstringFilterDecorator(new BaseFilter(), "padrões");
        List<JournalEntry> result = filterChain.filter();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getText().contains("padrões"));
    }

    @Test
    public void testFilterByDateRangeOnly() {
        // Faixa cobrindo do dia 01/05 ao dia 20/05 (Deve pegar os dois primeiros registros)
        JournalFilter filterChain = new DateRangeFilterDecorator(new BaseFilter(), date1, LocalDate.of(2026, 5, 20));
        List<JournalEntry> result = filterChain.filter();

        assertEquals(2, result.size());
    }

    @Test
    public void testFilterByCategoryOnly() {
        JournalFilter filterChain = new CategoryFilterDecorator(new BaseFilter(), "Java");
        List<JournalEntry> result = filterChain.filter();

        assertEquals(2, result.size());
    }

    @Test
    public void testFilterBySubstringAndDateRange() {
        JournalFilter filterChain = new SubstringFilterDecorator(
                new DateRangeFilterDecorator(new BaseFilter(), date1, date2),
                "Engenharia"
        );
        List<JournalEntry> result = filterChain.filter();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getText().contains("Trabalho final"));
    }

    @Test
    public void testFilterBySubstringAndCategory() {
        JournalFilter filterChain = new SubstringFilterDecorator(
                new CategoryFilterDecorator(new BaseFilter(), "Java"),
                "arquitetura"
        );
        List<JournalEntry> result = filterChain.filter();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getText().contains("Revisão de código"));
    }

    @Test
    public void testFilterByDateRangeAndCategory() {
        // Intervalo do dia 10/05 ao dia 05/06 buscando categoria "Java" (Deve trazer apenas o terceiro registro)
        JournalFilter filterChain = new DateRangeFilterDecorator(
                new CategoryFilterDecorator(new BaseFilter(), "Java"),
                LocalDate.of(2026, 5, 10),
                LocalDate.of(2026, 6, 5)
        );
        List<JournalEntry> result = filterChain.filter();

        assertEquals(1, result.size());
        assertEquals(date3, result.get(0).getDate());
    }

    @Test
    public void testFilterByAllThreeCriteria() {
        // Combinando os 3 filtros de forma encadeada (Abordagem Purista)
        JournalFilter filterChain = new SubstringFilterDecorator(
                new DateRangeFilterDecorator(
                        new CategoryFilterDecorator(new BaseFilter(), "Java"),
                        date1, date2
                ),
                "padrões"
        );
        List<JournalEntry> result = filterChain.filter();

        assertEquals(1, result.size());
        assertEquals("Estudando padrões de projeto em Java", result.get(0).getText());
    }
}