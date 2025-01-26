package br.ufrn.bti.banco1000.model;

/**
 * Representa uma conta corrente, que possui uma taxa de manutenção.
 */
public class ContaCorrente extends Conta {
    private final double taxaManutencao;

    public ContaCorrente(String nome, Cliente cliente, int agencia, int numeroConta, int senha, double saldoInicial, double taxaManutencao) {
        super(nome, cliente, agencia, numeroConta, TipoConta.CORRENTE, senha, saldoInicial);
        this.taxaManutencao = taxaManutencao;
    }

    /**
     * Deduz a taxa de manutenção mensal do saldo.
     */
    public void aplicarTaxaManutencao() {
        if (getSaldo() < taxaManutencao) {
            throw new IllegalStateException("Saldo insuficiente para deduzir a taxa de manutenção.");
        }
        sacar(taxaManutencao);
        System.out.println("Taxa de manutenção de " + taxaManutencao + " aplicada à conta " + getNumeroConta());
    }
}