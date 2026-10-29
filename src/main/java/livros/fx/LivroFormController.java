package livros.fx;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import livros.modelo.BandaDesenhada;
import livros.modelo.Biblioteca;
import livros.modelo.BibliotecaException;
import livros.modelo.Categoria;
import livros.modelo.FormaAquisicao;
import livros.modelo.Livro;
import livros.modelo.Localizacao;

/**
 * Controlador da janela LivroForm.fxml, usada para adicionar um livro novo ou editar um existente.
 */
public class LivroFormController {
    private static final String TIPO_LIVRO = "Livro";
    private static final String TIPO_BD = "Banda desenhada";

    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtEditora;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtIdioma;
    @FXML private TextField txtAno;
    @FXML private TextField txtPaginas;
    @FXML private ComboBox<Categoria> cbCategoria;
    @FXML private ComboBox<Localizacao> cbLocalizacao;
    @FXML private DatePicker dpAquisicao;
    @FXML private ComboBox<FormaAquisicao> cbForma;
    @FXML private TextField txtPreco;
    @FXML private Label lblIlustrador;
    @FXML private TextField txtIlustrador;
    @FXML private Label lblSerie;
    @FXML private TextField txtSerie;
    @FXML private TextField txtVolume;
    @FXML private TextArea txtNotas;
    @FXML private Label lblErro;

    private Biblioteca biblioteca;
    private Livro livro;          // null quando se está a criar um livro novo
    private boolean guardado = false;

    @FXML
    private void initialize() {
        cbTipo.getItems().addAll(TIPO_LIVRO, TIPO_BD);
        cbTipo.setValue(TIPO_LIVRO);
        cbForma.getItems().addAll(FormaAquisicao.values());
        cbForma.setValue(FormaAquisicao.COMPRA);
        dpAquisicao.setValue(LocalDate.now());
        txtIdioma.setText("Português");
        txtPreco.setText("0");
        tipoAlterado();
    }

    /** Prepara o formulário. Se livro for null, o formulário serve para criar um livro novo. */
    public void inicializar(Biblioteca biblioteca, Livro livro) {
        this.biblioteca = biblioteca;
        this.livro = livro;
        cbCategoria.getItems().setAll(biblioteca.getCategorias());
        cbLocalizacao.getItems().setAll(biblioteca.getLocalizacoes());
        if (livro == null) {
            return;
        }
        // Edição: preencher os campos com os dados atuais. O tipo não pode ser alterado.
        cbTipo.setValue(livro instanceof BandaDesenhada ? TIPO_BD : TIPO_LIVRO);
        cbTipo.setDisable(true);
        txtTitulo.setText(livro.getTitulo());
        txtAutor.setText(livro.getAutor());
        txtEditora.setText(livro.getEditora());
        txtIsbn.setText(livro.getIsbn());
        txtIdioma.setText(livro.getIdioma());
        txtAno.setText(livro.getAnoPublicacao() > 0 ? String.valueOf(livro.getAnoPublicacao()) : "");
        txtPaginas.setText(livro.getNumPaginas() > 0 ? String.valueOf(livro.getNumPaginas()) : "");
        cbCategoria.setValue(livro.getCategoria());
        cbLocalizacao.setValue(livro.getLocalizacao());
        dpAquisicao.setValue(livro.getDataAquisicao());
        cbForma.setValue(livro.getFormaAquisicao());
        txtPreco.setText(String.valueOf(livro.getPreco()));
        txtNotas.setText(livro.getNotas());
        if (livro instanceof BandaDesenhada bd) {
            txtIlustrador.setText(bd.getIlustrador());
            txtSerie.setText(bd.getSerie());
            txtVolume.setText(bd.getVolume() > 0 ? String.valueOf(bd.getVolume()) : "");
        }
        tipoAlterado();
    }

    public boolean isGuardado() {
        return guardado;
    }

