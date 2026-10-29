package livros.texto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import livros.modelo.Amigo;
import livros.modelo.BandaDesenhada;
import livros.modelo.Biblioteca;
import livros.modelo.BibliotecaException;
import livros.modelo.Categoria;
import livros.modelo.Datas;
import livros.modelo.Desaparecimento;
import livros.modelo.Emprestimo;
import livros.modelo.EstadoItem;
import livros.modelo.FormaAquisicao;
import livros.modelo.Livro;
import livros.modelo.Localizacao;
import livros.persistencia.Persistencia;

/**
 * Interface de texto da aplicação.
 */
public class MenuTexto {
    private final Biblioteca biblioteca;
    private final Leitor leitor;

    public MenuTexto(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        this.leitor = new Leitor(new Scanner(System.in));
    }

    public void executar() {
        mostrarAvisoAtrasos();
        int opcao;
        do {
            System.out.println();
            System.out.println("===== OS MEUS LIVROS =====");
            System.out.println("1 - Livros");
            System.out.println("2 - Categorias e localizações");
            System.out.println("3 - Amigos");
            System.out.println("4 - Empréstimos");
            System.out.println("5 - Aquisições e desaparecimentos");
            System.out.println("6 - Estatísticas");
            System.out.println("7 - Guardar");
            System.out.println("0 - Guardar e sair");
            opcao = leitor.lerInteiro("Opção: ", 0, 7);
            switch (opcao) {
                case 1 -> menuLivros();
                case 2 -> menuCategoriasLocalizacoes();
                case 3 -> menuAmigos();
                case 4 -> menuEmprestimos();
                case 5 -> menuAquisicoesDesaparecimentos();
                case 6 -> System.out.println("\n" + biblioteca.getResumoEstatisticas());
                case 7 -> guardar();
                default -> { }
            }
        } while (opcao != 0);
        guardar();
        System.out.println("Até à próxima!");
    }

    private void guardar() {
        try {
            Persistencia.guardar(biblioteca, Persistencia.FICHEIRO);
            System.out.println("Dados guardados em " + Persistencia.FICHEIRO.getName() + ".");
        } catch (IOException e) {
            System.out.println("Erro ao guardar: " + e.getMessage());
        }
    }

    private void mostrarAvisoAtrasos() {
        List<Emprestimo> atrasados = biblioteca.getEmprestimosAtrasados(LocalDate.now());
        if (!atrasados.isEmpty()) {
            System.out.println("ATENÇÃO: há " + atrasados.size() + " empréstimo(s) atrasado(s):");
            for (Emprestimo e : atrasados) {
                System.out.println("  " + e);
            }
        }
    }

    // ================================================================== Livros

    private void menuLivros() {
        int opcao;
        do {
            System.out.println("\n--- Livros ---");
            System.out.println("1 - Listar todos");
            System.out.println("2 - Ver detalhes de um livro");
            System.out.println("3 - Pesquisar");
            System.out.println("4 - Adicionar livro / banda desenhada");
            System.out.println("5 - Editar livro");
            System.out.println("6 - Remover livro");
            System.out.println("0 - Voltar");
            opcao = leitor.lerInteiro("Opção: ", 0, 6);
            switch (opcao) {
                case 1 -> listar(biblioteca.getLivros());
                case 2 -> verDetalhes();
                case 3 -> pesquisar();
                case 4 -> adicionarLivro();
                case 5 -> editarLivro();
                case 6 -> removerLivro();
                default -> { }
            }
        } while (opcao != 0);
    }

