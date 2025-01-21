package br.ufrn.bti.banco1000;

import br.ufrn.bti.banco1000.gui.BancoGUI;
import br.ufrn.bti.banco1000.service.BancoService;

/**
 * Classe principal para inicializar o sistema bancário.
 */
public class Main {

    /**
     * Método principal que inicia a aplicação.
     *
     * @param args Argumentos de linha de comando.
     */
    public static void main(String[] args) {
        System.out.println("Iniciando o sistema Banco 1000...");

        BancoService bancoService = new BancoService();

        List<Agencia> agencias = bancoService.carregarAgencias();
        List<Conta> contas = bancoService.carregarContas();
        List<Movimentacao> movimentacoes = bancoService.carregarMovimentacoes();

        BancoGUI bancoGUI = new BancoGUI();
        bancoGUI.executar();
    }
}