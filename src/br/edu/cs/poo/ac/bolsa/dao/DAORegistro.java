package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.util.Registro;

public class DAORegistro extends DAOGenerico{   
    public DAORegistro(Class<?> tipo) {
        inicializarCadastro(tipo);
    }

    public Registro buscar(String id) {
        return (Registro) cadastro.buscar(id);
    }

    public boolean incluir(Registro registro) {
        if (buscar(registro.getIdentificador()) == null) {
            cadastro.incluir(registro, registro.getIdentificador());
            return true;
        } else {
            return false;
        }
    }

    public boolean alterar(Registro registro) {
        if (buscar(registro.getIdentificador()) != null) {
            cadastro.alterar(registro, registro.getIdentificador());
            return true;
        } else {
            return false;
        }
    }

    public boolean excluir(String id) {
        if (buscar(id) != null) {
            cadastro.excluir(id);
            return true;
        } else {
            return false;
        }
    }
}
