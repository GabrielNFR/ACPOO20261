package br.edu.cs.poo.ac.bolsa.util;

public class ValidadorCpfCnpj {
    
    public static ResultadoValidacao validarCpf(String cpf)
    {
        if (cpf == null || cpf.trim().isEmpty())
        {
            return ResultadoValidacao.NAO_INFORMADO;
        }

        String numeros = cpf.replaceAll("[^0-9]", "");
        
        if (numeros.length() != 11 || todosDigitosIguais(numeros))
        {
            return ResultadoValidacao.FORMATO_INVALIDO;
        }

        int digito1 = calcularDigitoCpf(numeros.substring(0, 9), 10);
        int digito2 = calcularDigitoCpf(numeros.substring(0, 10), 11);

        String dvCalculado = "" + digito1 + digito2;
        String dvInformado = numeros.substring(9, 11);

        if (!dvCalculado.equals(dvInformado)) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        return null;
    }

    public static ResultadoValidacao validarCnpj(String cnpj)
    {
        if (cnpj == null || cnpj.trim().isEmpty())
        {
            return ResultadoValidacao.NAO_INFORMADO;
        }

        String numeros = cnpj.replaceAll("[^0-9]", "");

        if (numeros.length() != 14 || todosDigitosIguais(numeros))
        {
            return ResultadoValidacao.FORMATO_INVALIDO;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int digito1 = calcularDigitoCnpj(numeros.substring(0, 12), pesos1);
        int digito2 = calcularDigitoCnpj(numeros.substring(0, 13), pesos2);

        String dvCalculado = "" + digito1 + digito2;
        String dvInformado = numeros.substring(12, 14);

        if (!dvCalculado.equals(dvInformado)) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        return null;
    }

    private static boolean todosDigitosIguais(String documento)
    {
        return documento.matches("(\\d)\\1+");
    }

    private static int calcularDigitoCpf(String base, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesoInicial--;
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }

    private static int calcularDigitoCnpj(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }
}

