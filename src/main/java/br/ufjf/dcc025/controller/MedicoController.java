package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.model.util.ValidaDados;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void StatusPaciente(Medico medico, Paciente paciente, boolean internado, boolean aptoVisita){
        medico.alteraStatusPaciente(paciente, internado, aptoVisita);
    }

    public void alterarStatusMedicos(Medico medico, boolean novoStatus) {
        if (DadosHospital.medicos.contains(medico)) {
            medico.setStatus(novoStatus);
            DadosHospital.salvarDados();
            return;
        }
        throw  new IllegalArgumentException("Medico Inexistente");
    }

    public void geraExame(Consulta consulta, String tipoDeExame, String resultado, String doenca) throws Exception {
        if (consulta == null)
            throw new Exception("Consulta inválida.");
        ExameMedico novoExame = new ExameMedico(consulta.getMedico(), consulta.getPaciente(), tipoDeExame, resultado, null, LocalDateTime.now());
        consulta.adicionaDocumentoMedico(novoExame);
        }

    public void geraAtestado(Consulta consulta, int diasAfastamento, String doenca) throws Exception {
        if (consulta == null)
            throw new Exception("Consulta inválida.");
        AtestadoMedico novoAtestado = new AtestadoMedico(consulta.getMedico(), consulta.getPaciente(), doenca, diasAfastamento, LocalDateTime.now());
        consulta.adicionaDocumentoMedico(novoAtestado);

        // Opcional: Se precisar salvar em disco imediatamente
        // DadosHospital.salvarDados();
    }

    public void geraReceita(Consulta consulta, String doenca, List<String> remedios) throws Exception {
        if (consulta == null) {
            throw new Exception("Consulta inválida.");
        }
        ReceitaMedica novaReceita = new ReceitaMedica(consulta.getMedico(), consulta.getPaciente(), doenca, remedios, LocalDateTime.now());
        consulta.adicionaDocumentoMedico(novaReceita);
    }
}