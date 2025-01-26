package br.ufrn.bti.banco1000.exception;

/**
 * Exceção lançada quando uma movimentação bancária não é permitida.
 */
public class MovimentacaoNotAllowedException extends RuntimeException {
    public MovimentacaoNotAllowedException(String mensagem) {
        super(mensagem);
    }
}