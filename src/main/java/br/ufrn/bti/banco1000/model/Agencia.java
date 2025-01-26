package br.ufrn.bti.banco1000.model;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.bti.banco1000.exception.ContaNotFoundException;

/**
 * Representa uma agência bancária.
 */
public class Agencia {
    private final int codigo;
    private final String nome;
    private final List<Conta> contas;

    public Agencia(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
        this.contas = new ArrayList<>();
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public void adicionarConta(Conta conta) {
        if (contas.stream().anyMatch(c -> c.getNumeroConta() == conta.getNumeroConta())) {
            throw new IllegalArgumentException("Conta já existente na agência.");
        }
        contas.add(conta);
    }

    public Conta buscarConta(int numeroConta) {
        return contas.stream()
                .filter(conta -> conta.getNumeroConta() == numeroConta)
                .findFirst()
                .orElseThrow(() -> new ContaNotFoundException("Conta não encontrada."));
    }

    public static Agencia fromCsv(String csv) {
        String[] campos = csv.split(",");
        return new Agencia(Integer.parseInt(campos[0]), campos[1]);
    }

    public String toCsv() {
        return codigo + "," + nome;
    }
}