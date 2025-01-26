package br.ufrn.bti.banco1000.exception;

/**
 * Exceção lançada quando uma conta não é encontrada.
 */
public class ContaNotFoundException extends RuntimeException {
    public ContaNotFoundException(String mensagem) {
        super(mensagem);
    }
}