    private void listar(List<Livro> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }
        System.out.printf("%-4s %-16s %-35s %-22s %-18s %s%n", "Nº", "Tipo", "Título", "Autor", "Categoria", "Estado");
        for (Livro l : lista) {
            System.out.printf("%-4d %-16s %-35s %-22s %-18s %s%n", l.getId(), l.getTipo(),
                    cortar(l.getTitulo(), 35), cortar(l.getAutor(), 22),
                    l.getCategoria() == null ? "-" : cortar(l.getCategoria().getNome(), 18), l.getEstado());
        }
    }

    private static String cortar(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    /** Pede o número de um livro e devolve-o (ou null se não existir). */
    private Livro pedirLivro() {
        int id = leitor.lerInteiro("Número do livro: ", 1, Integer.MAX_VALUE);
        Livro l = biblioteca.procurarLivro(id);
        if (l == null) {
            System.out.println("Não existe nenhum livro com esse número.");
        }
        return l;
    }

    private void verDetalhes() {
        Livro l = pedirLivro();
        if (l == null) {
            return;
        }
        System.out.println();
        System.out.print(l.getDescricaoDetalhada());
        List<Emprestimo> historico = biblioteca.getEmprestimosDoLivro(l);
        if (!historico.isEmpty()) {
            System.out.println("Histórico de empréstimos:");
            for (Emprestimo e : historico) {
                System.out.println("  " + e);
            }
        }
    }

    private void pesquisar() {
        String texto = leitor.lerTexto("Texto (título, autor, editora, ISBN; Enter = qualquer): ");
        Categoria cat = leitor.escolher("Categoria:", biblioteca.getCategorias());
        Localizacao loc = leitor.escolher("Localização:", biblioteca.getLocalizacoes());
        EstadoItem estado = leitor.escolher("Estado:", List.of(EstadoItem.values()));
        listar(biblioteca.pesquisar(texto, cat, loc, estado));
    }

    private void adicionarLivro() {
        boolean bd = leitor.lerInteiro("Tipo (1 = Livro, 2 = Banda desenhada): ", 1, 2) == 2;
        String titulo = leitor.lerTextoObrigatorio("Título: ");
        String autor = leitor.lerTextoObrigatorio("Autor: ");
        String editora = leitor.lerTexto("Editora: ");
        String isbn = leitor.lerTexto("ISBN: ");
        int ano = leitor.lerInteiro("Ano de publicação (0 = desconhecido): ", 0, LocalDate.now().getYear());
        int paginas = leitor.lerInteiro("Nº de páginas (0 = desconhecido): ", 0, 100000);
        String idioma = leitor.lerTexto("Idioma: ");
        Categoria cat = leitor.escolher("Categoria:", biblioteca.getCategorias());
        Localizacao loc = leitor.escolher("Localização:", biblioteca.getLocalizacoes());
        LocalDate data = leitor.lerData("Data de aquisição", LocalDate.now());
        FormaAquisicao forma = leitor.escolher("Forma de aquisição:", List.of(FormaAquisicao.values()));
        double preco = leitor.lerDecimal("Preço (€): ");

        Livro livro;
        if (bd) {
            String ilustrador = leitor.lerTexto("Ilustrador: ");
            String serie = leitor.lerTexto("Série: ");
            int volume = serie.isEmpty() ? 0 : leitor.lerInteiro("Volume: ", 0, 10000);
            livro = new BandaDesenhada(titulo, autor, ilustrador, serie, volume, editora, isbn, ano, paginas,
                    idioma, cat, loc, data, forma, preco);
        } else {
            livro = new Livro(titulo, autor, editora, isbn, ano, paginas, idioma, cat, loc, data, forma, preco);
        }
        livro.setNotas(leitor.lerTexto("Notas: "));
        try {
            biblioteca.adicionarLivro(livro);
            System.out.println("Livro adicionado com o número " + livro.getId() + ".");
        } catch (BibliotecaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void editarLivro() {
        Livro l = pedirLivro();
        if (l == null) {
            return;
        }
        System.out.println("Deixe em branco para manter o valor atual.");
        String s = leitor.lerTexto("Título [" + l.getTitulo() + "]: ");
        if (!s.isEmpty()) {
            l.setTitulo(s);
        }
        s = leitor.lerTexto("Autor [" + l.getAutor() + "]: ");
        if (!s.isEmpty()) {
            l.setAutor(s);
        }
        s = leitor.lerTexto("Editora [" + l.getEditora() + "]: ");
        if (!s.isEmpty()) {
            l.setEditora(s);
        }
        s = leitor.lerTexto("ISBN [" + l.getIsbn() + "]: ");
        if (!s.isEmpty()) {
            l.setIsbn(s);
        }
        s = leitor.lerTexto("Notas [" + l.getNotas() + "]: ");
        if (!s.isEmpty()) {
            l.setNotas(s);
        }
        if (leitor.confirmar("Alterar categoria?")) {
            l.setCategoria(leitor.escolher("Categoria:", biblioteca.getCategorias()));
        }
        if (leitor.confirmar("Alterar localização?")) {
            l.setLocalizacao(leitor.escolher("Localização:", biblioteca.getLocalizacoes()));
        }
        System.out.println("Livro atualizado.");
    }

    private void removerLivro() {
        Livro l = pedirLivro();
        if (l == null || !leitor.confirmar("Remover \"" + l.getTitulo() + "\"?")) {
            return;
        }
        try {
            biblioteca.removerLivro(l);
            System.out.println("Livro removido.");
        } catch (BibliotecaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // ================================================ Categorias e localizações

    private void menuCategoriasLocalizacoes() {
        int opcao;
        do {
            System.out.println("\n--- Categorias e localizações ---");
            System.out.println("1 - Listar categorias");
            System.out.println("2 - Adicionar categoria");
            System.out.println("3 - Remover categoria");
            System.out.println("4 - Listar localizações");
            System.out.println("5 - Adicionar localização");
            System.out.println("6 - Remover localização");
            System.out.println("0 - Voltar");
            opcao = leitor.lerInteiro("Opção: ", 0, 6);
            try {
                switch (opcao) {
                    case 1 -> {
                        for (Categoria c : biblioteca.getCategorias()) {
                            System.out.println("  " + c.getNome() + " - " + c.getDescricao());
                        }
                    }
                    case 2 -> {
                        biblioteca.adicionarCategoria(leitor.lerTextoObrigatorio("Nome: "),
                                leitor.lerTexto("Descrição: "));
                        System.out.println("Categoria adicionada.");
                    }
                    case 3 -> {
                        Categoria c = leitor.escolher("Categoria a remover:", biblioteca.getCategorias());
                        if (c != null) {
                            biblioteca.removerCategoria(c);
                            System.out.println("Categoria removida.");
                        }
                    }
                    case 4 -> {
                        for (Localizacao loc : biblioteca.getLocalizacoes()) {
                            System.out.println("  " + loc);
                        }
                    }
                    case 5 -> {
                        biblioteca.adicionarLocalizacao(leitor.lerTextoObrigatorio("Divisão: "),
                                leitor.lerTextoObrigatorio("Estante: "),
                                leitor.lerInteiro("Prateleira: ", 1, 100));
                        System.out.println("Localização adicionada.");
                    }
                    case 6 -> {
                        Localizacao loc = leitor.escolher("Localização a remover:", biblioteca.getLocalizacoes());
                        if (loc != null) {
                            biblioteca.removerLocalizacao(loc);
                            System.out.println("Localização removida.");
                        }
                    }
                    default -> { }
                }
            } catch (BibliotecaException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    // ================================================================== Amigos

    private void menuAmigos() {
        int opcao;
        do {
            System.out.println("\n--- Amigos ---");
            System.out.println("1 - Listar amigos");
            System.out.println("2 - Adicionar amigo");
            System.out.println("3 - Remover amigo");
            System.out.println("4 - Empréstimos de um amigo");
            System.out.println("0 - Voltar");
            opcao = leitor.lerInteiro("Opção: ", 0, 4);
            try {
                switch (opcao) {
                    case 1 -> {
                        for (Amigo a : biblioteca.getAmigos()) {
                            System.out.println("  " + a.getNome() + " | " + a.getTelefone() + " | " + a.getEmail());
                        }
                    }
                    case 2 -> {
                        biblioteca.adicionarAmigo(leitor.lerTextoObrigatorio("Nome: "),
                                leitor.lerTexto("Telefone: "), leitor.lerTexto("Email: "));
                        System.out.println("Amigo adicionado.");
                    }
                    case 3 -> {
                        Amigo a = leitor.escolher("Amigo a remover:", biblioteca.getAmigos());
                        if (a != null) {
                            biblioteca.removerAmigo(a);
                            System.out.println("Amigo removido.");
                        }
                    }
                    case 4 -> {
                        Amigo a = leitor.escolher("Amigo:", biblioteca.getAmigos());
                        if (a != null) {
                            for (Emprestimo e : biblioteca.getEmprestimosDe(a)) {
                                System.out.println("  " + e);
                            }
                        }
                    }
                    default -> { }
                }
            } catch (BibliotecaException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    // ============================================================= Empréstimos

    private void menuEmprestimos() {
        int opcao;
        do {
            System.out.println("\n--- Empréstimos ---");
            System.out.println("1 - Emprestar livro");
            System.out.println("2 - Registar devolução");
            System.out.println("3 - Empréstimos ativos");
            System.out.println("4 - Empréstimos atrasados");
            System.out.println("5 - Histórico de todos os empréstimos");
            System.out.println("0 - Voltar");
            opcao = leitor.lerInteiro("Opção: ", 0, 5);
            try {
                switch (opcao) {
                    case 1 -> emprestar();
                    case 2 -> {
                        Emprestimo e = leitor.escolher("Empréstimo:", biblioteca.getEmprestimosAtivos());
                        if (e != null) {
                            biblioteca.devolver(e, leitor.lerData("Data de devolução", LocalDate.now()));
                            System.out.println("Devolução registada.");
                        }
                    }
                    case 3 -> imprimir(biblioteca.getEmprestimosAtivos());
                    case 4 -> imprimir(biblioteca.getEmprestimosAtrasados(LocalDate.now()));
                    case 5 -> imprimir(biblioteca.getEmprestimos());
                    default -> { }
                }
            } catch (BibliotecaException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void emprestar() throws BibliotecaException {
        Livro l = leitor.escolher("Livro:", biblioteca.pesquisar(null, null, null, EstadoItem.DISPONIVEL));
        if (l == null) {
            return;
        }
        Amigo a = leitor.escolher("Amigo:", biblioteca.getAmigos());
        if (a == null) {
            return;
        }
        LocalDate data = leitor.lerData("Data do empréstimo", LocalDate.now());
        LocalDate prevista = leitor.lerData("Data prevista de devolução", data.plusWeeks(2));
        biblioteca.emprestar(l, a, data, prevista);
        System.out.println("Empréstimo registado.");
    }

    private static void imprimir(List<?> lista) {
        if (lista.isEmpty()) {
            System.out.println("(nenhum)");
        }
        for (Object o : lista) {
            System.out.println("  " + o);
        }
    }

    // ============================================ Aquisições e desaparecimentos

    private void menuAquisicoesDesaparecimentos() {
        int opcao;
        do {
            System.out.println("\n--- Aquisições e desaparecimentos ---");
            System.out.println("1 - Aquisições num período");
            System.out.println("2 - Registar desaparecimento");
            System.out.println("3 - Registar recuperação de livro desaparecido");
            System.out.println("4 - Listar desaparecimentos");
            System.out.println("0 - Voltar");
            opcao = leitor.lerInteiro("Opção: ", 0, 4);
            try {
                switch (opcao) {
                    case 1 -> {
                        LocalDate inicio = leitor.lerData("Desde", LocalDate.now().withDayOfYear(1));
                        LocalDate fim = leitor.lerData("Até", LocalDate.now());
                        for (Livro l : biblioteca.getAquisicoesEntre(inicio, fim)) {
                            System.out.printf("  %s  %-10s %s%n", Datas.formatar(l.getDataAquisicao()),
                                    l.getFormaAquisicao(), l);
                        }
                    }
                    case 2 -> {
                        Livro l = pedirLivro();
                        if (l != null) {
                            LocalDate data = leitor.lerData("Data", LocalDate.now());
                            biblioteca.registarDesaparecimento(l, data, leitor.lerTexto("Descrição: "));
                            System.out.println("Desaparecimento registado.");
                        }
                    }
                    case 3 -> {
                        Livro l = leitor.escolher("Livro recuperado:",
                                biblioteca.pesquisar(null, null, null, EstadoItem.DESAPARECIDO));
                        if (l != null) {
                            biblioteca.registarRecuperacao(l, leitor.lerData("Data", LocalDate.now()));
                            System.out.println("Recuperação registada.");
                        }
                    }
                    case 4 -> {
                        List<Desaparecimento> lista = biblioteca.getDesaparecimentos();
                        imprimir(lista);
                    }
                    default -> { }
                }
            } catch (BibliotecaException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }
}
