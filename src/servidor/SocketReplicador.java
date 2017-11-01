package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.net.Socket;
import dao.DAOFactory;
import dao.cliente.ClienteDAO;
import entidade.Cliente;
import java.util.List;

/**
 * Classe que implementa o replicador de objetos via Socket
 */
public class SocketReplicador implements Runnable {

    public SocketServidor servidor;
    public Socket socket;
    private ObjectOutputStream saida;

    /**
     * Construtor do Replicador de objetos
     *
     * @param servidor Servidor onde o replicador foi instanciado
     * @param socket Socket que o replicador precisa controlar
     */
    public SocketReplicador(SocketServidor servidor, Socket socket) {
        try {
            this.socket = socket;
            this.servidor = servidor;
            saida = new ObjectOutputStream(socket.getOutputStream());// cria o objeto de saida para o fluxo
        } catch (IOException ioe) {
            servidor.escreveLog("IOException: " + ioe);
        }
    }

    /**
     * Método run da thread de replicação
     */
    public void run() {
        try {
            servidor.escreveLog("Coordenador Socket-Iniciando Thread para enviar os dados aos servidores!");
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clienteDAO = factory.getClienteDAO();
            //Maximo de conexoes do servidor
            if (servidor.getQtdeConexoes() < 5) {
                while (servidor.getRodar()) {
                    //Delay de 2000 milisegundos
                    Thread.sleep(2000);
                    List<Cliente> clientes = clienteDAO.getLista();

                    if (clientes != null) {
                        for (Cliente cliente : clientes) {
                            //Retorna uma copia do cliente os dados alterados
                            Cliente copia = cliente.copia();
                            saida.writeObject(copia); //escreve uma copia no socket
                            saida.flush(); //limpa a saida
                        }
                    }
                }
                servidor.escreveLog("Coordenador Socket-Parado");
            }
        } catch (InterruptedException ie) {
            servidor.escreveLog("InterruptedException: " + ie);
        } catch (UnknownHostException uhe) {
            servidor.escreveLog("Conexão Terminada: " + uhe);
        } catch (IOException ioe) {
            servidor.escreveLog("IOException: " + ioe);
        }
    }
}
