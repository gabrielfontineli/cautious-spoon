package br.ufrn.bti.banco1000.exception;

/**
 * Exceção lançada quando um cliente já existe no sistema.
 */
public class ClienteAlreadyExistsException extends RuntimeException {
    public ClienteAlreadyExistsException(String mensagem) {
        super(mensagem);
    }
}