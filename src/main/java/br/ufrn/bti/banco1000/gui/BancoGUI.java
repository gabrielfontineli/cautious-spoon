package br.ufrn.bti.banco1000.gui;

import br.ufrn.bti.banco1000.controller.ClienteController;
import br.ufrn.bti.banco1000.controller.ContaController;
import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;
import br.ufrn.bti.banco1000.utils.ExportarCSV;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de texto para interação com o sistema bancário.
 */
public class BancoGUI {

    private final ClienteController clienteController;
    private final ContaController contaController;

    public BancoGUI() {
        this.clienteController = new ClienteController();
        this.contaController = new ContaController();
    }

    public static void main(String[] args) {
        BancoGUI bancoGUI = new BancoGUI();
        bancoGUI.executar();
    }

    /**
     * Método principal que executa o menu do sistema bancário.
     */
    public void executar() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            exibirMenu();
            System.out.print("Escolha uma opção: ");
            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1 -> cadastrarCliente(scanner);
                case 2 -> criarConta(scanner);
                case 3 -> realizarDeposito(scanner);
                case 4 -> realizarSaque(scanner);
                case 5 -> realizarTransferencia(scanner);
                case 6 -> listarClientes();
                case 7 -> listarContas();
                case 8 -> exportarDados();
                case 0 -> {
                    System.out.println("Encerrando o sistema. Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("""
                === Banco 1000 ===
                1. Cadastrar Cliente
                2. Criar Conta
                3. Realizar Depósito
                4. Realizar Saque
                5. Realizar Transferência
                6. Listar Clientes
                7. Listar Contas
                8. Exportar Dados
                0. Sair
                """);
    }

    private void cadastrarCliente(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        try {
            clienteController.cadastrarCliente(nome, cpf, email, telefone);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void criarConta(Scanner scanner) {
        System.out.print("CPF do Cliente: ");
        String cpf = scanner.nextLine();
        Cliente cliente = clienteController.buscarClientePorCpf(cpf);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        try {
            System.out.print("Nome da conta: ");
            String nome = scanner.nextLine();
            System.out.print("Agência: ");
            int agencia = Integer.parseInt(scanner.nextLine());
            System.out.print("Número da Conta: ");
            int numeroConta = Integer.parseInt(scanner.nextLine());
            System.out.print("Tipo da Conta (1 - Corrente, 2 - Poupança, 3- Salario): ");
            int tipo = Integer.parseInt(scanner.nextLine());
            System.out.print("Senha da Conta: ");
            int senha = Integer.parseInt(scanner.nextLine());
            System.out.print("Saldo inicial: ");
            double saldo = Double.parseDouble(scanner.nextLine());

            Conta.TipoConta tipoConta = (tipo == 1) ? Conta.TipoConta.CORRENTE : Conta.TipoConta.POUPANCA;
            contaController.criarConta(nome, cliente, agencia, numeroConta, tipoConta, senha, saldo);
            System.out.println("Conta criada com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarDeposito(Scanner scanner) {
        try {
            System.out.print("Agência: ");
            int agencia = Integer.parseInt(scanner.nextLine());
            System.out.print("Número da Conta: ");
            int numeroConta = Integer.parseInt(scanner.nextLine());
            System.out.print("Valor do Depósito: ");
            double valor = Double.parseDouble(scanner.nextLine());

            Conta conta = contaController.buscarConta(agencia, numeroConta);

            if (conta == null) {
                System.out.println("Conta não encontrada.");
                return;
            }

            contaController.depositar(conta, valor);
            System.out.println("Depósito realizado com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarSaque(Scanner scanner) {
        try {
            System.out.print("Agência: ");
            int agencia = Integer.parseInt(scanner.nextLine());
            System.out.print("Número da Conta: ");
            int numeroConta = Integer.parseInt(scanner.nextLine());
            System.out.print("Valor do Saque: ");
            double valor = Double.parseDouble(scanner.nextLine());

            Conta conta = contaController.buscarConta(agencia, numeroConta);

            if (conta == null) {
                System.out.println("Conta não encontrada.");
                return;
            }

            contaController.sacar(conta, valor);
            System.out.println("Saque realizado com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite valores numéricos válidos.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarTransferencia(Scanner scanner) {
        try {
            System.out.print("Agência da Conta Origem: ");
            int agenciaOrigem = Integer.parseInt(scanner.nextLine());
            System.out.print("Número da Conta Origem: ");
            int numeroContaOrigem = Integer.parseInt(scanner.nextLine());
            System.out.print("Agência da Conta Destino: ");
            int agenciaDestino = Integer.parseInt(scanner.nextLine());
            System.out.print("Número da Conta Destino: ");
            int numeroContaDestino = Integer.parseInt(scanner.nextLine());
            System.out.print("Valor da Transferência: ");
            double valor = Double.parseDouble(scanner.nextLine());

            Conta contaOrigem = contaController.buscarConta(agenciaOrigem, numeroContaOrigem);
            Conta contaDestino = contaController.buscarConta(agenciaDestino, numeroContaDestino);

            if (contaOrigem == null || contaDestino == null) {
                System.out.println("Conta de origem ou destino não encontrada.");
                return;
            }

            contaController.transferir(contaOrigem, contaDestino, valor);
            System.out.println("Transferência realizada com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite valores numéricos válidos.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarClientes() {
        System.out.println("=== Clientes Cadastrados ===");
        clienteController.listarClientes().forEach(cliente -> 
                System.out.println("Nome: " + cliente.getNome() + ", CPF: " + cliente.getCpf()));
    }

    private void listarContas() {
        System.out.println("=== Contas Cadastradas ===");
        contaController.listarContas().forEach(conta -> 
                System.out.println("Conta: " + conta.getNumeroConta() + ", Agência: " + conta.getAgencia() +
                        ", Cliente: " + conta.getCliente().getNome() + ", Saldo: " + conta.getSaldo()));
    }

    private void exportarDados() {
        String pastaDestino = "dados_exportados";
        File pasta = new File(pastaDestino);

        if (!pasta.exists()) {
            pasta.mkdir();
        }

        try {
            clienteController.exportarClientesCsv(pastaDestino + File.separator + "clientes.csv");
            contaController.exportarContasCsv(pastaDestino + File.separator + "contas.csv");
            System.out.println("Dados exportados com sucesso! Arquivos salvos em: " + pastaDestino);
        } catch (Exception e) {
            System.out.println("Erro ao exportar dados: " + e.getMessage());
        }
    }
}