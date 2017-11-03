package dao;

import dao.cliente.ClienteDAO;

/**
 * Abstrai as fontes de dados do sistema.
 */
public abstract class DAOFactory {

    //Tipos de Fonte de Dados suportados pela Factory
    public static final int HASHMAP = 1;
	
	private static String enderecoServidor;
	private static int portaServidor;

    //Retorna o DAO instanciado
    public abstract ClienteDAO getClienteDAO();
	
	//Retorna a Factory do tipo especificado default	
    public static DAOFactory getDAOFactory() {		
		return getDAOFactory("localhost", 1099);
	}	

    //Retorna a Factory do tipo especificado	
    public static DAOFactory getDAOFactory(String enderecoServidor, int portaServidor) {
		setEnderecoServidor(enderecoServidor);
		setPortaServidor(portaServidor);
        int whichFactory = Factory.FABRICA;
        switch (whichFactory) {
            case HASHMAP:
                HashMapDAOFactory factory = new HashMapDAOFactory();
                return factory;
            default:
                return null;
        }
    }
	
	public static void setEnderecoServidor(String _enderecoServidor){
		enderecoServidor = _enderecoServidor;
	}
	
	public static String getEnderecoServidor(){
		return enderecoServidor;
	}
	
	public static void setPortaServidor(int _portaServidor){
		portaServidor = _portaServidor;
	}
	
	public static int getPortaServidor(){
		return portaServidor;
	}	
}
