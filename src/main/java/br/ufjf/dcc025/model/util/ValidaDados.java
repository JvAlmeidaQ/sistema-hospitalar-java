package br.ufjf.dcc025.model.util;

import java.util.InputMismatchException;
import java.util.regex.Pattern;

public class ValidaDados {

    public static boolean  ValidaCPF(String cpf) {
        //validação cpf, seguindo algoritimo já conhecido
        if ((cpf.length() != 11) ||
                cpf.equals("00000000000") ||
                cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999"))
            return false;

        char digito10, digito11;
        int soma, i, resto, num, peso;

        try {
            soma = 0;
            peso = 10;
            for(i = 0; i < 9; i++)
            {
                num = (int)(cpf.charAt(i) - 48);
                soma += (num * peso);
                peso = peso - 1;
            }
            resto = 11 - (soma % 11);
            if(resto == 10 || resto == 11)
                digito10 = '0';
            else
                digito10 = (char)(resto + 48);

            soma = 0;
            peso = 11;
            for(i = 0; i < 10; i++){
                num = (int)(cpf.charAt(i) - 48);
                soma += (num * peso);
                peso = peso - 1;
            }

            resto = 11 - (soma % 11);
            if(resto == 10 || resto == 11)
                digito11 = '0';
            else
                digito11 = (char)(resto + 48);

            if(digito10 == cpf.charAt(9) &&  digito11 == cpf.charAt(10))
                return true;
            else
                return false;
        }
        catch(InputMismatchException erro) {
            return false;
        }
    }
    public static boolean validaEmail(String email) {
        //regex
        if(email == null || email.isEmpty())
            return false;

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regex, email);
    }
    public static boolean validaSenha(String senha) {
        if (senha.length() < 6) return false;

        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;
        for (char c : senha.toCharArray()) {
            if (c >= '0' && c <= '9') {
                achouNumero = true;
            } else if (c >= 'A' && c <= 'Z') {
                achouMaiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }
        }
        return achouNumero && achouMaiuscula && achouMinuscula && achouSimbolo;
    }
    //senha com +6 caracteres, necessario conter, Letras maiusculas e minisculas, numeros e caracteres especiais

    public static boolean validaTelefone(String telefone) {
        String regex = "^(?:\\(?\\d{2}\\)?\\s?)?9\\d{4}-?\\d{4}$";
        return Pattern.matches(regex, telefone);
    }

    public static boolean validaCEP(String cep) {
        if(cep ==  null || cep.isEmpty())
            return false;
        String regex = "^\\d{5}-?\\d{3}$";
        return Pattern.matches(regex, cep);
    }
}


