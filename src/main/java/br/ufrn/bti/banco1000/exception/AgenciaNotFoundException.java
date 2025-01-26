package br.ufrn.bti.banco1000.exception;

/**
 * Exceção lançada quando uma agência não é encontrada.
 */
public class AgenciaNotFoundException extends RuntimeException {
    public AgenciaNotFoundException(String mensagem) {
        super(mensagem);
    }
}