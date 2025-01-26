package br.ufrn.bti.banco1000.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Classe que representa uma movimentação em uma conta bancária.
 * Cada movimentação possui um tipo, número da conta associada, descrição, valor e data.
 */
public class Movimentacao {

    public enum TipoMovimentacao {
        DEPOSITO("Depósito"),
        SAQUE("Saque"),
        TRANSFERENCIA_ENVIADA("Transferência Enviada"),
        TRANSFERENCIA_RECEBIDA("Transferência Recebida");

        private final String descricao;

        TipoMovimentacao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    private Date data;
    private TipoMovimentacao tipo;
    private int numeroConta;
    private String descricao;
    private double valor;

    /**
     * Construtor da classe Movimentacao.
     *
     * @param tipo Tipo da movimentação.
     * @param numeroConta Número da conta associada à movimentação.
     * @param descricao Descrição da movimentação.
     * @param valor Valor envolvido na movimentação.
     */
    public Movimentacao(TipoMovimentacao tipo, int numeroConta, String descricao, double valor) {
        this(tipo, numeroConta, descricao, valor, new Date());
    }

    /**
     * Construtor com data específica (para carregar de arquivos CSV).
     *
     * @param tipo Tipo da movimentação.
     * @param numeroConta Número da conta associada.
     * @param descricao Descrição.
     * @param valor Valor.
     * @param data Data da movimentação.
     */
    public Movimentacao(TipoMovimentacao tipo, int numeroConta, String descricao, double valor, Date data) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da movimentação deve ser positivo.");
        }
        this.tipo = tipo;
        this.numeroConta = numeroConta;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    // Getters e Setters
    public Date getData() {
        return data;
    }

    public String getDataFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(data);
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor deve ser positivo.");
        }
        this.valor = valor;
    }

    // Métodos para Persistência em CSV
    public String toCsv() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.join(",",
                tipo.name(),
                String.valueOf(numeroConta),
                descricao,
                String.valueOf(valor),
                sdf.format(data)
        );
    }

    public static Movimentacao fromCsv(String csv) {
        String[] campos = csv.split(",");
        if (campos.length != 5) {
            throw new IllegalArgumentException("CSV inválido para criação de movimentação.");
        }

        TipoMovimentacao tipo = TipoMovimentacao.valueOf(campos[0]);
        int numeroConta = Integer.parseInt(campos[1]);
        String descricao = campos[2];
        double valor = Double.parseDouble(campos[3]);
        Date data;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            data = sdf.parse(campos[4]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Data inválida no CSV.");
        }

        return new Movimentacao(tipo, numeroConta, descricao, valor, data);
    }

    // Métodos auxiliares
    @Override
    public String toString() {
        return "Movimentacao{" +
                "data=" + getDataFormatada() +
                ", tipo=" + tipo.getDescricao() +
                ", numeroConta=" + numeroConta +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movimentacao that = (Movimentacao) o;
        return numeroConta == that.numeroConta &&
                Double.compare(that.valor, valor) == 0 &&
                tipo == that.tipo &&
                Objects.equals(data, that.data) &&
                Objects.equals(descricao, that.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, tipo, numeroConta, descricao, valor);
    }
}