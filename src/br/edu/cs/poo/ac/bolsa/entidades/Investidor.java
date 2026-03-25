package br.edu.cs.poo.ac.bolsa.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class Investidor {
    
    private String nome;
    private Endereco endereco;
    private LocalDate dataCriacao;
    private BigDecimal bonus;
    private Contatos contatos;

    public Investidor(String nome, Contatos contatos, LocalDate dataCriacao, BigDecimal bonus, Endereco endereco) {
        this.nome = nome;
        this.contatos = contatos;
        this.dataCriacao = dataCriacao;
        this.bonus = bonus;
        this.endereco = endereco;
    }

    public int getIdade()
    {
        LocalDate dataAtual = LocalDate.now();
        Period periodo = Period.between(dataCriacao, dataAtual);

        return periodo.getYears();
    }

    public void creditarBonus(BigDecimal valor)
    {
        if (valor !=  null && valor.compareTo(BigDecimal.ZERO) > 0)
        {
            this.bonus = this.bonus.add(valor);
        }
    }

    public void debitarBonus(BigDecimal valor)
    {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) > 0)
        {
            if (this.bonus.compareTo(valor) >= 0)
            {
                this.bonus = this.bonus.subtract(valor);
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Contatos getContatos() {
        return contatos;
    }

    public void setContatos(Contatos contatos) {
        this.contatos = contatos;
    }

    protected LocalDate getDataCriacao() {
        return dataCriacao;
    }

    protected void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public BigDecimal getBonus() {
        return bonus;
    }
}
