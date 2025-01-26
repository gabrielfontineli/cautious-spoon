package br.ufrn.bti.banco1000.exception;

/**
 * Exceção lançada quando um cliente não é encontrado.
 */
public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(String mensagem) {
        super(mensagem);
    }
}