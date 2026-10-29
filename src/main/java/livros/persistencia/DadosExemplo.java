package livros.persistencia;

import java.time.LocalDate;

import livros.modelo.Amigo;
import livros.modelo.BandaDesenhada;
import livros.modelo.Biblioteca;
import livros.modelo.BibliotecaException;
import livros.modelo.Categoria;
import livros.modelo.FormaAquisicao;
import livros.modelo.Livro;
import livros.modelo.Localizacao;

/**
 * Cria uma biblioteca com alguns dados para a aplicação não começar vazia.
 */
final class DadosExemplo {

    private DadosExemplo() {
    }

    static Biblioteca criar() {
        Biblioteca b = new Biblioteca();
        try {
            Categoria romance = b.adicionarCategoria("Romance", "Ficção narrativa");
            Categoria ficcao = b.adicionarCategoria("Ficção científica", "Futuro, ciência e tecnologia");
            Categoria policial = b.adicionarCategoria("Policial", "Crimes e mistério");
            Categoria bd = b.adicionarCategoria("Banda desenhada", "Histórias aos quadradinhos");
            Categoria tecnico = b.adicionarCategoria("Técnico", "Informática, ciência e manuais");

            Localizacao sala1 = b.adicionarLocalizacao("Sala", "Estante grande", 1);
            Localizacao sala2 = b.adicionarLocalizacao("Sala", "Estante grande", 2);
            Localizacao quarto = b.adicionarLocalizacao("Quarto", "Mesa de cabeceira", 1);
            Localizacao escritorio = b.adicionarLocalizacao("Escritório", "Estante branca", 3);

            b.adicionarLivro(new Livro("Os Maias", "Eça de Queirós", "Livros do Brasil", "9789723816882",
                    1888, 712, "Português", romance, sala1,
                    LocalDate.of(2023, 3, 12), FormaAquisicao.COMPRA, 15.50));
            b.adicionarLivro(new Livro("Ensaio sobre a Cegueira", "José Saramago", "Porto Editora", "9789720049405",
                    1995, 312, "Português", romance, sala1,
                    LocalDate.of(2024, 12, 25), FormaAquisicao.OFERTA, 17.70));
            b.adicionarLivro(new Livro("Duna", "Frank Herbert", "Saída de Emergência", "9789896376466",
                    1965, 600, "Português", ficcao, sala2,
                    LocalDate.of(2025, 6, 2), FormaAquisicao.COMPRA, 22.00));
            b.adicionarLivro(new Livro("Um Crime no Expresso do Oriente", "Agatha Christie", "Asa", "9789892325797",
                    1934, 240, "Português", policial, quarto,
                    LocalDate.of(2025, 9, 18), FormaAquisicao.TROCA, 0));
            b.adicionarLivro(new Livro("Java: Como Programar", "Paul Deitel", "Pearson", "9788543004792",
                    2016, 968, "Português", tecnico, escritorio,
                    LocalDate.of(2026, 9, 20), FormaAquisicao.COMPRA, 59.90));
            b.adicionarLivro(new BandaDesenhada("Astérix, o Gaulês", "René Goscinny", "Albert Uderzo", "Astérix", 1,
                    "Asa", "9789724141235", 1961, 48, "Português", bd, quarto,
                    LocalDate.of(2022, 7, 30), FormaAquisicao.COMPRA, 9.95));

            Amigo ana = b.adicionarAmigo("Ana Silva", "912345678", "ana.silva@exemplo.pt");
            b.adicionarAmigo("João Costa", "934567890", "joao.costa@exemplo.pt");

            b.emprestar(b.procurarLivro(3), ana, LocalDate.now().minusDays(20), LocalDate.now().minusDays(5));
        } catch (BibliotecaException e) {
            // Os dados de exemplo são válidos, por isso isto não deve acontecer.
            System.err.println("Erro ao criar dados de exemplo: " + e.getMessage());
        }
        return b;
    }
}
