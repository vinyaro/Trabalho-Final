package br.ufmg.coltec.journal.business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParserUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Converte uma String no formato dd/MM/yyyy para LocalDate.
     * Retorna null se a string for vazia ou nula (útil para filtros opcionais).
     * Lança IllegalArgumentException se o formato for inválido.
     */
    public static LocalDate parse(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido! Use o formato dd/MM/yyyy (Ex: 05/06/2026).");
        }
    }
}