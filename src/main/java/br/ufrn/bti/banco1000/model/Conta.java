package br.ufrn.bti.banco1000.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma conta bancária no sistema.
 * Suporta operações de depósito, saque, transferência e registro de movimentações.
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
        CORRENTE, POUPANCA, SALARIO
    }

    /**
     * Construtor completo da classe Conta.
     *
     * @param nome Nome da conta.
     * @param cliente Cliente associado.
     * @param agencia Número da agência.
     * @param numeroConta Número único da conta.
     * @param tipo Tipo da conta (CORRENTE, POUPANCA ou SALARIO).
     * @param senha Senha da conta.
     * @param saldo Saldo inicial.
     */
    public Conta(String nome, Cliente cliente, int agencia, int numeroConta, TipoConta tipo, int senha, double saldo) {
        if (saldo < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }

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
        registrarMovimentacao(Movimentacao.TipoMovimentacao.DEPOSITO, "Depósito realizado", valor);
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
        registrarMovimentacao(Movimentacao.TipoMovimentacao.SAQUE, "Saque realizado", valor);
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

        registrarMovimentacao(Movimentacao.TipoMovimentacao.TRANSFERENCIA_ENVIADA,
                "Transferência enviada para conta " + contaDestino.getNumeroConta(), valor);

        contaDestino.registrarMovimentacao(Movimentacao.TipoMovimentacao.TRANSFERENCIA_RECEBIDA,
                "Transferência recebida da conta " + this.numeroConta, valor);
    }

    /**
     * Registra uma movimentação associada a esta conta.
     *
     * @param tipo Tipo da movimentação.
     * @param descricao Descrição da movimentação.
     * @param valor Valor da movimentação.
     */
    private void registrarMovimentacao(Movimentacao.TipoMovimentacao tipo, String descricao, double valor) {
        movimentacoes.add(new Movimentacao(tipo, this.numeroConta, descricao, valor));
    }

    // Métodos para persistência

    /**
     * Converte os dados da conta para uma linha no formato CSV.
     *
     * @return Array de strings representando os campos da conta.
     */
    public String[] toCsvRow() {
        return new String[]{
                String.valueOf(this.numeroConta),
                this.cliente.getNome(),
                String.valueOf(this.agencia),
                this.tipo.name(),
                String.format("%.2f", this.saldo),
                String.valueOf(this.senha)
        };
    }

    /**
     * Reconstrói uma conta a partir de uma linha CSV.
     *
     * @param csv Linha CSV representando uma conta.
     * @param cliente Cliente associado à conta.
     * @return Objeto Conta reconstruído.
     */
    public static Conta fromCsv(String csv, Cliente cliente) {
        String[] campos = csv.split(",");
        if (campos.length < 6) {
            throw new IllegalArgumentException("Formato CSV inválido.");
        }
    
        int numeroConta = Integer.parseInt(campos[0]);
        int agencia = Integer.parseInt(campos[2]);
        TipoConta tipo = TipoConta.valueOf(campos[3]);
        double saldo = Double.parseDouble(campos[4]);
        int senha = Integer.parseInt(campos[5]);
    
        switch (tipo) {
            case CORRENTE:
                double taxaManutencao = Double.parseDouble(campos[6]);
                return new ContaCorrente(campos[1], cliente, agencia, numeroConta, senha, saldo, taxaManutencao);
            case POUPANCA:
                double taxaRendimento = Double.parseDouble(campos[6]);
                return new ContaPoupanca(campos[1], cliente, agencia, numeroConta, senha, saldo, taxaRendimento);
            case SALARIO:
                int limiteSaques = Integer.parseInt(campos[6]);
                return new ContaSalario(campos[1], cliente, agencia, numeroConta, senha, saldo, limiteSaques);
            default:
                throw new IllegalArgumentException("Tipo de conta desconhecido.");
        }
    }

    // Métodos auxiliares

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