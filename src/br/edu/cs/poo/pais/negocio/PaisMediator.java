package br.edu.cs.poo.pais.negocio;
import java.time.LocalDate;

import br.edu.cs.poo.pais.dao.DAOPais;
import br.edu.cs.poo.pais.entidade.Pais;

public class PaisMediator {
	private DAOPais daoPais = new DAOPais();
	
	public String incluir(Pais pais) {
		String ret = validar(pais);
		if (ret == null) {
			boolean incluido = daoPais.incluir(pais);
			if (!incluido) {
				ret = "País já existente";
			}			
		}
		return ret;
	}
	public String alterar(Pais pais) {
		String ret = validar(pais);
		if (ret == null) {
			boolean alterado = daoPais.alterar(pais);
			if (!alterado) {
				ret = "País não existente";
			}			
		}
		return ret;
	}
	public String excluir(String sigla) {
		if (sigla == null || sigla.isEmpty()) {
			return "Sigla não informada";
		}
		boolean excluido = daoPais.excluir(sigla);
		if (!excluido) {
			return "País não existente";
		}			
		return null;
	}
	public Pais buscar(String sigla) {
		if (sigla != null && !sigla.isEmpty()) {
			return daoPais.buscar(sigla);
		}
		return null;
	}

	private String validar(Pais pais) {
		if (pais == null) {
			return "País não informado";
		}
		if (pais.getSigla() == null ||
				pais.getSigla().isEmpty()) {
			return "Sigla não informada";
		}
		if (pais.getSigla().length() != 2) {
			return "Sigla não tem dois caracteres";
		}
		if (pais.getNome() == null ||
				pais.getNome().isEmpty()) {
			return "Nome não informado";
		}
		if (pais.getDataFundacao() != null && 
				pais.getDataFundacao().isAfter(LocalDate.now())) {
			return "Data de validação é posterior à data atual";
		}
		return null;
	}
}
