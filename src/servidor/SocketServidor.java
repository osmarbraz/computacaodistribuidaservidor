package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.List;
import dao.cliente.ClienteDAO;
import dao.DAOFactory;
import entidade.Cliente;
import javax.swing.JTextArea;

/**
 * Classe do servidor Socket
 */
public class SocketServidor {

    //Thread para tratar as conexoes dos servidores
    private Thread[] threads = new Thread[5];
    //Replicador para conter as referências dos objetos e socket 
    private SocketReplicador[] replicadores = new SocketReplicador[5];
    private int qtd = 0;
    private ServerSocket escuta;
    public Socket socket;
    private String enderecoCoordenador;
    private int porta;
    private JTextArea log;
    private volatile boolean rodar = true;

    /**
     * Construtor sem argumentos do servidor de socket
     */
    public SocketServidor() {
        this("localhost", 4444, null);
    }

    /**
     * Construtor com argumentos do servidor de socket
     *
     * @param enderecoCoordenador endereço do coordenador
     * @param porta porta do servidor socket a ser aberto
     * @param log Objeto de log a ser utilizado
     */
    public SocketServidor(String enderecoCoordenador, int porta, JTextArea log) {
        this.rodar = true;
        this.enderecoCoordenador = enderecoCoordenador;
        this.porta = porta;
        this.log = log;
    }

    /**
     * muda o estado do servidor
     *
     * @param rodar variável para mudar status do servidor
     */
    public void setRodar(boolean rodar) {
        this.rodar = rodar;
    }

    /**
     * retorna o estado de rodar
     *
     * @return o estado de rodar
     */
    public boolean getRodar() {
        return rodar;
    }

    /**
     * Escreve no log ou na saida default
     *
     * @param mensagem Mensagem a ser exibida
     */
    public void escreveLog(String mensagem) {
        if (log == null) {
            System.out.println(mensagem);
        } else {
            log.append(mensagem + "\n");
        }
    }

    /**
     * Método principal da classe Servidor
     */
    public void executa() {
        if (getVerificaPorta(porta) == false) {
            escreveLog("Socket Coordenador-Porta Liberada abrindo servidor para replicar os dados via RMI");
            //Coordenador abrindo socket para servir os dados 
            servirObjeto();
        } else {
            escreveLog("Socket Servidor-Porta Ocupada, conectando ao servidor para replicar os dados via Sockets");
            //Servidor conectando via socket no cordenador para pegar os dados
            pegarObjeto();
        }
    }

    /**
     * Coordenador esperando clientes(outros servidores) para servir os dados
     */
    public void servirObjeto() {
        try {
            escuta = new ServerSocket(porta);
            escreveLog("Socket Coordenador-Escutando a porta esperando servidores " + escuta.getLocalPort());
            while (rodar) {
                Socket socket = escuta.accept(); // espera um outro servidor cliente se conectar
                if (getQtdeConexoes() < 5) {
                    //cria um replicador e coloca em um vetor
                    replicadores[getQtdeConexoes()] = new SocketReplicador(this, socket);
                    //cria um thread com o replicador e coloca em outro vetor
                    threads[getQtdeConexoes()] = new Thread(replicadores[getQtdeConexoes()]);
                    threads[getQtdeConexoes()].start();
                    setQtdeConexoes(getQtdeConexoes() + 1);
                } else {
                    escreveLog("Socket Coordenador-Número máximo de conexoes atingidas");
                }
            }
            //Para todas as threds do servidor
            for (int i = 0; i < getQtdeConexoes(); i++) {
                threads[i].interrupt();
            }
            escuta.close();
            escreveLog("Socket Coordenador - Parado");
        } catch (UnknownHostException uhe) {
            escreveLog("Conexão Terminada:" + uhe);
        } catch (IOException ioe) {
            escreveLog("IOException" + ioe);
        }
    }

    /**
     * Servidor que pega os objetos do Coordenador RMI
     */
    public void pegarObjeto() {
        Socket con = null;
        ObjectInputStream entrada = null;
        try {
            //Conecta no coordenador para pegar os objetos
            con = new Socket(enderecoCoordenador, porta);
            entrada = new ObjectInputStream(con.getInputStream()); // cria o objeto de entrada para o fluxo
            escreveLog("Socket Servidor-Início");
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clienteDAO = factory.getClienteDAO();
            while (rodar) {
                try {
                    //Retardo no replicação
                    Thread.sleep(2000);
                    escreveLog("Socket Servidor-Recebendo dados do coordenador");
                    //Le o objeto cliente enviado pelo coordenador
                    Cliente clienteRecebido = (Cliente) entrada.readObject();
                    //Inserindo o cliente no novo dao
                    clienteRecebido.inserir();
                    //Mostra os dados recebidos do coordenador
                    List<Cliente> clientes = clienteRecebido.getLista();
                    if (clientes != null) {
                        for (Cliente cliente : clientes) {
                            escreveLog("ClienteId:" + cliente.paraString());
                        }
                    }
                } catch (ClassNotFoundException cnfe) {
                    escreveLog("ClassNotFoundException: " + cnfe);
                } catch (InterruptedException ie) {
                    escreveLog("InterruptedException: " + ie);
                }
            }
            escreveLog("Socket Servidor - Parado");
        } catch (IOException ioe) {
            escreveLog("IOException" + ioe);
            escreveLog(">>>Parando de receber dados do servidor coordenador");
            try {
                entrada.close();
                con.close();
            } catch (IOException io) {
            }
        }
    }

    /**
     * Verifica se a porta esta ocupada
     *
     * @param porta porta a ser verificada
     * @return se a porta está ocupada
     */
    public boolean getVerificaPorta(int porta) {
        boolean portaOcupada = false;
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(porta);
        } catch (IOException e) {
            portaOcupada = true;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    /* e.printStackTrace(); */
                }
            }
        }
        return portaOcupada;
    }

    /**
     * Retorna quantidade conexões abertas
     *
     * @return a quantidade de conexões
     */
    public int getQtdeConexoes() {
        return qtd;
    }

    /**
     * Modificador da quantidade de conexões
     *
     * @param qtd quantidade a ser modificada
     */
    public void setQtdeConexoes(int qtd) {
        this.qtd = qtd;
    }
}
