package dao;

import dao.cliente.ClienteDAO;
import dao.cliente.HashMapClienteDAO;

/**
 * Implementa a fonte de dado para persistência em memória utilizando HashMap.
 */
public class HashMapDAOFactory extends DAOFactory {

    public HashMapDAOFactory() {
        super();
    }

    /**
     * Retorna uma Cliente DAO
     *
     * @return ClienteDAO Um DAORemoto para cliente
     */
    public ClienteDAO getClienteDAO() {
        return new HashMapClienteDAO();
    }
}
