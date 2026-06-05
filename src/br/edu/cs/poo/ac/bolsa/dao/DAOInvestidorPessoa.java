package br.edu.cs.poo.ac.bolsa.dao;

import java.io.Serializable;

import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;

public class DAOInvestidorPessoa extends DAOGenerico{
    public DAOInvestidorPessoa()
    {
        inicializarCadastro(InvestidorPessoa.class);
    }
    public InvestidorPessoa buscar(String cpf) {
		return (InvestidorPessoa)cadastro.buscar(cpf);
	}
    public boolean incluir(InvestidorPessoa investidorPessoa) {		
		if (buscar(investidorPessoa.getCpf()) == null) {
			cadastro.incluir(investidorPessoa, investidorPessoa.getCpf());
			return true; 
		} else {
			return false;
		}
	}
    public boolean alterar(InvestidorPessoa investidorPessoa) {
		if (buscar(investidorPessoa.getCpf()) != null) {
			cadastro.alterar(investidorPessoa, investidorPessoa.getCpf());
			return true; 
		} else {
			return false;
		}
	}
	public boolean excluir(String cpf) {
		if (buscar(cpf) != null) {
			cadastro.excluir(cpf);
			return true; 
		} else {
			return false;
		}
	}

	public boolean incluirInvestidorPessoa(InvestidorPessoa investidorPessoa) {
		return incluir(investidorPessoa);
	}

	public InvestidorPessoa[] consultarTodos() {
		Serializable[] ret = cadastro.buscarTodos();
		if (ret == null) {
			return null;
		}
		InvestidorPessoa[] lista = new InvestidorPessoa[ret.length];
		for (int i = 0; i < ret.length; i++) {
			lista[i] = (InvestidorPessoa)ret[i];
		}
		return lista;
	}

	public InvestidorPessoa buscarInvestidorPessoa(String cpf) {
    return buscar(cpf);
}
}
