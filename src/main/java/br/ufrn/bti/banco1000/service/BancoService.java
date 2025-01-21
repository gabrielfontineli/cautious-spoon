package br.ufrn.bti.banco1000.service;

import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;
import br.ufrn.bti.banco1000.model.Movimentacao;
import br.ufrn.bti.banco1000.model.Transferencia;
import br.ufrn.bti.banco1000.model.Conta.TipoConta;

import java.io.*;
import java.util.*;

public class BancoService {

    private static final String ARQUIVO_AGENCIAS = "agencias.csv";
    private static final String ARQUIVO_CONTAS = "contas.csv";
    private static final String ARQUIVO_MOVIMENTACOES = "movimentacoes.csv";

    public void salvarAgencias(List<Agencia> agencias) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_AGENCIAS))) {
            for (Agencia agencia : agencias) {
                writer.write(agencia.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Agencia> carregarAgencias() {
        List<Agencia> agencias = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_AGENCIAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                agencias.add(Agencia.fromCsv(linha));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agencias;
    }

    public void salvarContas(List<Conta> contas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CONTAS))) {
            for (Conta conta : contas) {
                writer.write(conta.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Conta> carregarContas() {
        List<Conta> contas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CONTAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                contas.add(Conta.fromCsv(linha));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contas;
    }

    public void salvarMovimentacoes(List<Movimentacao> movimentacoes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_MOVIMENTACOES))) {
            for (Movimentacao movimentacao : movimentacoes) {
                writer.write(movimentacao.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Movimentacao> carregarMovimentacoes() {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_MOVIMENTACOES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                movimentacoes.add(Movimentacao.fromCsv(linha));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movimentacoes;
    }
}