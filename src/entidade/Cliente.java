package entidade;

import java.util.List;

import dao.cliente.ClienteDAO;
import dao.DAOFactory;
import java.rmi.RemoteException;
import java.io.Serializable;

/**
 * Classe que representa a abstração principal do sistema.
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
     * @param clienteId id de um cliente
     * @param nome nome de um cliente
     * @param cpf cpf de um cliente
     */
    public Cliente(String clienteId, String nome, String cpf) {
        setClienteId(clienteId);
        setNome(nome);
        setCpf(cpf);
    }

    /**
     * Retorna o id de um cliente.
     *
     * @return uma string com o id do cliente
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
     *
     * @return Uma string com o nome
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
     *
     * @return Uma strint com o cpf
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
     *
     * @return Uma string com os dados do cliente concatenados
     */
    public String paraString() {
        return ("clienteId:" + getClienteId() + " - Nome :" + getNome() + " = CPF:" + getCpf());
    }

    /**
     * Chamada da operação inserir para o servidor default
     *
     * @return um boolean com o resultado da operação
     */
    public boolean inserir() {
        return inserir("localhost", 1099);
    }

    /**
     * Persiste um objeto.
     *
     * @param enderecoServidor Endereço do servidor remoto
     * @param portaServidor Porta do servidor remoto
     * @return um boolean com o resultado da operação
     */
    public boolean inserir(String enderecoServidor, int portaServidor) {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(enderecoServidor, portaServidor);
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.inserir(this);
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return false;
        }
    }

    /**
     * Chamada da operação alterar para o servidor default
     *
     * @return um int com o resultado da operação
     */
    public int alterar() {
        return alterar("localhost", 1099);
    }

    /**
     * Altera o estado de um objeto persistente.
     *
     * @param enderecoServidor Endereço do servidor remoto
     * @param portaServidor Porta do servidor remoto
     * @return Um inteiro com a quantidade de alterações
     */
    public int alterar(String enderecoServidor, int portaServidor) {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(enderecoServidor, portaServidor);
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.alterar(this);
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return 0;
        }
    }

    /**
     * Chamada da operação excluir para o servidor default
     *
     * @return um int com o resultado da operação
     */
    public int excluir() {
        return excluir("localhost", 1099);
    }

    /**
     * Exclui um objeto da persistência através do id do cliente.
     *
     * @param enderecoServidor Endereço do servidor remoto
     * @param portaServidor Porta do servidor remoto
     * @return
     */
    public int excluir(String enderecoServidor, int portaServidor) {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(enderecoServidor, portaServidor);
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.excluir(this);
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return 0;
        }
    }

    /**
     * Chamada da operação aplicar filtro para o servidor default
     *
     * @return um List com o resultado da operação
     */
    public List aplicarFiltro() {
        return aplicarFiltro("localhost", 1099);
    }

    /**
     * Retorna uma lista de objetos que atende os valores passados pelo objeto.
     * O Id realiza comparação e o nome realiza uma comparação parcial.
     *
     * @param enderecoServidor Endereço do servidor remoto
     * @param portaServidor Porta do servidor remoto
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List aplicarFiltro(String enderecoServidor, int portaServidor) {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(enderecoServidor, portaServidor);
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.aplicarFiltro(this);
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return null;
        }
    }

    /**
     * Chamada da operação getLista para o servidor default
     *
     * @return um List com o resultado da operação
     */
    public List getLista() {
        return getLista("localhost", 1099);
    }

    /**
     * Retorna uma lista com todos os objetos.
     *
     * @param enderecoServidor Endereço do servidor remoto
     * @param portaServidor Porta do servidor remoto
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List getLista(String enderecoServidor, int portaServidor) {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(enderecoServidor, portaServidor);
            ClienteDAO clientedao = factory.getClienteDAO();
            return clientedao.getLista();
        } catch (RemoteException e) {
            System.out.println("Erro no servidor: " + e);
            return null;
        }
    }

    /**
     * Chamada da operação abrir para o servidor default
     *
     * @return um boolean com o resultado da operação
     */
    public boolean abrir() {
        return abrir("localhost", 1099);
    }

    /**
     * Restaura o estado do objeto apartir do id.
     *
     * @param enderecoServidor
     * @param portaServidor
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean abrir(String enderecoServidor, int portaServidor) {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(enderecoServidor, portaServidor);
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
     * Retorna uma cópia do cliente com os mesmos dados
     *
     * @return uma nova instancia do objeto cliente com os mesmos dados
     */
    public Cliente copia() {
        return new Cliente(getClienteId(), getNome()+"_1", getCpf());
    }
}
