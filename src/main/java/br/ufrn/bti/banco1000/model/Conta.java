package br.ufrn.bti.banco1000.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma conta bancária no sistema.
 * Suporta operações de depósito, saque e transferência.
 * Cada conta é associada a um cliente.
 */
public class Conta {
    private String nome;
    private Cliente cliente;
    private int agencia;
    private int numeroConta;
    private TipoConta tipo;
    private int senha;
    private double saldo;
    private List<Movimentacao> movimentacoes;

    public enum TipoConta {
        CORRENTE, POUPANCA
    }

    /**
     * Construtor completo da classe Conta.
     *
     * @param nome Nome da conta.
     * @param cliente Cliente associado.
     * @param agencia Número da agência.
     * @param numeroConta Número único da conta.
     * @param tipo Tipo da conta (CORRENTE ou POUPANCA).
     * @param senha Senha da conta.
     * @param saldo Saldo inicial.
     */
    public Conta(String nome, Cliente cliente, int agencia, int numeroConta, TipoConta tipo, int senha, double saldo) {
        this.nome = nome;
        this.cliente = cliente;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.tipo = tipo;
        this.senha = senha;
        this.saldo = saldo;
        this.movimentacoes = new ArrayList<>();
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public int getAgencia() {
        return agencia;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public double getSaldo() {
        return saldo;
    }

    public List<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    // Métodos principais
    /**
     * Realiza um depósito na conta.
     *
     * @param valor Valor a ser depositado.
     */
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        this.saldo += valor;
        this.movimentacoes.add(new Movimentacao(
                Movimentacao.TipoMovimentacao.DEPOSITO, 
                this.cliente, 
                "Depósito realizado", 
                valor
        ));
    }

    /**
     * Realiza um saque na conta, se houver saldo suficiente.
     *
     * @param valor Valor a ser sacado.
     */
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }
        if (this.saldo < valor) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        this.saldo -= valor;
        this.movimentacoes.add(new Movimentacao(
                Movimentacao.TipoMovimentacao.SAQUE, 
                this.cliente, 
                "Saque realizado", 
                valor
        ));
    }

    /**
     * Transfere um valor para outra conta.
     *
     * @param contaDestino Conta de destino.
     * @param valor Valor a ser transferido.
     */
    public void transferir(Conta contaDestino, double valor) {
        if (contaDestino == null) {
            throw new IllegalArgumentException("A conta de destino não pode ser nula.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }
        if (this.saldo < valor) {
            throw new IllegalStateException("Saldo insuficiente para transferência.");
        }

        this.sacar(valor);
        contaDestino.depositar(valor);

        this.movimentacoes.add(new Movimentacao(
                Movimentacao.TipoMovimentacao.TRANSFERENCIA_ENVIADA, 
                this.cliente, 
                "Transferência enviada para conta " + contaDestino.getNumeroConta(), 
                valor
        ));

        contaDestino.getMovimentacoes().add(new Movimentacao(
                Movimentacao.TipoMovimentacao.TRANSFERENCIA_RECEBIDA, 
                contaDestino.getCliente(), 
                "Transferência recebida da conta " + this.numeroConta, 
                valor
        ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return numeroConta == conta.numeroConta && agencia == conta.agencia;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "nome='" + nome + '\'' +
                ", cliente=" + cliente.getNome() +
                ", agencia=" + agencia +
                ", numeroConta=" + numeroConta +
                ", tipo=" + tipo +
                ", saldo=" + saldo +
                '}';
    }
}