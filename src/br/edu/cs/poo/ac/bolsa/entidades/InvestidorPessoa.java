package br.edu.cs.poo.ac.bolsa.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvestidorPessoa extends Investidor{
    
    private String cpf;
    private double renda;
    private FaixaRenda faixaRenda;

    public InvestidorPessoa(String nome, Contatos contatos, LocalDate dataNascimento, BigDecimal bonus, Endereco endereco, String cpf, double renda, FaixaRenda faixaRenda)
    {
        super(nome, contatos, dataNascimento, bonus, endereco);
        this.cpf = cpf;
        this.renda = renda;
        this.faixaRenda = faixaRenda;
    }

    public LocalDate getDataNascimento()
    {
        return super.getDataCriacao();
    }

    public void setDataNascimento(LocalDate dataAbertura)
    {
        super.setDataCriacao(dataAbertura);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getRenda() {
        return renda;
    }

    public void setRenda(double renda) {
        this.renda = renda;
    }

    public FaixaRenda getFaixaRenda() {
        return faixaRenda;
    }

    public void setFaixaRenda(FaixaRenda faixaRenda) {
        this.faixaRenda = faixaRenda;
    }
}
