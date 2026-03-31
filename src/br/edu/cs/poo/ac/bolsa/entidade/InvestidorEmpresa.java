package br.edu.cs.poo.ac.bolsa.entidade;

import java.time.LocalDate;
import java.math.BigDecimal;

public class InvestidorEmpresa extends Investidor {
    private String cnpj;
    private double faturamento;

    public InvestidorEmpresa(String nome, Contatos contatos, LocalDate dataAbertura, BigDecimal bonus, Endereco endereco, String cnpj, double faturamento)
    {
        super(nome, contatos, dataAbertura, bonus, endereco);
        this.cnpj = cnpj;
        this.faturamento = faturamento;
    }

   public LocalDate getDataAbertura()
   {
    return super.getDataCriacao();
   }

   public void setDataAbertura(LocalDate dataAbertura)
   {
    super.setDataCriacao(dataAbertura);
   }
   
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public double getFaturamento() {
        return faturamento;
    }

    public void setFaturamento(double faturamento) {
        this.faturamento = faturamento;
    }
}
