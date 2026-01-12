//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.model.util.ValidaDados;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public void adicionarHorarioTrabalho(Medico medico, DiasDaSemana dia, LocalTime inicio, LocalTime fim, int duracaoMinutos) {
        if (inicio.isAfter(fim))
            throw new IllegalArgumentException("Horario de início deve ser antes do fim.");

        medico.adicionarHorarioAtendimento(dia, inicio, fim, duracaoMinutos);

        DadosHospital.salvarDados();
    }

    public void limparHorarios(Medico medico) {
        medico.limparHorarios();
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
        DadosHospital.salvarDados();
        }

    public void geraAtestado(Consulta consulta, int diasAfastamento, String doenca) throws Exception {
        if (consulta == null)
            throw new Exception("Consulta inválida.");
        AtestadoMedico novoAtestado = new AtestadoMedico(consulta.getMedico(), consulta.getPaciente(), doenca, diasAfastamento, LocalDateTime.now());
        consulta.adicionaDocumentoMedico(novoAtestado);

        DadosHospital.salvarDados();
    }

    public void geraReceita(Consulta consulta, String doenca, List<String> remedios) throws Exception {
        if (consulta == null) {
            throw new Exception("Consulta inválida.");
        }
        ReceitaMedica novaReceita = new ReceitaMedica(consulta.getMedico(), consulta.getPaciente(), doenca, remedios, LocalDateTime.now());
        consulta.adicionaDocumentoMedico(novaReceita);
        DadosHospital.salvarDados();
    }

    public List<Paciente> listarPacientesDoMedico(Medico medico) {
        Set<Paciente> pacientesUnicos = new HashSet<>();
        if (medico.getConsultasMarcadas() != null) {
            for (Consulta consulta : medico.getConsultasMarcadas()) {
                pacientesUnicos.add(consulta.getPaciente());
            }
        }
        List<Paciente> listaFinal = new ArrayList<>(pacientesUnicos);
        Collections.sort(listaFinal, (p1, p2) -> p1.getNome().compareToIgnoreCase(p2.getNome()));

        return listaFinal;
    }

    public String gerarTextoHistorico(Paciente p, Medico medicoLogado) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PRONTUÁRIO ELETRÔNICO: ").append(p.getNome().toUpperCase()).append(" ===\n\n");

        boolean encontrou = false;

        for (Consulta c : DadosHospital.consultas) {
            if (c.getPaciente().equals(p) && c.getStatusConsulta() == StatusConsulta.CONCLUIDA && c.getMedico().equals(medicoLogado)) {
                encontrou = true;
                sb.append("--------------------------------------------------\n");
                sb.append("DATA: ").append(c.getDataConsulta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
                sb.append("MÉDICO: ").append(c.getMedico().getNome()).append(" (").append(c.getMedico().getEspecialidade()).append(")\n");
                sb.append("HORÁRIO: ").append(c.getHorarioConsulta().getInicio()).append("\n");


                if (c.getDocumentoMedico() != null && !c.getDocumentoMedico().isEmpty()) {
                    sb.append("DOCUMENTOS EMITIDOS: ").append(c.getDocumentoMedico().size()).append("\n");
                }
                sb.append("\n");
            }
        }

        if (!encontrou) {
            sb.append("Nenhum histórico de consultas realizadas encontrado para este paciente.");
        }

        return sb.toString();
    }

    public List<Consulta> consultasDoDia(Medico medico) {
        List<Consulta> consultasDoDia = new ArrayList<>();
            for(Consulta consulta : medico.getConsultasMarcadas()) {
                if(consulta.getDataConsulta().equals(LocalDate.now()))
                {
                    consultasDoDia.add(consulta);
                }
            }

            consultasDoDia.sort(Comparator.comparing(Consulta::getDataHoraConsulta));
            return consultasDoDia;
    }

    public void finalizarConsulta(Consulta consulta) {
        consulta.setStatusConsulta(StatusConsulta.CONCLUIDA);
        DadosHospital.salvarDados();
    }
}