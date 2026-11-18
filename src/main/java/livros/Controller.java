package livros;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller {
    private Biblioteca biblioteca;

    @FXML private TextField txtPesquisa;
    @FXML private ListView<Livro> listaLivros;
    @FXML private ListView<Emprestimo> listaEmprestimos;
    @FXML private Label lblEstatisticas;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtIlustrador;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtLocalizacao;
    @FXML private DatePicker dpAquisicao;
    @FXML private TextField txtPreco;

    @FXML private TextField txtAmigo;
    @FXML private DatePicker dpLimite;

    @FXML
    private void initialize() {
        try {
            biblioteca = Biblioteca.carregar();
        } catch (Exception e) {
            erro("Não foi possível ler os dados: " + e.getMessage());
            biblioteca = new Biblioteca();
        }
        dpAquisicao.setValue(LocalDate.now());
        dpLimite.setValue(LocalDate.now().plusWeeks(2));
        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> atualizar());
        atualizar();
    }

    private void atualizar() {
        listaLivros.getItems().setAll(biblioteca.pesquisar(txtPesquisa.getText()));
        listaEmprestimos.getItems().setAll(biblioteca.getEmprestimos());
        lblEstatisticas.setText(biblioteca.estatisticas());
    }

    public void guardar() {
        try {
            biblioteca.guardar();
        } catch (Exception e) {
            erro("Erro ao guardar: " + e.getMessage());
        }
    }

    private void erro(String mensagem) {
        new Alert(Alert.AlertType.ERROR, mensagem).showAndWait();
    }

    /** Devolve o livro selecionado ou lança erro se não houver nenhum. */
    private Livro selecionado() {
        Livro l = listaLivros.getSelectionModel().getSelectedItem();
        if (l == null) {
            throw new IllegalArgumentException("Selecione um livro na lista.");
        }
        return l;
    }

    /** Executa uma ação; se correr bem grava e atualiza, senão mostra o erro. */
    private void fazer(Runnable acao) {
        try {
            acao.run();
            guardar();
            atualizar();
        } catch (IllegalArgumentException e) {
            erro(e.getMessage());
        }
    }

    @FXML
    private void adicionar() {
        fazer(() -> {
            double preco;
            try {
                preco = Double.parseDouble(txtPreco.getText().replace(',', '.'));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Preço inválido.");
            }
            Livro l;
            if (txtIlustrador.getText().isBlank()) {
                l = new Livro(txtTitulo.getText(), txtAutor.getText(), txtCategoria.getText(),
                        txtLocalizacao.getText(), dpAquisicao.getValue(), preco);
            } else {
                l = new BandaDesenhada(txtTitulo.getText(), txtAutor.getText(), txtIlustrador.getText(),
                        txtCategoria.getText(), txtLocalizacao.getText(), dpAquisicao.getValue(), preco);
            }
            biblioteca.adicionar(l);
            txtTitulo.clear();
            txtAutor.clear();
            txtIlustrador.clear();
        });
    }

    @FXML
    private void remover() {
        fazer(() -> biblioteca.remover(selecionado()));
    }

    @FXML
    private void emprestar() {
        fazer(() -> biblioteca.emprestar(selecionado(), txtAmigo.getText(), dpLimite.getValue()));
    }

    @FXML
    private void devolver() {
        fazer(() -> biblioteca.devolver(selecionado()));
    }

    @FXML
    private void desaparecer() {
        fazer(() -> biblioteca.desaparecer(selecionado()));
    }

    @FXML
    private void recuperar() {
        fazer(() -> biblioteca.recuperar(selecionado()));
    }
}
