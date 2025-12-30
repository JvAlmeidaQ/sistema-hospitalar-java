package br.ufjf.dcc025.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Secretaria extends Usuario {

    public Secretaria(String nome, String email, String senha, String cpf) {
        super(nome, email, senha, cpf);
    }

    public void cadastrarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente Inexistente");
        }
        DadosHospital.pacientes.add(paciente);
    }

    public boolean autorizarVisitas(Paciente paciente) {
        if (DadosHospital.pacientes.contains(paciente)){
            if (paciente.getPodeReceberVisitas())
                return true;
            else
                return false;
        }
        throw new IllegalArgumentException("Paciente Inexistente");
    }

    public void cadastroMedicos(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("Medico Inexistente");
        }
        DadosHospital.medicos.add(medico);
    }

    public void setStatusMedicos(Medico medico, boolean status) {
        if (DadosHospital.medicos.contains(medico))
            medico.setStatus(status);
        throw  new IllegalArgumentException("Medico Inexistente");
    }

    public List<Medico> medicosAtivos(DiasDaSemana dia, LocalTime inicio, LocalTime fim) {
        List<Medico> medicos = new ArrayList<>();
        for (Medico medico : DadosHospital.medicos) {
            if(medico.getStatus() == true)
                continue;
            for(HorarioAtendimento horarios : medico.getHorariosDisponiveis())
            {
                if(horarios.getDia() == dia) {
                    if (!inicio.isBefore(horarios.getInicio()) && !fim.isAfter(horarios.getFim())) {
                        medicos.add(medico);
                        break;
                    }
                }
            }
        }
        return medicos;
    }

    public List<Consulta> monitoramentoFaltas()
    {
        List<Consulta> consultasFaltadas = new ArrayList<>();
        LocalDateTime agora =  LocalDateTime.now();

        for(Consulta consulta : DadosHospital.consultas)
        {
            if(consulta.getDataHoraConsulta().isBefore(agora) && consulta.getStatusConsulta() == StatusConsulta.AGENDADA){
                consulta.setStatusConsulta(StatusConsulta.NAO_COMPARECEU);
                consultasFaltadas.add(consulta);
            }
        }
        return consultasFaltadas;
    }
}
