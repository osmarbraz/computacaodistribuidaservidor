package entidade;

import java.util.List;

import dao.cliente.ClienteDAO;
import dao.DAOFactory;
import java.rmi.RemoteException;
import java.io.Serializable;

/**
 * Classe que representa a abstração principal do sistema.
 *
 * @author osmarbraz
 * @version 1.0
 */
public class Cliente implements Serializable {

    /**
     * Serve para identificar um cliente.
     */
    private String clienteId;
    /**
     * Nome do Cliente.
     */
    private String nome;
    /**
     * CPF do cliente
     */
    private String cpf;

    /**
     * Construtor sem argumentos da classe.
     */
    public Cliente() {
        this("", "", "");
    }

    /**
     * Construtor com argumentos da classe.
     *
     * @param clienteId
     * @param nome
     */
    public Cliente(String clienteId, String nome, String cpf) {
        setClienteId(clienteId);
        setNome(nome);
        setCpf(cpf);
    }

    /**
     * Retorna o id de um cliente.
     */
    public String getClienteId() {
        return clienteId;
    }

    /**
     * Modifica o id de um cliente.
     *
     * @param clienteId Um literal com o id de um cliente.
     */
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    /**
     * Retorna o nome de um cliente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Modifica o nome de um cliente.
     *
     * @param nome Um literal com o nome de um cliente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o cpf de um cliente.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Modifica o CPF de um cliente.
     *
     * @param cpf Um literal com o cpf de um cliente
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Retorna uma string com o estado do objeto.
     */
    public String paraString() {
        return ("clienteId:" + getClienteId() + " - Nome :" + getNome() + " = CPF:" + getCpf());
    }

    /**
     * Persiste um objeto.
     */
    public boolean inserir() {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.inserir(this);
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return false;
        }

    }

    /**
     * Altera o estado de um objeto persistente.
     */
    public int alterar() {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.alterar(this);
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return 0;
        }
    }

    /**
     * Exclui um objeto da persistência através do identificado.
     */
    public int excluir() {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.excluir(this);
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return 0;
        }
    }

    /**
     * Retorna uma lista de objetos que atende os valores passados pelo objeto.
     * O Id realiza comparação e o nome realiza uma comparação parcial.
     */
    @SuppressWarnings("rawtypes")
    public List aplicarFiltro() {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.aplicarFiltro(this);
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return null;
        }
    }

    /**
     * Retorna uma lista com todos os objetos.
     */
    @SuppressWarnings("rawtypes")
    public List getLista() {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.getLista();
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return null;
        }
    }

    /**
     * Restaura o estado do objeto apartir do id.
     */
    @SuppressWarnings("rawtypes")
    public boolean abrir() {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory();
            ClienteDAO clientedao = factory.getClienteDAO();
            List lista = clientedao.aplicarFiltro(this);
            if (!lista.isEmpty()) {
                Cliente oCliente = (Cliente) lista.iterator().next();
                setNome(oCliente.getNome());
                setCpf(oCliente.getCpf());
                return true;
            } else {
                return false;
            }
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return false;
        }
    }

    /**
     * Retorna uma copia do cliente com os mesmos dados
     *
     * @return uma nova instancia do objeto cliente com os mesmos dados
     */
    public Cliente copia() {
        return new Cliente(getClienteId(), getNome() + "_1", getCpf());
    }
}
