package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.util.ValidaDados;

public class MedicoController {
    public void atualizarMedico(Medico medico, String senhaAtual,
                                String novaSenha, String nome, String email, String convenio) throws Exception {
        Autenticar autenticacao = new Autenticar();

        if(medico == null)
            throw new Exception("Medico inexistente");

        if (nome == null || nome.length() < 2)
            throw new Exception("Nome invalido");

        if(!email.contains("@"))
            throw new Exception("Email invalido");

        if(convenio == null || convenio.trim().isEmpty())
            throw new Exception("Convenio invalido");

        medico.setNome(nome);
        medico.setEmail(email);

        if(novaSenha != null && !novaSenha.isBlank())
        {
            if(!autenticacao.validarSenha(medico, senhaAtual)) {
                throw new Exception("Senha Digitada Incorreta!");
            }
            medico.ValidacaoSetSenha(novaSenha);
        }


        DadosHospital.salvarDados();
    }

    public void alterarStatusMedicos(Medico medico, boolean novoStatus) {
        if (DadosHospital.medicos.contains(medico)) {
            medico.setStatus(novoStatus);
            DadosHospital.salvarDados();
            return;
        }
        throw  new IllegalArgumentException("Medico Inexistente");
    }
}

