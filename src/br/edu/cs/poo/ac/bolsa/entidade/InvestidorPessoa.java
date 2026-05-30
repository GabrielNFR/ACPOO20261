package br.edu.cs.poo.ac.bolsa.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.cs.poo.ac.bolsa.util.Comparavel;

public class InvestidorPessoa extends Investidor implements Comparavel {
    
    private String cpf;
    private double renda;
    private FaixaRenda faixaRenda;

    public InvestidorPessoa(){}
    
    public InvestidorPessoa(String nome, Contatos contatos, LocalDate dataNascimento, BigDecimal bonus, Endereco endereco, String cpf, double renda, FaixaRenda faixaRenda)
    {
        super(nome, contatos, dataNascimento, bonus, endereco);
        this.cpf = cpf;
        this.renda = renda;
        this.faixaRenda = faixaRenda;
    }

    @Override
    public int comparar(Comparavel comp) {    
        if (!(comp instanceof InvestidorPessoa)) {
            throw new RuntimeException("O argumento nao e do tipo InvestidorPessoa");
        }
        InvestidorPessoa outro = (InvestidorPessoa)comp;
        return this.getNome().compareTo(outro.getNome());
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
