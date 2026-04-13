package br.edu.cs.poo.pais.apresentacao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import br.edu.cs.poo.pais.entidade.Pais;
import br.edu.cs.poo.pais.negocio.PaisMediator;

public class TelaPais {

    private static Scanner sc = new Scanner(System.in);
    private static PaisMediator mediator = new PaisMediator();
    private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcao;

        do {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma opcao: ");

            switch (opcao) {
                case 1 -> incluir();
                case 2 -> alterar();
                case 3 -> excluir();
                case 4 -> buscar();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opcao invalida");
            }

            System.out.println("\nPressione ENTER para continuar...");
            sc.nextLine();

        } while (opcao != 0);
    }

    private static void mostrarMenu() {
        System.out.println("====================================");
        System.out.println("        CRUD DE PAISES");
        System.out.println("====================================");
        System.out.println("1 - Incluir Pais");
        System.out.println("2 - Alterar Pais");
        System.out.println("3 - Excluir Pais");
        System.out.println("4 - Buscar Pais");
        System.out.println("0 - Sair");
        System.out.println("====================================");
    }

    // ---------------------------------------------------------
    //  OPERACOES DO CRUD
    // ---------------------------------------------------------

    private static void incluir() {
        System.out.println("\n--- INCLUSAO DE PAIS ---");

        Pais pais = lerDadosPais();

        String ret = mediator.incluir(pais);

        if (ret == null) {
            System.out.println("Pais incluido com sucesso!");
        } else {
            System.out.println("Erro: " + ret);
        }
    }

    private static void alterar() {
        System.out.println("\n--- ALTERACAO DE PAIS ---");

        Pais pais = lerDadosPais();

        String ret = mediator.alterar(pais);

        if (ret == null) {
            System.out.println("Pais alterado com sucesso!");
        } else {
            System.out.println("Erro: " + ret);
        }
    }

    private static void excluir() {
        System.out.println("\n--- EXCLUSAO DE PAIS ---");

        String sigla = lerTexto("Informe a sigla do pais: ").toUpperCase();

        String ret = mediator.excluir(sigla);

        if (ret == null) {
            System.out.println("Pais excluido com sucesso!");
        } else {
            System.out.println("Erro: " + ret);
        }
    }

    private static void buscar() {
        System.out.println("\n--- BUSCA DE PAIS ---");

        String sigla = lerTexto("Informe a sigla do pais: ").toUpperCase();

        Pais pais = mediator.buscar(sigla);

        if (pais == null) {
            System.out.println("Pais nao encontrado.");
        } else {
            System.out.println("\n--- DADOS DO PAIS ---");
            System.out.println("Sigla: " + pais.getSigla());
            System.out.println("Nome: " + pais.getNome());
            System.out.println("Data de Fundacao: " +
                    (pais.getDataFundacao() != null ? pais.getDataFundacao().format(fmt) : "Nao informada"));
        }
    }

    // ---------------------------------------------------------
    //  METODOS AUXILIARES
    // ---------------------------------------------------------

    private static Pais lerDadosPais() {
        String sigla = lerTexto("Sigla (2 letras): ").toUpperCase();
        String nome = lerTexto("Nome: ");

        String dataStr = lerTexto("Data de fundacao (dd/MM/yyyy) ou ENTER para ignorar: ");

        LocalDate dataFundacao = null;
        if (!dataStr.isBlank()) {
            dataFundacao = LocalDate.parse(dataStr, fmt);
        }

        return new Pais(sigla, nome, dataFundacao);
    }

    private static String lerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }

    private static int lerInteiro(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido, tente novamente.");
            }
        }
    }
}