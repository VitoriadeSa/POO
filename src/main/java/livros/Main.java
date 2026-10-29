/*
 * G99 - Programação Orientada a Objetos 2026/2027 - Trabalho Prático
 * Tema T03 - Os meus livros
 *
 * Elementos do grupo:
 *   - aNNNNN  Nome Apelido
 *   - aNNNNN  Nome Apelido
 *   - aNNNNN  Nome Apelido
 *   - aNNNNN  Nome Apelido
 */
package livros;

import java.io.IOException;

import javafx.application.Application;
import livros.fx.AppFX;
import livros.modelo.Biblioteca;
import livros.persistencia.Persistencia;
import livros.texto.MenuTexto;

/**
 * Ponto de entrada da aplicação.
 * Sem argumentos abre a interface gráfica (JavaFX); com o argumento "texto" abre a interface de texto.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("texto")) {
            try {
                Biblioteca biblioteca = Persistencia.carregarOuCriar(Persistencia.FICHEIRO);
                new MenuTexto(biblioteca).executar();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Não foi possível ler " + Persistencia.FICHEIRO + ": " + e.getMessage());
            }
        } else {
            Application.launch(AppFX.class, args);
        }
    }
}
