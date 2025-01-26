package br.ufrn.bti.banco1000.controller;

import br.ufrn.bti.banco1000.exception.ClienteAlreadyExistsException;
import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.utils.ExportarCSV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     */
    public void cadastrarCliente(String nome, String cpf, String email, String telefone) {
        if (!Cliente.validarCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }

        if (buscarClientePorCpf(cpf) != null) {
            throw new ClienteAlreadyExistsException("Cliente já cadastrado com este CPF.");
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

    /**
     * Exporta os clientes cadastrados para um arquivo CSV.
     *
     * @param filePath Caminho do arquivo CSV.
     */
    public void exportarClientesCsv(String filePath) {
        List<String[]> rows = new ArrayList<>();

        for (Cliente cliente : clientes) {
            rows.add(new String[]{
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getEmail(),
                    cliente.getTelefone()
            });
        }

        try {
            ExportarCSV.export(filePath, new String[]{"Nome", "CPF", "Email", "Telefone"}, rows);
        } catch (IOException e) {
            System.err.println("Erro ao exportar clientes para CSV: " + e.getMessage());
        }
    }

    /**
     * Importa clientes de um arquivo CSV.
     *
     * @param filePath Caminho do arquivo CSV.
     */
    public void importarClientesCsv(String filePath) {
        try {
            List<String[]> rows = ExportarCSV.importar(filePath);

            for (String[] row : rows) {
                if (row.length != 4) {
                    System.err.println("Linha CSV inválida. Ignorando...");
                    continue;
                }

                String nome = row[0];
                String cpf = row[1];
                String email = row[2];
                String telefone = row[3];

                if (buscarClientePorCpf(cpf) == null) {
                    cadastrarCliente(nome, cpf, email, telefone);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao importar clientes de CSV: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo CSV: " + e.getMessage());
        }
    }
}