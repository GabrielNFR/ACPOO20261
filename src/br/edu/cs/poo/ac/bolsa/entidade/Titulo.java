package br.edu.cs.poo.ac.bolsa.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.io.Serializable;

public class Titulo implements Serializable {
    private InvestidorPessoa investidorPessoa;
    private InvestidorEmpresa investidorEmpresa;
    private Ativo ativo;
    private BigDecimal valorInvestido;
    private BigDecimal valorAtual;
    private BigDecimal taxaDiaria;
    private LocalDate dataAplicacao;
    private LocalDate dataVencimento;
    private LocalDate dataUltimoRendimento;
    private StatusTitulo status;

    public Titulo(InvestidorPessoa investidorPessoa, InvestidorEmpresa investidorEmpresa, Ativo ativo, BigDecimal valorInvestido, BigDecimal valorAtual, BigDecimal taxaDiaria, 
        LocalDate dataAplicacao, LocalDate dataVencimento, LocalDate dataUltimoRendimento, StatusTitulo status)
    {
        this.investidorPessoa = investidorPessoa;
        this.investidorEmpresa = investidorEmpresa;
        this.ativo = ativo;
        this.valorInvestido = valorInvestido;
        this.valorAtual = valorAtual;
        this.taxaDiaria = taxaDiaria;
        this.dataAplicacao = dataAplicacao;
        this.dataVencimento = dataVencimento;
        this.dataUltimoRendimento = dataUltimoRendimento;
        this.status = status;
    }
    
    public boolean render()
    {
        LocalDate dataAtual = LocalDate.now();
        
        if (this.status != StatusTitulo.ATIVO) {
            return false;
        }
        if (!dataAtual.isBefore(this.dataVencimento)) { 
            return false;
        }
        if (!dataAtual.isAfter(this.dataAplicacao)) {  
            return false;
        }
        

        if (this.dataUltimoRendimento != null && !dataAtual.isAfter(this.dataUltimoRendimento)) {
            return false;
        }

        long dd = 0;
        if (this.dataUltimoRendimento == null) {
            dd = ChronoUnit.DAYS.between(this.dataAplicacao, dataAtual);
        } else {
            dd = ChronoUnit.DAYS.between(this.dataUltimoRendimento, dataAtual);
        }

        if (dd <= 0) return false;

        BigDecimal taxaDecimal = this.taxaDiaria.divide(new BigDecimal("100"));
        BigDecimal fator = BigDecimal.ONE.add(taxaDecimal);
        BigDecimal multiplicador = fator.pow((int) dd);

        this.valorAtual = this.valorAtual.multiply(multiplicador);
        
        this.dataUltimoRendimento = dataAtual;

        return true;
    }

    public String getNumero() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dataFormatada = this.dataAplicacao.format(formatador);
        
        if (this.investidorPessoa != null) {
            return "000" + this.investidorPessoa.getCpf() + this.ativo.getCodigo() + dataFormatada + "0000";
        } else if (this.investidorEmpresa != null) {
            return this.investidorEmpresa.getCnpj() + this.ativo.getCodigo() + dataFormatada + "0000";
        }
        return null;
    }

    public InvestidorPessoa getInvestidorPessoa() { return investidorPessoa; }
    public void setInvestidorPessoa(InvestidorPessoa investidorPessoa) { this.investidorPessoa = investidorPessoa; }

    public InvestidorEmpresa getInvestidorEmpresa() { return investidorEmpresa; }
    public void setInvestidorEmpresa(InvestidorEmpresa investidorEmpresa) { this.investidorEmpresa = investidorEmpresa; }

    public Ativo getAtivo() { return ativo; }
    public void setAtivo(Ativo ativo) { this.ativo = ativo; }

    public BigDecimal getValorInvestido() { return valorInvestido; }
    public void setValorInvestido(BigDecimal valorInvestido) { this.valorInvestido = valorInvestido; }

    public BigDecimal getValorAtual() { return valorAtual; }
    public void setValorAtual(BigDecimal valorAtual) { this.valorAtual = valorAtual; }

    public BigDecimal getTaxaDiaria() { return taxaDiaria; }
    public void setTaxaDiaria(BigDecimal taxaDiaria) { this.taxaDiaria = taxaDiaria; }

    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public LocalDate getDataUltimoRendimento() { return dataUltimoRendimento; }
    public void setDataUltimoRendimento(LocalDate dataUltimoRendimento) { this.dataUltimoRendimento = dataUltimoRendimento; }

    public StatusTitulo getStatus() { return status; }
    public void setStatus(StatusTitulo status) { this.status = status; }
}
