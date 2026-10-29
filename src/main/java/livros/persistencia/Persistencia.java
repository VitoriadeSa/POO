package livros.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import livros.modelo.Biblioteca;

/**
 * Grava e lê a biblioteca num ficheiro, usando ObjectOutputStream / ObjectInputStream.
 * Como a Biblioteca guarda todas as listas, basta gravar esse único objeto.
 */
public final class Persistencia {
    public static final File FICHEIRO = new File("biblioteca.dat");

    private Persistencia() {
    }

    public static void guardar(Biblioteca biblioteca, File ficheiro) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ficheiro))) {
            out.writeObject(biblioteca);
        }
    }

    public static Biblioteca carregar(File ficheiro) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ficheiro))) {
            return (Biblioteca) in.readObject();
        }
    }

    /**
     * Lê a biblioteca do ficheiro. Se o ficheiro ainda não existir (primeira execução),
     * cria uma biblioteca nova com alguns dados de exemplo.
     */
    public static Biblioteca carregarOuCriar(File ficheiro) throws IOException, ClassNotFoundException {
        if (ficheiro.exists()) {
            return carregar(ficheiro);
        }
        return DadosExemplo.criar();
    }
}