    /** Os campos de banda desenhada só ficam ativos quando esse tipo está escolhido. */
    @FXML
    private void tipoAlterado() {
        boolean bd = TIPO_BD.equals(cbTipo.getValue());
        lblIlustrador.setDisable(!bd);
        txtIlustrador.setDisable(!bd);
        lblSerie.setDisable(!bd);
        txtSerie.setDisable(!bd);
        txtVolume.setDisable(!bd);
    }

    @FXML
    private void guardar() {
        try {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            if (titulo.isEmpty() || autor.isEmpty()) {
                throw new BibliotecaException("O título e o autor são obrigatórios.");
            }
            int ano = lerInteiro(txtAno, "O ano de publicação");
            if (ano > LocalDate.now().getYear()) {
                throw new BibliotecaException("O ano de publicação não pode ser no futuro.");
            }
            int paginas = lerInteiro(txtPaginas, "O número de páginas");
            int volume = lerInteiro(txtVolume, "O volume");
            double preco = lerPreco();
            LocalDate data = dpAquisicao.getValue();

            if (livro == null) {
                Livro novo;
                if (TIPO_BD.equals(cbTipo.getValue())) {
                    novo = new BandaDesenhada(titulo, autor, txtIlustrador.getText().trim(), txtSerie.getText().trim(),
                            volume, txtEditora.getText().trim(), txtIsbn.getText().trim(), ano, paginas,
                            txtIdioma.getText().trim(), cbCategoria.getValue(), cbLocalizacao.getValue(),
                            data, cbForma.getValue(), preco);
                } else {
                    novo = new Livro(titulo, autor, txtEditora.getText().trim(), txtIsbn.getText().trim(), ano,
                            paginas, txtIdioma.getText().trim(), cbCategoria.getValue(), cbLocalizacao.getValue(),
                            data, cbForma.getValue(), preco);
                }
                novo.setNotas(txtNotas.getText());
                biblioteca.adicionarLivro(novo);
            } else {
                if (data != null && data.isAfter(LocalDate.now())) {
                    throw new BibliotecaException("A data de aquisição não pode ser no futuro.");
                }
                livro.setTitulo(titulo);
                livro.setAutor(autor);
                livro.setEditora(txtEditora.getText().trim());
                livro.setIsbn(txtIsbn.getText().trim());
                livro.setIdioma(txtIdioma.getText().trim());
                livro.setAnoPublicacao(ano);
                livro.setNumPaginas(paginas);
                livro.setCategoria(cbCategoria.getValue());
                livro.setLocalizacao(cbLocalizacao.getValue());
                livro.setDataAquisicao(data);
                livro.setFormaAquisicao(cbForma.getValue());
                livro.setPreco(preco);
                livro.setNotas(txtNotas.getText());
                if (livro instanceof BandaDesenhada bd) {
                    bd.setIlustrador(txtIlustrador.getText().trim());
                    bd.setSerie(txtSerie.getText().trim());
                    bd.setVolume(volume);
                }
            }
            guardado = true;
            fechar();
        } catch (BibliotecaException e) {
            lblErro.setText(e.getMessage());
        }
    }

    /** Lê um número inteiro de um campo; um campo vazio vale 0. */
    private int lerInteiro(TextField campo, String nome) throws BibliotecaException {
        String s = campo.getText().trim();
        if (s.isEmpty() || campo.isDisabled()) {
            return 0;
        }
        try {
            int n = Integer.parseInt(s);
            if (n < 0) {
                throw new NumberFormatException();
            }
            return n;
        } catch (NumberFormatException e) {
            throw new BibliotecaException(nome + " tem de ser um número inteiro positivo.");
        }
    }

    private double lerPreco() throws BibliotecaException {
        String s = txtPreco.getText().trim().replace(',', '.');
        if (s.isEmpty()) {
            return 0;
        }
        try {
            double d = Double.parseDouble(s);
            if (d < 0) {
                throw new NumberFormatException();
            }
            return d;
        } catch (NumberFormatException e) {
            throw new BibliotecaException("O preço tem de ser um valor positivo.");
        }
    }

    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        ((Stage) txtTitulo.getScene().getWindow()).close();
    }
}
