package br.edu.cs.poo.ac.bolsa.negocio;

import java.time.LocalDate;

import java.math.BigDecimal;
import java.security.MessageDigest;

import br.edu.cs.poo.ac.bolsa.dao.InvestidorEmpresaDAO;
import br.edu.cs.poo.ac.bolsa.dao.InvestidorPessoaDAO;
import br.edu.cs.poo.ac.bolsa.entidade.Endereco;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.Contatos;
import br.edu.cs.poo.ac.bolsa.util.MensagensValidacao;
import br.edu.cs.poo.ac.bolsa.util.ResultadoValidacao;
import br.edu.cs.poo.ac.bolsa.util.ValidadorCpfCnpj;

public class InvestidorMediator {
    private InvestidorEmpresaDAO daoInvEmp = new InvestidorEmpresaDAO();
    private InvestidorPessoaDAO daoInvPes = new InvestidorPessoaDAO();

    private MensagensValidacao validarEndereco(Endereco endereco)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty())
        {
            msgs.adicionar("Logradouro é obrigatório.");
        }
        if (endereco.getNumero() == null || endereco.getNumero().trim().isEmpty())
        {
            msgs.adicionar("Número é obrigatório.");
        }
        if (endereco.getPais() == null || endereco.getPais().trim().isEmpty())
        {
            msgs.adicionar("País é obrigatório.");
        }
        if (endereco.getEstado() == null || endereco.getEstado().trim().isEmpty())
        {
            msgs.adicionar("Estado é obrigatório.");
        }
        if (endereco.getCidade() == null || endereco.getCidade().trim().isEmpty())
        {
            msgs.adicionar("Cidade é obrigatório.");
        }

        return msgs;
    }

    private MensagensValidacao validarContatos(Contatos contatos, boolean ehPessoaJuridica)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        if (contatos.getEmail() == null || contatos.getEmail().trim().isEmpty() || !contatos.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
        {
            msgs.adicionar("E-mail inválido.");
        }

        boolean fixoPreenchido = contatos.getTelefoneFixo() != null && !contatos.getTelefoneFixo().trim().isEmpty();
        boolean celularPreenchido = contatos.getTelefoneCelular() != null && !contatos.getTelefoneCelular().trim().isEmpty();
        boolean whatsappPreenchido = contatos.getNumeroWhatsApp() != null && !contatos.getNumeroWhatsApp().trim().isEmpty();

        if (!fixoPreenchido && !celularPreenchido && !whatsappPreenchido)
        {
            msgs.adicionar("Pelo menos um telefone deve ser informado.");
        }

        if (fixoPreenchido && !contatos.getTelefoneFixo().matches("^[0-9]+$"))
        {
            msgs.adicionar("Telefone fixo deve conter apenas números.");
        }
        if (celularPreenchido && !contatos.getTelefoneCelular().matches("^[0-9]+$"))
        {
            msgs.adicionar("Telefone celular deve conter apenas números.");
        }
        if (whatsappPreenchido && !contatos.getNumeroWhatsApp().matches("^[0-9]+$"))
        {
            msgs.adicionar("Número de Whatsapp deve conter apenas números.");
        }
        
        if (ehPessoaJuridica)
        {
            if (contatos.getNomeParaContato() == null || contatos.getNomeParaContato().trim().isEmpty())
            {
                msgs.adicionar("Nome para contato é obrigatório para pessoa jurídica.");
            }
        }

        return msgs;
    }

    private MensagensValidacao validar(DadosInvestidor dadosInv)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        if (dadosInv == null)
        {
            msgs.adicionar("Investidor não informado.");
            return msgs;
        }

        if (dadosInv.getNome() == null || dadosInv.getNome().trim().isEmpty())
        {
            msgs.adicionar("Nome é obrigatório.");
        }
        if (dadosInv.getEndereco() == null)
        {
            msgs.adicionar("Endereço é obrigatório.");
        }
        else
        {
            msgs.adicionar(validarEndereco(dadosInv.getEndereco()));
        }

        if (dadosInv.getDataCriacao() == null)
        {
            msgs.adicionar("Data de criação é obrigatória.");
        }
        else if (dadosInv.getDataCriacao().isAfter(LocalDate.now()))
        {
            msgs.adicionar("Data de criação não pode ser maior que a data atual.");
        }

        if (dadosInv.getBonus() == null)
        {
            msgs.adicionar("Bônus é obrigatório.");
        }
        else if (dadosInv.getBonus().compareTo(BigDecimal.ZERO) < 0)
        {
            msgs.adicionar("Bônus deve ser maior ou igual a zero.");
        }

        if (dadosInv.getContatos() == null)
        {
            msgs.adicionar("Contato é obrigatório.");
        }
        else
        {
            msgs.adicionar(validarContatos(dadosInv.getContatos(), dadosInv.ehInvestidorEmpresa()));
        }

        return msgs;
    }

    private MensagensValidacao validarInvestidorEmpresa(InvestidorEmpresa ie)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        if (ie == null)
        {
            msgs.adicionar("Investidor Empresa não existente.");
            return msgs;
        }

        DadosInvestidor dadosInv = new DadosInvestidor(ie, null);
        msgs.adicionar(validar(dadosInv));

        ResultadoValidacao resultadoCnpj = ValidadorCpfCnpj.validarCnpj(ie.getCnpj());

        if (resultadoCnpj != null)
        {
            msgs.adicionar("CNPJ inválido.");
        }

        if (ie.getFaturamento() < 100000.0)
        {
            msgs.adicionar("Faturamento deve ser maior ou igual a 100000.0.");
        }

        return msgs;
    }

    private MensagensValidacao validarInvestidorPessoa(InvestidorPessoa ip)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        if (ip == null)
        {
            msgs.adicionar("Investidor Pessoa não existente.");
            return msgs;
        }

        DadosInvestidor dadosInv = new DadosInvestidor(null, ip);
        msgs.adicionar(validar(dadosInv));

        ResultadoValidacao resultadoCpf = ValidadorCpfCnpj.validarCpf(ip.getCpf());

        if (resultadoCpf != null)
        {
            msgs.adicionar("CPF inválido.");
        }

        if (ip.getRenda() < 10000.0)
        {
            msgs.adicionar("Renda deve ser maior ou igual a 10000.0.");
        }
        else
        {
            for (br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda faixa : br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda.values())
            {
                if (ip.getRenda() >= faixa.getValorInicial() && ip.getRenda() <= faixa.getValorFinal())
                {
                    ip.setFaixaRenda(faixa);
                    break;
                }
            }

            if (ip.getFaixaRenda() == null)
            {
                msgs.adicionar("Não há faixa de renda compatível come esta renda.");
            }
        }

        return msgs;
    
    }

    public MensagensValidacao incluirInvestidorEmpresa(InvestidorEmpresa ie)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        msgs.adicionar(validarInvestidorEmpresa(ie));

        if (msgs.estaVazio() == true)
        {
            if (!daoInvEmp.incluir(ie))
            {
                msgs.adicionar("Investidor Empresa já existente.");
            }
        }

        return msgs;
    }

    public MensagensValidacao alterarInvestidorEmpresa(InvestidorEmpresa ie)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        msgs.adicionar(validarInvestidorEmpresa(ie));

        if (msgs.estaVazio() == true)
        {
            if (!daoInvEmp.alterar(ie))
            {
                msgs.adicionar("Investidor Empresa não existente.");
            }
        }

        return msgs;
    }

    public MensagensValidacao excluirInvestidorEmpresa(String cnpj)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        ResultadoValidacao resultadoCnpj = ValidadorCpfCnpj.validarCnpj(cnpj);

        if (resultadoCnpj != null)
        {
            msgs.adicionar("CNPJ inválido.");
            return msgs;
        }

        if (msgs.estaVazio() == true)
        {
            if (!daoInvEmp.excluir(cnpj))
            {
                msgs.adicionar("Investidor Empresa não existente.");
            }
        }

        return msgs;
    }

    public InvestidorEmpresa buscarInvestidorEmpresa(String cnpj)
    {
        ResultadoValidacao resultadoCnpj = ValidadorCpfCnpj.validarCnpj(cnpj); 
        
        if (resultadoCnpj != null)
        {
            return null;
        }

        return daoInvEmp.buscar(cnpj);
    }

    public MensagensValidacao incluirInvestidorPessoa(InvestidorPessoa ip)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        msgs.adicionar(validarInvestidorPessoa(ip));

        if (msgs.estaVazio() == true)
        {
            if (!daoInvPes.incluir(ip))
            {
                msgs.adicionar("Investidor Pessoa já existente.");
            }
        }

        return msgs;
    }

    public MensagensValidacao alterarInvestidorPessoa(InvestidorPessoa ip)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        msgs.adicionar(validarInvestidorPessoa(ip));

        if (msgs.estaVazio() == true)
        {
            if (!daoInvPes.alterar(ip))
            {
                msgs.adicionar("Investidor Pessoa não existente.");
            }
        }

        return msgs;
    }

    public MensagensValidacao excluirInvestidorPessoa(String cpf)
    {
        MensagensValidacao msgs = new MensagensValidacao();

        ResultadoValidacao resultadoCpf = ValidadorCpfCnpj.validarCpf(cpf);

        if (resultadoCpf != null)
        {
            msgs.adicionar("CPF inválido.");
            return msgs;
        }

        if (msgs.estaVazio() == true)
        {
            if (!daoInvPes.excluir(cpf))
            {
                msgs.adicionar("Investidor Pessoa não existente.");
            }
        }

        return msgs;
    }

    public InvestidorPessoa buscarInvestidorPessoa(String cpf)
    {
        ResultadoValidacao resultadoCpf = ValidadorCpfCnpj.validarCpf(cpf); 
        
        if (resultadoCpf != null)
        {
            return null;
        }

        return daoInvPes.buscar(cpf);
    }
}
