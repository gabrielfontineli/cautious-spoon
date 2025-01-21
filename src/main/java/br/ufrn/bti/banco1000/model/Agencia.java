package br.ufrn.bti.banco1000.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma agência bancária.
 * Cada agência gerencia uma lista de contas associadas a ela.
 */
public class Agencia {
    private final int numeroAgencia;
    private final String nome;
    private final List<Conta> contas;

    /**
     * Construtor para inicializar uma agência.
     *
     * @param numeroAgencia Número identificador da agência.
     * @param nome Nome da agência.
     */
    public Agencia(int numeroAgencia, String nome) {
        this.numeroAgencia = numeroAgencia;
        this.nome = nome;
        this.contas = new ArrayList<>();
    }

    // Getters e Setters
    public int getNumeroAgencia() {
        return numeroAgencia;
    }

    public String getNome() {
        return nome;
    }

    public List<Conta> getContas() {
        return contas;
    }

    @Override
    public String toString() {
        return "Agencia{" +
                "numeroAgencia=" + numeroAgencia +
                ", nome='" + nome + '\'' +
                ", totalContas=" + contas.size() +
                '}';
    }

    public Cliente[] getClientes() {
        return new Cliente[0];
    }
}