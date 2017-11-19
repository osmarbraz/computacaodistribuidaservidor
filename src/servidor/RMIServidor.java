package servidor;

import dao.DAOFactory;
import dao.cliente.ClienteDAO;
import entidade.Cliente;
import java.util.List;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JTextArea;

/**
 * Classe do servidor RMI
 */
public class RMIServidor {
    private String nomeServidor;
    private String enderecoCoordenador;
    private int porta;
    private int portaSocket;
    private String nomeServico = "clienteDAO"; // Este é o nome pelo qual o serviço será posteriormente localizado			
    private JTextArea log;
    private volatile boolean rodar = true;

    /**
     * muda o estado do servidor
     */
    public void parar() {
        this.rodar = false;
    }

    /**
     * Construtor sem argumentos
     */
    public RMIServidor(){
        this("Servidor","localhost",1099, 4444, null);
    }    
    
    /**
     * Construtor com argumentos
     * @param nomeServidor nome do servidor RMI
     * @param enderecoCoordenador endereço do coordenador RMI
     * @param porta porta a ser aberta pelo servidor RMI
     * @param portaSocket porta a ser aberta pelo servidor Socket
     * @param log Objeto do tipo TextArea para exibir o log
     */
    public RMIServidor(String nomeServidor, String enderecoCoordenador, int porta, int portaSocket, JTextArea log) {
        this.rodar = true;
        this.nomeServidor = nomeServidor;
        this.enderecoCoordenador = enderecoCoordenador;
        this.porta = porta;
        this.portaSocket = portaSocket;
        this.log = log;
    }
    
    /**
     * Escreve no log ou na saída default
     * @param mensagem Mensagem a ser exibida
     */
    public void escreveLog(String mensagem){
        if (log == null){
            System.out.println(mensagem);
        }else {
            log.append(mensagem+"\n");
            log.setCaretPosition(log.getDocument().getLength());
        }
    }

    /**
     * Metodo principal do RMIServidor
     */
    public void executar() {
        while (rodar) {
            servico();
        }
        escreveLog(nomeServidor+": Servidor RMI - Parado");
    }

    /**
     * Verifica se serviço do RMI pode ser habilitado
     */
    public void servico() {
        //Verifica se o objeto não está registrado
        Object obj = getObjetoRegistro(nomeServico);
        escreveLog(nomeServidor+": Início Serviço-Verificando se o objeto está registrado: \'clienteDAO\'");
        if (obj == null) {
            escreveLog(nomeServidor+": RMI Coordenador-Objeto \'clienteDAO\' não esta registrado");
            coordenador();
        } else {
            escreveLog(nomeServidor+": RMI Servidor-Objeto \'clienteDAO\' já está registrado procurando servidor para replicar");
        }
        //Executa o servidor socket para esperar os outros servidores e repliquen os dados
        SocketServidor ser = new SocketServidor(enderecoCoordenador, portaSocket, log);
        ser.setRodar(rodar);
        ser.executa();
    }

    /**
     * Roda os serviços do Coordenador
     */
    public void coordenador() {
        try {
            //Instancia o objeto a ser servido pelo coordenador
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clienteDAO = factory.getClienteDAO();

            //Cria o stub dinamicamente do objeto a ser servido
            ClienteDAO stub = (ClienteDAO) UnicastRemoteObject.exportObject(clienteDAO, 0);
            // Referência para rmiregistry na porta 1099
            Registry registro = LocateRegistry.getRegistry(porta);
            registro.rebind(nomeServico, stub); //Registra o stub no rmiregistry            
            escreveLog(nomeServidor+": RMI Coordenador-Servidor no ar. Nome do objeto: \'clienteDAO\'");

            //Inicia uma Thread para mostras os dados armazenados
            mostrarDadosArmazenados(nomeServidor+":  RMI Coordenador", clienteDAO);

        } catch (Exception e) {
            escreveLog("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Servidor geral nada a fazer somente mostrar os dados recebidos via socket e aguardar virar coordenador
     * @param obj que será exibido
     */
    public void servidor(Object obj) {
        try {
            //Utiliza o objeto existente
            ClienteDAO clienteDAO = (ClienteDAO) obj;

            //Inicia uma Thread para mostras os dados armazenados
            mostrarDadosArmazenados(nomeServidor+": RMI Servidor", clienteDAO);

        } catch (Exception e) {
            System.out.println("Exceção: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retorna o objeto do servidor de registro RMI
     * @param objeto a ser verifica no servidor
     * @return uma instancia do objeto no servidor ou null
     */    
    public Object getObjetoRegistro(String objeto) {
        //Variavel a ser retornada
        Object obj = null;
        try {
            //Referencia para rmiregistry do coordenador na porta 1099
            Registry registro = LocateRegistry.getRegistry(enderecoCoordenador,porta);
            //Localiza a referência do objeto remoto 
            obj = registro.lookup(objeto);
            ClienteDAO clienteDAO = (ClienteDAO) obj;
            if (clienteDAO != null) {
                if (clienteDAO.getLista() == null) {
                    obj = null;
                }
            }
            if (obj != null) {
                return obj;
            }
            return obj;
        } catch (NotBoundException e) {
            //escreveLog("Erro no lookup: " + e);
            obj = null;
            return obj;
        } catch (RemoteException e) {
            escreveLog("Erro no servidor: " + e);
            obj = null;
            return obj;
        }
    }

    /**
     * Mostra os dados armazenados
     * @param mensagem mensagem a contatenada na saída do relatório
     * @param clienteDAO objeto que contêm os dados
     */
    public void mostrarDadosArmazenados(String mensagem, ClienteDAO clienteDAO) {
        //Mostra os dados armazenados no coordenador
        Runnable simple = new Runnable() {
            public void run() {
                while (rodar) {
                    try {
                        Thread.sleep(2000);
                        escreveLog(mensagem+"-Dados armazenados:");
                        List<Cliente> clientes = clienteDAO.getLista();
                        if (clientes != null) {
                            for (Cliente cliente : clientes) {
                                escreveLog("ClienteId:" + cliente.paraString());
                            }
                        }
                    } catch (Exception e) {
                        escreveLog("Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                escreveLog(mensagem+"-Parado");
            }
        };
        new Thread(simple).start();
    }
}
