package br.ufrn.bti.banco1000.model;

/**
 * Representa uma conta salário, que é restrita a depósitos do empregador
 * e possui um número limitado de saques.
 */
public class ContaSalario extends Conta {
    private final int limiteSaquesMensais;
    private int saquesRealizados;

    public ContaSalario(String nome, Cliente cliente, int agencia, int numeroConta, int senha, double saldoInicial, int limiteSaquesMensais) {
        super(nome, cliente, agencia, numeroConta, TipoConta.SALARIO, senha, saldoInicial);
        this.limiteSaquesMensais = limiteSaquesMensais;
        this.saquesRealizados = 0;
    }

    @Override
    public void depositar(double valor) {
        // Permitir depósito apenas de empregador
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        super.depositar(valor);
        System.out.println("Depósito de " + valor + " realizado na conta salário " + getNumeroConta());
    }

    @Override
    public void sacar(double valor) {
        if (saquesRealizados >= limiteSaquesMensais) {
            throw new IllegalStateException("Limite de saques mensais excedido.");
        }
        super.sacar(valor);
        saquesRealizados++;
        System.out.println("Saque realizado. Saques restantes este mês: " + (limiteSaquesMensais - saquesRealizados));
    }

    public void resetarSaquesMensais() {
        saquesRealizados = 0;
        System.out.println("Limite de saques resetado para a conta " + getNumeroConta());
    }
}