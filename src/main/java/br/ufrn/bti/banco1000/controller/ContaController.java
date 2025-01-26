package br.ufrn.bti.banco1000.controller;

import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;
import br.ufrn.bti.banco1000.model.Movimentacao;
import br.ufrn.bti.banco1000.utils.ExportarCSV;

import java.io.IOException;
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
     * @param tipo Tipo da conta (CORRENTE, POUPANCA ou SALARIO).
     * @param senha Senha da conta.
     * @param saldo Saldo inicial.
     */
    public void criarConta(String nome, Cliente cliente, int agencia, int numeroConta, Conta.TipoConta tipo, int senha, double saldo) {
        if (buscarConta(agencia, numeroConta) != null) {
            throw new IllegalArgumentException("Já existe uma conta com este número nesta agência.");
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

        // Registrar movimentação
        conta.getMovimentacoes().add(new Movimentacao(
                Movimentacao.TipoMovimentacao.DEPOSITO,
                conta.getNumeroConta(),
                "Depósito realizado",
                valor
        ));
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

        // Registrar movimentação
        conta.getMovimentacoes().add(new Movimentacao(
                Movimentacao.TipoMovimentacao.SAQUE,
                conta.getNumeroConta(),
                "Saque realizado",
                valor
        ));
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

        // Registrar movimentações
        contaOrigem.getMovimentacoes().add(new Movimentacao(
                Movimentacao.TipoMovimentacao.TRANSFERENCIA_ENVIADA,
                contaOrigem.getNumeroConta(),
                "Transferência enviada para conta " + contaDestino.getNumeroConta(),
                valor
        ));

        contaDestino.getMovimentacoes().add(new Movimentacao(
                Movimentacao.TipoMovimentacao.TRANSFERENCIA_RECEBIDA,
                contaDestino.getNumeroConta(),
                "Transferência recebida da conta " + contaOrigem.getNumeroConta(),
                valor
        ));
    }

    /**
     * Lista todas as contas cadastradas.
     *
     * @return Lista de contas.
     */
    public List<Conta> listarContas() {
        return new ArrayList<>(contas);
    }

    /**
     * Exporta as contas para um arquivo CSV.
     *
     * @param filePath Caminho do arquivo CSV.
     */
    public void exportarContasCsv(String filePath) {
        List<String[]> rows = new ArrayList<>();

        for (Conta conta : contas) {
            rows.add(conta.toCsvRow());
        }

        try {
            ExportarCSV.export(filePath, new String[] {
                "Número Conta", "Nome Cliente", "Agência", "Tipo Conta", "Saldo", "Senha"
            }, rows);
        } catch (IOException e) {
            System.err.println("Erro ao exportar contas para CSV: " + e.getMessage());
        }
    }

    /**
     * Importa contas de um arquivo CSV.
     *
     * @param filePath Caminho do arquivo CSV.
     * @param clientes Lista de clientes disponíveis.
     */
    public void importarContasCsv(String filePath, List<Cliente> clientes) {
        try {
            List<String[]> rows = ExportarCSV.importar(filePath);

            for (String[] row : rows) {
                int numeroConta = Integer.parseInt(row[0]);
                String nomeCliente = row[1];
                int agencia = Integer.parseInt(row[2]);
                Conta.TipoConta tipo = Conta.TipoConta.valueOf(row[3]);
                double saldo = Double.parseDouble(row[4]);
                int senha = Integer.parseInt(row[5]);

                Cliente cliente = clientes.stream()
                        .filter(c -> c.getNome().equals(nomeCliente))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + nomeCliente));

                criarConta(nomeCliente, cliente, agencia, numeroConta, tipo, senha, saldo);
            }
        } catch (IOException e) {
            System.err.println("Erro ao importar contas de CSV: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo CSV: " + e.getMessage());
        }
    }
}