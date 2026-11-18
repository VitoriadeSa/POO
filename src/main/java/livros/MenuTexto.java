package livros;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuTexto {
    private Biblioteca biblioteca;
    private Scanner sc = new Scanner(System.in);

    public MenuTexto(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public void executar() {
        int opcao;
        do {
            System.out.println("\n=== OS MEUS LIVROS ===");
            System.out.println("1 - Listar livros");
            System.out.println("2 - Adicionar livro (aquisição)");
            System.out.println("3 - Remover livro");
            System.out.println("4 - Pesquisar");
            System.out.println("5 - Emprestar livro");
            System.out.println("6 - Devolver livro");
            System.out.println("7 - Listar empréstimos");
            System.out.println("8 - Registar desaparecimento");
            System.out.println("9 - Registar recuperação");
            System.out.println("10 - Estatísticas");
            System.out.println("0 - Guardar e sair");
            opcao = lerInt("Opção: ");
            try {
                switch (opcao) {
                    case 1 -> listar(biblioteca.getLivros());
                    case 2 -> adicionar();
                    case 3 -> biblioteca.remover(escolherLivro());
                    case 4 -> listar(biblioteca.pesquisar(ler("Texto a procurar: ")));
                    case 5 -> emprestar();
                    case 6 -> biblioteca.devolver(escolherLivro());
                    case 7 -> {
                        for (Emprestimo e : biblioteca.getEmprestimos()) System.out.println(e);
                    }
                    case 8 -> biblioteca.desaparecer(escolherLivro());
                    case 9 -> biblioteca.recuperar(escolherLivro());
                    case 10 -> System.out.println(biblioteca.estatisticas());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);

        try {
            biblioteca.guardar();
            System.out.println("Dados guardados.");
        } catch (Exception e) {
            System.out.println("Erro ao guardar: " + e.getMessage());
        }
    }

    private void listar(ArrayList<Livro> lista) {
        if (lista.isEmpty()) System.out.println("Não há livros.");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + " - " + lista.get(i));
        }
    }

    private void adicionar() {
        String tipo = ler("Livro ou BD? (L/B): ");
        String titulo = ler("Título: ");
        String autor = ler("Autor: ");
        String categoria = ler("Categoria: ");
        String localizacao = ler("Localização: ");
        LocalDate data = lerData("Data de aquisição (aaaa-mm-dd): ");
        double preco = lerDouble("Preço: ");
        if (tipo.equalsIgnoreCase("B")) {
            String ilustrador = ler("Ilustrador: ");
            biblioteca.adicionar(new BandaDesenhada(titulo, autor, ilustrador, categoria, localizacao, data, preco));
        } else {
            biblioteca.adicionar(new Livro(titulo, autor, categoria, localizacao, data, preco));
        }
        System.out.println("Livro adicionado.");
    }

    private void emprestar() {
        Livro livro = escolherLivro();
        String amigo = ler("Nome do amigo: ");
        LocalDate limite = lerData("Devolver até (aaaa-mm-dd): ");
        biblioteca.emprestar(livro, amigo, limite);
        System.out.println("Empréstimo registado.");
    }

    private Livro escolherLivro() {
        listar(biblioteca.getLivros());
        int n = lerInt("Número do livro: ");
        if (n < 1 || n > biblioteca.getLivros().size()) {
            throw new IllegalArgumentException("Número inválido.");
        }
        return biblioteca.getLivros().get(n - 1);
    }

    // ---------- leitura do teclado ----------

    private String ler(String pergunta) {
        System.out.print(pergunta);
        return sc.nextLine().trim();
    }

    private int lerInt(String pergunta) {
        while (true) {
            try {
                return Integer.parseInt(ler(pergunta));
            } catch (NumberFormatException e) {
                System.out.println("Escreva um número.");
            }
        }
    }

    private double lerDouble(String pergunta) {
        while (true) {
            try {
                return Double.parseDouble(ler(pergunta).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Escreva um número.");
            }
        }
    }

    private LocalDate lerData(String pergunta) {
        while (true) {
            try {
                return LocalDate.parse(ler(pergunta));
            } catch (Exception e) {
                System.out.println("Data inválida. Exemplo: 2026-11-18");
            }
        }
    }
}
