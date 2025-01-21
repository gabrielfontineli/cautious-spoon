package br.ufrn.bti.banco1000.service;

import br.ufrn.bti.banco1000.model.Agencia;
import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Movimentacao;
import br.ufrn.bti.banco1000.model.Conta;
import br.ufrn.bti.banco1000.model.Transferencia;

import java.io.*;
import java.util.*;

public class BancoCsv {
    private List<Agencia> agencias;

    public BancoCsv() {
        agencias = new ArrayList<>();
    }

    public static void salvarDados() {
        BancoCsv bancoCsv = new BancoCsv();  // Cria uma instância para chamar os métodos não estáticos
        bancoCsv.salvarAgencias();
        bancoCsv.salvarContas();
        bancoCsv.salvarMovimentacoes();
    }

    public static void carregarDados() {
        BancoCsv bancoCsv = new BancoCsv();  // Cria uma instância para chamar os métodos não estáticos
        bancoCsv.carregarAgencias();
        bancoCsv.carregarContas();
        bancoCsv.carregarMovimentacoes();
    }

    private void carregarAgencias() {
        try (BufferedReader reader = new BufferedReader(new FileReader("agencias.csv"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 2) {
                    int numeroAgencia = Integer.parseInt(dados[0]);
                    String nomeAgencia = dados[1];
                    Agencia agencia = new Agencia(numeroAgencia, nomeAgencia);
                    agencias.add(agencia);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarContas() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contas.csv"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 6) {
                    String nomeConta = dados[0];
                    int numeroConta = Integer.parseInt(dados[1]);
                    int agencia = Integer.parseInt(dados[2]);
                    Conta.TipoConta tipo = Conta.TipoConta.valueOf(dados[3]);
                    double saldo = Double.parseDouble(dados[4]);
                    String cpfCliente = dados[5];

                    Cliente cliente = findClienteByCpf(cpfCliente);
                    if (cliente != null) {
                        Conta conta = new Conta(nomeConta, cliente, agencia, numeroConta, tipo, saldo, saldo);
                        cliente.adicionarConta(conta);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarMovimentacoes() {
        try (BufferedReader reader = new BufferedReader(new FileReader("movimentacoes.csv"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 5) {
                    int numeroContaOrigem = Integer.parseInt(dados[0]);
                    int numeroContaDestino = Integer.parseInt(dados[1]);
                    double valor = Double.parseDouble(dados[2]);
                    Movimentacao.TipoMovimentacao tipo = Movimentacao.TipoMovimentacao.valueOf(dados[3]);
                    String descricao = dados[4];

                    Conta contaOrigem = findContaByNumero(numeroContaOrigem);
                    Conta contaDestino = findContaByNumero(numeroContaDestino);
                    if (contaOrigem != null && contaDestino != null) {
                        new Transferencia(contaOrigem, contaDestino, valor, descricao);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Agencia findAgenciaByNumero(int numeroAgencia) {
        for (Agencia agencia : agencias) {
            if (agencia.getNumeroAgencia() == numeroAgencia) {
                return agencia;
            }
        }
        return null;
    }

    private Cliente findClienteByCpf(String cpf) {
        for (Agencia agencia : agencias) {
            for (Cliente cliente : agencia.getClientes()) {
                if (cliente.getCpf().equals(cpf)) {
                    return cliente;
                }
            }
        }
        return null;
    }

    private Conta findContaByNumero(int numeroConta) {
        for (Agencia agencia : agencias) {
            for (Conta conta : agencia.getContas()) {
                if (conta.getNumeroConta() == numeroConta) {
                    return conta;
                }
            }
        }
        return null;
    }

    private void salvarAgencias() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("agencias.csv"))) {
            for (Agencia agencia : agencias) {
                writer.write(agencia.getNumeroAgencia() + "," + agencia.getNome() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarContas() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contas.csv"))) {
            for (Agencia agencia : agencias) {
                for (Conta conta : agencia.getContas()) {
                    writer.write(conta.getNome() + "," +
                            conta.getNumeroConta() + "," +
                            agencia.getNumeroAgencia() + "," +
                            conta.getTipo() + "," +
                            conta.getSaldo() + "," +
                            conta.getCliente().getCpf() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarMovimentacoes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("movimentacoes.csv"))) {
            for (Agencia agencia : agencias) {
                for (Conta conta : agencia.getContas()) {
                    for (Movimentacao movimentacao : conta.getMovimentacoes()) {
                        writer.write(conta.getNumeroConta() + "," + conta.getNumeroConta() + "," + movimentacao.getValor() +
                                "," + movimentacao.getTipo() + "," + movimentacao.getDescricao() + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}