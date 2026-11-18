/*
 * G16 - POO 2026/2027 - Trabalho Prático - T03 Os meus livros
 * Elementos do grupo:
 *   - aNNNNN Rodrigo
 *   - 54111  Vitória Sá
 *   - 54482  Inês Menezes
 *   - aNNNNN Henrique
 */
package livros;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("texto")) {
            new MenuTexto(Biblioteca.carregar()).executar();
        } else {
            Application.launch(AppFX.class, args);
        }
    }
}
