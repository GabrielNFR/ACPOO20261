package br.edu.cs.poo.pais.dao;

import br.edu.cs.poo.ac.bolsa.dao.DAOGenerico;
import br.edu.cs.poo.ac.bolsa.entidade.Ativo;
import br.edu.cs.poo.pais.entidade.Pais;

public class DAOPais extends DAOGenerico {
	public DAOPais() {
		inicializarCadastro(Pais.class);
	}
	public Pais buscar(String sigla) {
		return (Pais)cadastro.buscar(sigla);
	}	
	public boolean incluir(Pais pais) {		
		if (buscar(pais.getSigla()) == null) {
			cadastro.incluir(pais, "" + pais.getSigla());
			return true; 
		} else {
			return false;
		}
	}
	public boolean alterar(Pais pais) {
		if (buscar(pais.getSigla()) != null) {
			cadastro.alterar(pais, "" + pais.getSigla());
			return true; 
		} else {
			return false;
		}
	}
	public boolean excluir(String sigla) {
		if (buscar(sigla) != null) {
			cadastro.excluir(sigla);
			return true; 
		} else {
			return false;
		}
	}
}
