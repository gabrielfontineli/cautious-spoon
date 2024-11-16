package br.ufrn.bti.banco1000.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe que representa uma transferência entre contas bancárias.
 * Contém informações sobre a conta de origem, conta de destino,
 * valor, data e descrição da transferência.
 * 
 * @author vinicius
 */
public class Transferencia {

    private Conta contaOrigem;
    private Conta contaDestino;
    private double valor;
    private Date data;
    private String descricao;

    /**
     * Construtor da classe Transferencia.
     *
     * @param contaOrigem Conta de onde o valor será retirado.
     * @param contaDestino Conta para onde o valor será enviado.
     * @param valor Valor da transferência.
     * @param descricao Descrição da transferência.
     */
    public Transferencia(Conta contaOrigem, Conta contaDestino, double valor, String descricao) {
        if (contaOrigem == null || contaDestino == null) {
            throw new IllegalArgumentException("As contas de origem e destino não podem ser nulas.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }
        if (contaOrigem.getSaldo() < valor) {
            throw new IllegalStateException("Saldo insuficiente na conta de origem.");
        }

        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.valor = valor;
        this.data = new Date();
        this.descricao = descricao;

        realizarTransferencia();
    }

    /**
     * Realiza a transferência entre as contas.
     */
    private void realizarTransferencia() {
        contaOrigem.sacar(valor);
        contaDestino.depositar(valor);
        contaOrigem.getMovimentacoes().add(
                new Movimentacao(Movimentacao.TipoMovimentacao.TRANSFERENCIA_ENVIADA, 
                                 contaOrigem.getCliente(), 
                                 "Transferência para conta " + contaDestino.getNumeroConta(), 
                                 valor));
        contaDestino.getMovimentacoes().add(
                new Movimentacao(Movimentacao.TipoMovimentacao.TRANSFERENCIA_RECEBIDA, 
                                 contaDestino.getCliente(), 
                                 "Transferência recebida da conta " + contaOrigem.getNumeroConta(), 
                                 valor));
    }

    // Getters
    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public double getValor() {
        return valor;
    }

    public Date getData() {
        return data;
    }

    public String getDataFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(data);
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return "Transferencia{" +
                "contaOrigem=" + contaOrigem.getNumeroConta() +
                ", contaDestino=" + contaDestino.getNumeroConta() +
                ", valor=" + valor +
                ", data=" + getDataFormatada() +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}