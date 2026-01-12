//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Secretaria;
import br.ufjf.dcc025.model.util.ValidaDados;

public class SecretariaController {
    public void atualizarSecretaria(Secretaria secretaria, String senhaAtual,
                                    String novaSenha, String nome, String email) throws Exception {
        Autenticar autenticacao = new Autenticar();

        if(secretaria == null)
            throw new Exception("Paciente inexistente");

        if (nome == null || nome.length() < 2)
            throw new Exception("Nome invalido");

        if(!email.contains("@"))
            throw new Exception("Email invalido");

        secretaria.setNome(nome);
        secretaria.setEmail(email);

        if(novaSenha != null && !novaSenha.isBlank())
        {
            if(!autenticacao.validarSenha(secretaria, senhaAtual)) {
                throw new Exception("Senha Digitada Incorreta!");
            }
            secretaria.ValidacaoSetSenha(novaSenha);
        }


        DadosHospital.salvarDados();
    }
}
