package br.edu.cs.poo.ac.bolsa.negocio;

import java.math.BigDecimal;
import br.edu.cs.poo.ac.bolsa.dao.DAO;
import br.edu.cs.poo.ac.bolsa.entidade.Ativo;
import br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.Investidor;
import br.edu.cs.poo.ac.bolsa.entidade.Titulo;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoNegocio;
import br.edu.cs.poo.ac.bolsa.util.MensagensValidacao;
import java.time.LocalDate;
import br.edu.cs.poo.ac.bolsa.entidade.StatusTitulo;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoObjetoJaExistente;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoOobjetoNaoExistente;

public class TituloMediator {
    private DAO<Titulo> daoTitulo;
    private AtivoMediator ativoMediator;
    private InvestidorMediator investidorMediator;

    private static TituloMediator instancia = new TituloMediator();

    private TituloMediator() {
        daoTitulo = new DAO<>(Titulo.class);
        ativoMediator = AtivoMediator.getInstancia();
        investidorMediator = new InvestidorMediator();
    }

    public static TituloMediator getInstancia() {
        return instancia;
    }

    public void incluir(DadosTitulo dados) throws ExcecaoNegocio {
        MensagensValidacao msgs = new MensagensValidacao();
        
        if (dados.getCpfOuCnpj() == null || dados.getCpfOuCnpj().trim().isEmpty()) {
            msgs.adicionar("CPF/CNPJ inválido");
        }

        if (dados.getCodigoAtivo() <= 0) {
            msgs.adicionar("Código do ativo inválido");
        }

        if (dados.getValorInvestido() == null) {
            msgs.adicionar("Valor investido não pode ser nulo");
        }

        if (dados.getTaxaDiaria() == null) {
            msgs.adicionar("Taxa diária não pode ser nula");
        }

        Ativo ativo = null;
        if (dados.getCodigoAtivo() > 0) {
            ativo = ativoMediator.buscar(dados.getCodigoAtivo());
            if (ativo == null) {
                msgs.adicionar("Ativo não encontrado");
            }
        }

        Investidor investidor = null;
        if (dados.getCpfOuCnpj() != null && !dados.getCpfOuCnpj().trim().isEmpty()) {
            investidor = investidorMediator.buscarInvestidor(dados.getCpfOuCnpj());
            if (investidor == null) {
                msgs.adicionar("Investidor não encontrado");
            }
        }

        if (dados.getValorInvestido() != null && ativo != null) {
            BigDecimal valorMin = BigDecimal.valueOf(ativo.getValorMinimoAplicacao());
            BigDecimal valorMax = BigDecimal.valueOf(ativo.getValorMaximoAplicacao());
            if (dados.getValorInvestido().compareTo(valorMin) < 0 || dados.getValorInvestido().compareTo(valorMax) > 0) {
                msgs.adicionar("Valor investido fora da faixa permitida");
            }
        }

        if (dados.getTaxaDiaria() != null && ativo != null) {
            BigDecimal taxaDecimal = dados.getTaxaDiaria().divide(new BigDecimal("100"));
            BigDecimal base = BigDecimal.ONE.add(taxaDecimal);
            BigDecimal pot = base.pow(30);
            BigDecimal taxaMensal = new BigDecimal("100").multiply(pot.subtract(BigDecimal.ONE));
            BigDecimal min = BigDecimal.valueOf(ativo.getTaxaMensalMinima());
            BigDecimal max = BigDecimal.valueOf(ativo.getTaxaMensalMaxima());
            if (taxaMensal.compareTo(min) < 0 || taxaMensal.compareTo(max) > 0) {
                msgs.adicionar("Taxa fora da faixa permitida");
            }
        }

        if (investidor != null && ativo != null) {
            BigDecimal entrada = investidor.getEntradaFinanceira();
            FaixaRenda faixaInvestidor = null;
            for (FaixaRenda f : FaixaRenda.values()) {
                double valor = entrada.doubleValue();
                if (valor >= f.getValorInicial() && valor <= f.getValorFinal()) {
                    faixaInvestidor = f;
                    break;
                }
            }
            if (faixaInvestidor == null || faixaInvestidor.getCodigo() < ativo.getFaixaMinimaPermitida().getCodigo()) {
                msgs.adicionar("Faixa de renda incompatível");
            }
        }

        if (!msgs.estaVazio()) {
            throw new ExcecaoNegocio(msgs);
        }

        Titulo titulo = new Titulo(investidor, ativo, dados.getValorInvestido(), dados.getValorInvestido(), dados.getTaxaDiaria(),
        LocalDate.now(), LocalDate.now().plusMonths(ativo.getPrazoEmMeses()), null, StatusTitulo.ATIVO);

        try {
            daoTitulo.incluir(titulo);
        } catch (ExcecaoObjetoJaExistente e) {
            msgs.adicionar("Titulo ja existente");
            throw new ExcecaoNegocio(msgs);
        }
    }

    public void processarRendimentos() {
        Titulo[] titulos = daoTitulo.buscarTodos();

        if (titulos == null) {
            return;
        }

        for (int i = 0; i < titulos.length; i++) {
            Titulo titulo = titulos[i];
            boolean sucesso = titulo.render();

            if (sucesso == true) {
                BigDecimal diferenca = titulo.getValorAtual().subtract(titulo.getValorInvestido());
                BigDecimal bonus = diferenca.multiply(new BigDecimal("0.01").divide(new BigDecimal("100")));
                String id = titulo.getInvestidor().getIdentificador();
                Investidor inv = investidorMediator.buscarInvestidor(id);

                inv.creditarBonus(bonus);
                investidorMediator.alterarInvestidor(inv);
            }
            if (!titulo.getDataVencimento().isAfter(LocalDate.now())) {
                titulo.setStatus(StatusTitulo.VENCIDO);
            }

            try {
                daoTitulo.alterar(titulo);
            } catch (ExcecaoOobjetoNaoExistente e) {
            }
        }
    }

    public void cancelarTitulo(String numero) throws ExcecaoNegocio {
        MensagensValidacao msgs = new MensagensValidacao();
        Titulo titulo = daoTitulo.buscar(numero);

        if (titulo == null) {
            msgs.adicionar("Título não encontrado");
            throw new ExcecaoNegocio(msgs);
        }

        if (titulo.getStatus() == StatusTitulo.VENCIDO || titulo.getStatus() == StatusTitulo.CANCELADO) {
            msgs.adicionar("Título não pode ser cancelado");
            throw new ExcecaoNegocio(msgs);
        }

        titulo.setStatus(StatusTitulo.CANCELADO);
        try {
            daoTitulo.alterar(titulo);
        } catch (ExcecaoOobjetoNaoExistente e) {
        }

        Investidor inv = investidorMediator.buscarInvestidor(titulo.getInvestidor().getIdentificador());
        BigDecimal valorDebito = inv.getBonus().multiply(new BigDecimal("0.7"));
        inv.debitarBonus(valorDebito);
        investidorMediator.alterarInvestidor(inv);
    }
}
