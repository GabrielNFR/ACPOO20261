package br.edu.cs.poo.pais.entidade;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Pais implements Serializable {
	private final String sigla;
	private String nome;
	private LocalDate dataFundacao;
	public Pais(String sigla, String nome, LocalDate dataFundacao) {
		this.sigla = sigla;
		this.nome = nome;
		this.dataFundacao = dataFundacao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LocalDate getDataFundacao() {
		return dataFundacao;
	}
	public void setDataFundacao(LocalDate dataFundacao) {
		this.dataFundacao = dataFundacao;
	}
	public String getSigla() {
		return sigla;
	}
	public int getIdade() {
		return (int)ChronoUnit.YEARS.between(LocalDate.now(),
				dataFundacao);
	}
}
