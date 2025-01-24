package br.ufrn.bti.banco1000.controller;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.bti.banco1000.model.Cliente;

/**
 * Controller para gerenciar operações relacionadas a clientes.
 */
public class ClienteController {
    private final List<Cliente> clientes;

    public ClienteController() {
        this.clientes = new ArrayList<>();
    }

    /**
     * Cadastra um novo cliente no sistema.
     *
     * @param nome Nome do cliente.
     * @param cpf CPF do cliente.
     * @param email Email do cliente.
     * @param telefone Telefone do cliente.
     * @throws IllegalArgumentException
     */
    public void cadastrarCliente(String nome, String cpf, String email, String telefone) {
        if(clienteExiste(cpf)){
            throw new IllegalArgumentException("Cliente já cadastrado!");
        }
        if (!Cliente.validarCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }
        else if(!Cliente.validarNome(nome)){
            throw new IllegalArgumentException("Nome inválido!");
        }
        else if(!Cliente.validarEmail(email)){
            throw new IllegalArgumentException("Email  inválido!");
        }
        else if(!Cliente.validarTelefone(telefone)){
            throw new IllegalArgumentException("Telefone inválido!");
        }
        Cliente cliente = new Cliente(nome, cpf, email, telefone);
        clientes.add(cliente);
    }

    /**
     * Busca um cliente pelo CPF.
     *
     * @param cpf CPF do cliente.
     * @return Cliente encontrado, ou null se não existir.
     */
    public Cliente buscarClientePorCpf(String cpf) {
        return clientes.stream()
                .filter(cliente -> cliente.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    /**
     * Lista todos os clientes cadastrados.
     *
     * @return Lista de clientes.
     */
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    public boolean clienteExiste(String cpf){
        return clientes.stream().anyMatch(cliente -> cliente.getCpf().equals(cpf));
    }
}