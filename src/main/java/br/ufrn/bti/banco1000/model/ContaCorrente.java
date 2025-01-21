package br.ufrn.bti.banco1000.model;

public class ContaCorrente extends Conta {
    private double taxaManutencao;

    public ContaCorrente(String nome, Cliente cliente, int agencia, int numeroConta, int senha, double saldo, double taxaManutencao) {
        super(nome, cliente, agencia, numeroConta, TipoConta.CORRENTE, senha, saldo);
        this.taxaManutencao = taxaManutencao;
    }

    public void aplicarTaxaManutencao() {
        sacar(taxaManutencao);
        getMovimentacoes().add(new Movimentacao(
                Movimentacao.TipoMovimentacao.MANUTENCAO,
                getCliente(),
                "Taxa de manutenção",
                taxaManutencao
        ));
    }
}
