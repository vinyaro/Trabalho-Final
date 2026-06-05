package br.ufmg.coltec.journal.business;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class DateParserUtilTest {

    @Test
    public void testParseValidDate() {
        LocalDate result = DateParserUtil.parse("05/06/2026");
        assertNotNull(result);
        assertEquals(2026, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(5, result.getDayOfMonth());
    }

    @Test
    public void testParseEmptyOrNullDateReturnsNull() {
        assertNull(DateParserUtil.parse(""));
        assertNull(DateParserUtil.parse("   "));
        assertNull(DateParserUtil.parse(null));
    }

    @Test
    public void testParseInvalidDateFormatThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DateParserUtil.parse("2026-06-05"); // Formato ISO incorreto para a CLI
        });
        assertTrue(exception.getMessage().contains("Formato de data inválido"));
    }
}