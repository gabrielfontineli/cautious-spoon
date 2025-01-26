package br.ufrn.bti.banco1000.model;

/**
 * Representa uma conta poupança, que possui rendimento mensal.
 */
public class ContaPoupanca extends Conta {
    private final double taxaRendimento; // Em porcentagem (e.g., 0.01 para 1%)

    public ContaPoupanca(String nome, Cliente cliente, int agencia, int numeroConta, int senha, double saldoInicial, double taxaRendimento) {
        super(nome, cliente, agencia, numeroConta, TipoConta.POUPANCA, senha, saldoInicial);
        this.taxaRendimento = taxaRendimento;
    }

    /**
     * Aplica o rendimento mensal ao saldo da conta.
     */
    public void aplicarRendimentoMensal() {
        double rendimento = getSaldo() * taxaRendimento;
        depositar(rendimento);
        System.out.println("Rendimento de " + rendimento + " aplicado à conta " + getNumeroConta());
    }
}