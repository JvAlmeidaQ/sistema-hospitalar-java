

package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.exceptions.DadosInvalidosException;
import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Secretaria;
import br.ufjf.dcc025.model.util.ValidaDados;

public class SecretariaController {
    public void atualizarSecretaria(Secretaria secretaria, String senhaAtual,
                                    String novaSenha, String nome, String email) throws DadosInvalidosException {
        Autenticar autenticacao = new Autenticar();

        if(secretaria == null)
            throw new DadosInvalidosException("Paciente inexistente");

        if (nome == null || nome.length() < 2)
            throw new DadosInvalidosException("Nome invalido");

        if(!email.contains("@"))
            throw new DadosInvalidosException("Email invalido");

        secretaria.setNome(nome);
        secretaria.setEmail(email);

        if(novaSenha != null && !novaSenha.isBlank())
        {
            if(!autenticacao.validarSenha(secretaria, senhaAtual)) {
                throw new DadosInvalidosException("Senha Digitada Incorreta!");
            }
            secretaria.ValidacaoSetSenha(novaSenha);
        }


        DadosHospital.getInstance().salvarDados();
    }
}
