package dao.cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import entidade.Cliente;

/**
 * Interface que define as operações para a persist�ncia de cliente.
 */
public interface ClienteDAO extends Remote {

    public boolean inserir(Object obj) throws RemoteException;

    public int alterar(Object obj) throws RemoteException;

    public int excluir(Object obj) throws RemoteException;

    @SuppressWarnings("rawtypes")
    public List aplicarFiltro(Object obj) throws RemoteException;

    @SuppressWarnings("rawtypes")
    public List getLista() throws RemoteException;

    public Map<String, Cliente> getMapa() throws RemoteException;

    public void setMapa(Map<String, Cliente> mapa) throws RemoteException;
}
