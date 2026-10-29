package livros.texto;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import livros.modelo.Datas;

/**
 * Métodos auxiliares para ler dados do teclado, repetindo a pergunta até o valor ser válido.
 */
public class Leitor {
    private final Scanner scanner;

    public Leitor(Scanner scanner) {
        this.scanner = scanner;
    }

    public String lerTexto(String pergunta) {
        System.out.print(pergunta);
        return scanner.nextLine().trim();
    }

    public String lerTextoObrigatorio(String pergunta) {
        String s;
        do {
            s = lerTexto(pergunta);
        } while (s.isEmpty());
        return s;
    }

    public int lerInteiro(String pergunta, int min, int max) {
        while (true) {
            String s = lerTexto(pergunta);
            try {
                int n = Integer.parseInt(s);
                if (n >= min && n <= max) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // pergunta de novo
            }
            System.out.println("Introduza um número entre " + min + " e " + max + ".");
        }
    }

    public double lerDecimal(String pergunta) {
        while (true) {
            String s = lerTexto(pergunta).replace(',', '.');
            try {
                double d = Double.parseDouble(s);
                if (d >= 0) {
                    return d;
                }
            } catch (NumberFormatException e) {
                // pergunta de novo
            }
            System.out.println("Introduza um valor numérico positivo.");
        }
    }

    /** Lê uma data no formato dd/MM/aaaa. Se o utilizador não escrever nada, devolve a data por omissão. */
    public LocalDate lerData(String pergunta, LocalDate omissao) {
        while (true) {
            String s = lerTexto(pergunta + " (dd/mm/aaaa, Enter = " + Datas.formatar(omissao) + "): ");
            if (s.isEmpty()) {
                return omissao;
            }
            LocalDate d = Datas.converter(s);
            if (d != null) {
                return d;
            }
            System.out.println("Data inválida.");
        }
    }

    public boolean confirmar(String pergunta) {
        String s = lerTexto(pergunta + " (s/n): ");
        return s.equalsIgnoreCase("s") || s.equalsIgnoreCase("sim");
    }

    /**
     * Mostra uma lista numerada e devolve o elemento escolhido.
     * Devolve null se a lista estiver vazia ou se o utilizador escolher 0.
     */
    public <T> T escolher(String titulo, List<T> opcoes) {
        if (opcoes.isEmpty()) {
            System.out.println("(lista vazia)");
            return null;
        }
        System.out.println(titulo);
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println("  " + (i + 1) + " - " + opcoes.get(i));
        }
        int n = lerInteiro("Escolha (0 = nenhum): ", 0, opcoes.size());
        return n == 0 ? null : opcoes.get(n - 1);
    }
}
