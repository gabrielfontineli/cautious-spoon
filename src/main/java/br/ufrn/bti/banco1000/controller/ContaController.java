package br.ufrn.bti.banco1000.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;
import br.ufrn.bti.banco1000.utils.ExportarCSV;

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
        if(contaExiste(agencia, numeroConta)){
            throw new IllegalArgumentException("Conta com mesma agência e número de conta já existe");
        }
        if (!Conta.validarNomeConta(nome)) {
            throw new IllegalArgumentException("Nome da conta inválido. Deve conter apenas letras e/ou espaços");
        }
        if (!Conta.validarAgencia(agencia)) {
            throw new IllegalArgumentException("Agência inválida.Deve ser composta por 4 dígitos");
        }
        if (!Conta.validarNumeroConta(numeroConta)) {
            throw new IllegalArgumentException("Número da conta inválido. Deve ter de 6 a 10 dígitos");
        }
        if (!Conta.validarTipoConta(tipo)) {
            throw new IllegalArgumentException("Tipo de conta inválido");
        }
        if (!Conta.validarSenha(senha)) {
            throw new IllegalArgumentException("Senha inválida. Deve ter no mínimo 6 caracteres");
        }
        if (!Conta.validarSaldoInicial(saldo)) {
            throw new IllegalArgumentException("Saldo inicial inválido. O saldo deve ser positivo");
        }

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
        if(contas.isEmpty()){
            System.out.println("Não existem contas cadastradas");
            return new ArrayList<>();
        }
        ArrayList<Conta> listadecontas = new ArrayList<>(contas);
        return listadecontas;
    }

    public void exportarContasCsv(String filePath) {
        List<String[]> rows = new ArrayList<>();

        for (Conta conta : contas) {
            rows.add(conta.toCsvRow());
        }

        try {
            ExportarCSV.export(filePath, new String[] {
                "Agência", "Número Conta", "Nome", "Cliente", "Tipo Conta", "Saldo"
            }, rows);
        } catch (IOException e) {
            System.err.println("Erro ao exportar contas para CSV: " + e.getMessage());
        }
    }

    public boolean contaExiste(int agencia, int numeroConta){
        return contas.stream().anyMatch(conta -> conta.getAgencia() == agencia && conta.getNumeroConta() == numeroConta);
    }
}