package br.ufrn.bti.banco1000.gui;

import br.ufrn.bti.banco1000.controller.ClienteController;
import br.ufrn.bti.banco1000.controller.ContaController;
import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;
import br.ufrn.bti.banco1000.model.Agencia;
import br.ufrn.bti.banco1000.service.BancoCsv;
import br.ufrn.bti.banco1000.model.ContaCorrente;
import br.ufrn.bti.banco1000.model.ContaPoupanca;
import br.ufrn.bti.banco1000.model.ContaSalario;

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
            System.out.println("=== Banco 1000 ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Criar Conta");
            System.out.println("3. Realizar Depósito");
            System.out.println("4. Realizar Saque");
            System.out.println("5. Realizar Transferência");
            System.out.println("6. Listar Clientes");
            System.out.println("7. Listar Contas");
            System.out.println("8. Atualizar dados de agências e contas");
            System.out.println("9. Exportar dados de agências e contas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha

            switch (opcao) {
                case 1 -> cadastrarCliente(scanner);
                case 2 -> criarConta(scanner);
                case 3 -> realizarDeposito(scanner);
                case 4 -> realizarSaque(scanner);
                case 5 -> realizarTransferencia(scanner);
                case 6 -> listarClientes();
                case 7 -> listarContas();
                case 8 -> carregarDadosDoCsv();
                case 9 -> salvarDadosNoCsv();
                case 0 -> {
                    System.out.println("Encerrando o sistema. Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
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

        System.out.print("Nome da conta: ");
        String nome = scanner.nextLine();
        System.out.print("Agência: ");
        int agencia = scanner.nextInt();
        System.out.print("Número da Conta: ");
        int numeroConta = scanner.nextInt();
        System.out.print("Senha da Conta: ");
        int senha = scanner.nextInt();
        System.out.print("Saldo inicial: ");
        double saldo = scanner.nextDouble();
        scanner.nextLine(); // Consumir quebra de linha

        System.out.println("Escolha o tipo de conta:");
        System.out.println("1. Conta Corrente");
        System.out.println("2. Conta Poupança");
        System.out.println("3. Conta Salário");
        System.out.print("Digite o número da opção: ");
        int tipoContaEscolhido = scanner.nextInt();
        scanner.nextLine();

        Conta.TipoConta tipoConta = null;
        double taxaManutencao = 0;
        double taxaRendimento = 0;
        String empregador = "";
        int limiteSaques = 0;

        switch (tipoContaEscolhido) {
            case 1:
                tipoConta = Conta.TipoConta.CORRENTE;
                System.out.print("Taxa de Manutenção: ");
                taxaManutencao = scanner.nextDouble();
                break;

            case 2:
                tipoConta = Conta.TipoConta.POUPANCA;
                System.out.print("Taxa de Rendimento: ");
                taxaRendimento = scanner.nextDouble();
                break;

            case 3:
                tipoConta = Conta.TipoConta.SALARIO;
                scanner.nextLine();
                System.out.print("Empregador: ");
                empregador = scanner.nextLine();
                System.out.print("Limite de Saques Mensais: ");
                limiteSaques = scanner.nextInt();
                break;

            default:
                System.out.println("Opção inválida.");
                return;
        }

        try {
            contaController.criarConta(nome, cliente, agencia, numeroConta, tipoConta, senha, saldo, taxaManutencao, taxaRendimento, empregador, limiteSaques);
            System.out.println("Conta criada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarDeposito(Scanner scanner) {
        System.out.print("Agência: ");
        int agencia = scanner.nextInt();
        System.out.print("Número da Conta: ");
        int numeroConta = scanner.nextInt();
        System.out.print("Valor do Depósito: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir quebra de linha

        Conta conta = contaController.buscarConta(agencia, numeroConta);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        try {
            contaController.depositar(conta, valor);
            System.out.println("Depósito realizado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarSaque(Scanner scanner) {
        System.out.print("Agência: ");
        int agencia = scanner.nextInt();
        System.out.print("Número da Conta: ");
        int numeroConta = scanner.nextInt();
        System.out.print("Valor do Saque: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir quebra de linha

        Conta conta = contaController.buscarConta(agencia, numeroConta);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        try {
            contaController.sacar(conta, valor);
            System.out.println("Saque realizado com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarTransferencia(Scanner scanner) {
        System.out.print("Agência da Conta Origem: ");
        int agenciaOrigem = scanner.nextInt();
        System.out.print("Número da Conta Origem: ");
        int numeroContaOrigem = scanner.nextInt();
        System.out.print("Agência da Conta Destino: ");
        int agenciaDestino = scanner.nextInt();
        System.out.print("Número da Conta Destino: ");
        int numeroContaDestino = scanner.nextInt();
        System.out.print("Valor da Transferência: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir quebra de linha

        Conta contaOrigem = contaController.buscarConta(agenciaOrigem, numeroContaOrigem);
        Conta contaDestino = contaController.buscarConta(agenciaDestino, numeroContaDestino);

        if (contaOrigem == null || contaDestino == null) {
            System.out.println("Conta de origem ou destino não encontrada.");
            return;
        }

        try {
            contaController.transferir(contaOrigem, contaDestino, valor);
            System.out.println("Transferência realizada com sucesso!");
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

    private void carregarDadosDoCsv() {
        BancoCsv.carregarDados();
        System.out.println("Dados de agências e contas atualizados com sucesso!");
    }

    private void salvarDadosNoCsv() {
        BancoCsv.salvarDados();
        System.out.println("Dados de agências e contas exportados com sucesso!");
    }
}