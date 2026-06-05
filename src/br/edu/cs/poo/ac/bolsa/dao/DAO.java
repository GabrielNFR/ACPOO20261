package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoObjetoJaExistente;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoOobjetoNaoExistente;
import br.edu.cs.poo.ac.bolsa.util.Registro;

public class DAO <T extends Registro>{
    private CadastroObjetos cadastro;
    private Class<T> tipo;                          

    public DAO(Class<T> tipo) {
        this.tipo = tipo;                          
        cadastro = new CadastroObjetos(tipo);
    }

    public void incluir(T registro) throws ExcecaoObjetoJaExistente {
        if (cadastro.buscar(registro.getIdentificador()) != null) {
            throw new ExcecaoObjetoJaExistente();
        }
        cadastro.incluir(registro, registro.getIdentificador());
    }

    public void alterar(T registro) throws ExcecaoOobjetoNaoExistente {
        if (cadastro.buscar(registro.getIdentificador()) == null) {
            throw new ExcecaoOobjetoNaoExistente();
        }
        cadastro.alterar(registro, registro.getIdentificador());
    }

    public void excluir(String id) throws ExcecaoOobjetoNaoExistente {
        if (cadastro.buscar(id) == null) {
            throw new ExcecaoOobjetoNaoExistente();
        }
        cadastro.excluir(id);
    }

    public T buscar(String id) {
        return (T) cadastro.buscar(id);
    }

    public T[] buscarTodos() {
        java.io.Serializable[] ret = cadastro.buscarTodos();
        if (ret == null) {
            return null;
        }
        T[] lista = (T[]) java.lang.reflect.Array.newInstance(tipo, ret.length);
        for (int i = 0; i < ret.length; i++) {
            lista[i] = (T) ret[i];
        }
        return lista;
    }
}
