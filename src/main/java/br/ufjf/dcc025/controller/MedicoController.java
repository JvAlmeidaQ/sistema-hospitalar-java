package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;
import br.ufjf.dcc025.model.util.ValidaDados;

public class MedicoController {
    public void atualizarMedico(Medico medico, String senhaAtual,
                                String novaSenha, String nome, String email, String convenio) throws Exception {
        Autenticar autenticacao = new Autenticar();

        if(medico == null)
            throw new Exception("Medico inexistente");

        if(autenticacao.validarSenha(medico, senhaAtual))
            throw new Exception("Senha Digitada Incorreta!");

        if (nome == null || nome.length() < 2)
            throw new Exception("Nome invalido");

        if(ValidaDados.validaEmail(email) == false)
            throw new Exception("Email invalido");

        if(convenio == null || convenio.trim().isEmpty())
            throw new Exception("Convenio invalido");

        medico.setNome(nome);
        medico.setEmail(email);

        if(!medico.ValidacaoSetSenha(novaSenha))
            throw new Exception("Nova Senha Invalida!");


        DadosHospital.salvarDados();
    }

    public void StatusPaciente(Medico medico, Paciente paciente, boolean internado, boolean aptoVisita){
        medico.alteraStatusPaciente(paciente, internado, aptoVisita);
    }
}

