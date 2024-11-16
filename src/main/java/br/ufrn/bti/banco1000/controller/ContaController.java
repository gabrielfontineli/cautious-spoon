package br.ufrn.bti.banco1000.controller;

import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gerenciar operações bancárias.
 */
public class ContaController {
    private final List<Conta> contas;

    public ContaController() {
        this.contas = new ArrayList<>();
    }

    /**
     * Cria uma nova conta para um cliente.
     *
     * @param nome Nome da conta.
     * @param cliente Cliente associado.
     * @param agencia Número da agência.
     * @param numeroConta Número único da conta.
     * @param tipo Tipo da conta (CORRENTE ou POUPANCA).
     * @param senha Senha da conta.
     * @param saldo Saldo inicial.
     */
    public void criarConta(String nome, Cliente cliente, int agencia, int numeroConta, Conta.TipoConta tipo, int senha, double saldo) {
        Conta conta = new Conta(nome, cliente, agencia, numeroConta, tipo, senha, saldo);
        contas.add(conta);
        cliente.adicionarConta(conta);
    }

    /**
     * Busca uma conta pelo número e agência.
     *
     * @param agencia Número da agência.
     * @param numeroConta Número da conta.
     * @return Conta encontrada, ou null se não existir.
     */
    public Conta buscarConta(int agencia, int numeroConta) {
        return contas.stream()
                .filter(conta -> conta.getAgencia() == agencia && conta.getNumeroConta() == numeroConta)
                .findFirst()
                .orElse(null);
    }

    /**
     * Realiza um depósito em uma conta.
     *
     * @param conta Conta a ser depositada.
     * @param valor Valor do depósito.
     */
    public void depositar(Conta conta, double valor) {
        if (conta == null) {
            throw new IllegalArgumentException("Conta inválida.");
        }
        conta.depositar(valor);
    }

    /**
     * Realiza um saque em uma conta.
     *
     * @param conta Conta a ser sacada.
     * @param valor Valor do saque.
     */
    public void sacar(Conta conta, double valor) {
        if (conta == null) {
            throw new IllegalArgumentException("Conta inválida.");
        }
        conta.sacar(valor);
    }

    /**
     * Realiza uma transferência entre contas.
     *
     * @param contaOrigem Conta de origem.
     * @param contaDestino Conta de destino.
     * @param valor Valor da transferência.
     */
    public void transferir(Conta contaOrigem, Conta contaDestino, double valor) {
        if (contaOrigem == null || contaDestino == null) {
            throw new IllegalArgumentException("Conta de origem ou destino inválida.");
        }
        contaOrigem.transferir(contaDestino, valor);
    }

    /**
     * Lista todas as contas cadastradas.
     *
     * @return Lista de contas.
     */
    public List<Conta> listarContas() {
        return new ArrayList<>(contas);
    }
}