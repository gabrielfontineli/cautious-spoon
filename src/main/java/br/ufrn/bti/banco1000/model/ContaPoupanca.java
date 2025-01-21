package br.ufrn.bti.banco1000.model;

public class ContaPoupanca extends Conta {
    private double taxaRendimento;

    public ContaPoupanca(String nome, Cliente cliente, int agencia, int numeroConta, int senha, double saldo, double taxaRendimento) {
        super(nome, cliente, agencia, numeroConta, TipoConta.POUPANCA, senha, saldo);
        this.taxaRendimento = taxaRendimento;
    }

    public void aplicarRendimento() {
        double rendimento = getSaldo() * (taxaRendimento / 100);
        depositar(rendimento);
        getMovimentacoes().add(new Movimentacao(
                Movimentacao.TipoMovimentacao.RENDIMENTO,
                getCliente(),
                "Rendimento mensal",
                rendimento
        ));
    }
}