package br.ufrn.bti.banco1000.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Classe que representa uma movimentação em uma conta bancária.
 * Cada movimentação possui um tipo, cliente associado, descrição, valor e data.
 * 
 * @author vinicius
 */
public class Movimentacao {

    public enum TipoMovimentacao {
        DEPOSITO, SAQUE, TRANSFERENCIA_ENVIADA, TRANSFERENCIA_RECEBIDA, MANUTENCAO, RENDIMENTO
    }

    private Date data;
    private TipoMovimentacao tipo;
    private Cliente cliente;
    private String descricao;
    private double valor;

    /**
     * Construtor da classe Movimentacao.
     *
     * @param tipo Tipo da movimentação.
     * @param cliente Cliente associado à movimentação.
     * @param descricao Descrição da movimentação.
     * @param valor Valor envolvido na movimentação.
     */
    public Movimentacao(TipoMovimentacao tipo, Cliente cliente, String descricao, double valor) {
        this.data = new Date();
        this.tipo = tipo;
        this.cliente = cliente;
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDataFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(data);
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        this.valor = valor;
    }

    // Métodos auxiliares
    @Override
    public String toString() {
        return "Movimentacao{" +
                "data=" + getDataFormatada() +
                ", tipo=" + tipo +
                ", cliente=" + cliente.getNome() +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movimentacao that = (Movimentacao) o;
        return Double.compare(that.valor, valor) == 0 &&
                Objects.equals(data, that.data) &&
                tipo == that.tipo &&
                Objects.equals(cliente, that.cliente) &&
                Objects.equals(descricao, that.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, tipo, cliente, descricao, valor);
    }
}