package br.ufrn.bti.banco1000.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe que representa um cliente do sistema bancário.
 * Um cliente possui nome, CPF, email, telefone e pode ter várias contas associadas.
 * 
 * @author vinicius
 */
public class Cliente {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private List<Conta> contas;

    /**
     * Construtor da classe Cliente.
     *
     * @param nome Nome do cliente.
     * @param cpf CPF do cliente (somente números).
     * @param email Email do cliente.
     * @param telefone Telefone do cliente.
     */
    public Cliente(String nome, String cpf, String email, String telefone) {
        if (!validarCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.contas = new ArrayList<>();
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Conta> getContas() {
        return contas;
    }

    /**
     * Adiciona uma conta à lista de contas do cliente.
     *
     * @param conta Conta a ser adicionada.
     */
    public void adicionarConta(Conta conta) {
        this.contas.add(conta);
    }

    /**
     * Valida o CPF do cliente.
     * 
     * @param cpf CPF a ser validado (somente números).
     * @return true se o CPF for válido, false caso contrário.
     */
    public static boolean validarCpf(String cpf) {
        // Simples validação de tamanho e formato
        return cpf != null && cpf.matches("\\d{11}");
    }

    public static boolean validarEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(email).matches();
    }

    public static boolean validarTelefone(String telefone) {
        String phoneRegex = "^\\+?[0-9. ()-]{7,25}$";
        Pattern pattern = Pattern.compile(phoneRegex);

        return pattern.matcher(telefone).matches();
    }

    public static boolean validarNome(String name) {
        String nameRegex = "^[a-zA-Z\\s]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        
        return name != null && pattern.matcher(name).matches();
    }
    

    /**
     * Exibe as informações do cliente de forma legível.
     *
     * @return String contendo os dados do cliente.
     */
    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", contas=" + contas.size() +
                '}';
    }
}