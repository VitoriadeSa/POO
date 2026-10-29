package livros.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Métodos auxiliares para converter datas (LocalDate) de e para texto no formato dd/MM/aaaa.
 */
public final class Datas {
    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Datas() {
    }

    public static String formatar(LocalDate data) {
        return data == null ? "" : data.format(FORMATO);
    }

    /** Devolve a data correspondente ao texto, ou null se o texto não for uma data válida. */
    public static LocalDate converter(String texto) {
        try {
            return LocalDate.parse(texto.trim(), FORMATO);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
