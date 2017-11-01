import controle.CtlServidor;

/**
 * Classe que possui a operação main da aplicação. Server para dar inicio ao
 * sistema.
 */
public class Principal {

    /**
     * Inicia a aplicação.
     *
     * @param args
     */
    public static void main(String args[]) {

        CtlServidor controle = new CtlServidor();
        controle.executar();

    }
}
