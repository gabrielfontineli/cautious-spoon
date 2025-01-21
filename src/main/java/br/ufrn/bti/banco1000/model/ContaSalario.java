package br.ufrn.bti.banco1000.model;

public class ContaSalario extends Conta {
    private String empregador;
    private int limiteSaques;
    private int saquesRealizados;

    public ContaSalario(String nome, Cliente cliente, int agencia, int numeroConta, int senha, double saldo, String empregador, int limiteSaques) {
        super(nome, cliente, agencia, numeroConta, TipoConta.CORRENTE, senha, saldo);
        this.empregador = empregador;
        this.limiteSaques = limiteSaques;
        this.saquesRealizados = 0;
    }

    @Override
    public void depositar(double valor) {
        if (!empregador.equals(getCliente().getNome())) {
            throw new IllegalArgumentException("Apenas depósitos do empregador são permitidos.");
        }
        super.depositar(valor);
    }

    @Override
    public void sacar(double valor) {
        if (saquesRealizados >= limiteSaques) {
            throw new IllegalStateException("Limite de saques mensais atingido.");
        }
        super.sacar(valor);
        saquesRealizados++;
    }
}