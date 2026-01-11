package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.model.util.TraduzDias;
import br.ufjf.dcc025.model.util.ValidaDados;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class AgendamentoController {

    public AgendamentoController() {}

    public List<LocalTime> disponibilidadeDeHorarioConsultas(Medico medico, LocalDate data)
    {
        DiasDaSemana dia = TraduzDias.traduzDias(data.getDayOfWeek());

        List<LocalTime> horariosDisponiveis = new ArrayList<>();

        boolean isHorarioLivre;
        for(LocalTime horarioParaConsulta : medico.slotsParaConsultas(dia))
        {
            isHorarioLivre = true;

            for(Consulta consultaMarcada : medico.getConsultasMarcadas())
            {
                if(consultaMarcada.getDataConsulta().equals(data))
                {
                    if(consultaMarcada.getHorarioConsulta().getInicio().equals(horarioParaConsulta)
                            && consultaMarcada.getStatusConsulta() != StatusConsulta.CANCELADA)
                        {
                            isHorarioLivre = false;
                            break;
                        }
                }
            }
            if(isHorarioLivre)
                horariosDisponiveis.add(horarioParaConsulta);
        }
        return horariosDisponiveis;
    }

    public List<String> listarEspecialidadesDisponiveis()
    {
        Set<String> especialidades = new HashSet<>();
        for(Medico m : DadosHospital.medicos)
        {
            if(m.getStatus())
            {
                especialidades.add(m.getEspecialidade());
            }
        }
        List<String> especialidadesDisponiveis = new ArrayList<>(especialidades);
        Collections.sort(especialidadesDisponiveis);
        return especialidadesDisponiveis;
    }

    public List<Medico> buscarMedicosPorEspecialidade(String especialidade)
    {
        List<Medico> medicos = new ArrayList<>();
        for(Medico m : DadosHospital.medicos)
        {
            if(m.getEspecialidade().equals(especialidade)
            && m.getStatus())
            {
                medicos.add(m);
            }
        }
        return medicos;
    }

    public List<Medico> medicosDisponiveisAgora(LocalDate data, LocalTime turno) {

        DiasDaSemana dia = TraduzDias.traduzDias(data.getDayOfWeek());

        List<Medico> medicos = new ArrayList<>();
        for (Medico medico : DadosHospital.medicos) {
            if(medico.getStatus() == false)
                continue;
            for(HorarioAtendimento horarios : medico.getHorarioDeTrabalho())
            {
                if(horarios.getDia() == dia) {
                    if (!turno.isBefore(horarios.getInicio()) && !turno.isAfter(horarios.getFim())) {
                        medicos.add(medico);
                        break;
                    }
                }
            }
        }
        return medicos;
    }

    public void agendarConsulta(Medico medico, Paciente paciente, LocalDate data, LocalTime horarioInicioConsulta) throws Exception
    {

        DiasDaSemana dia = TraduzDias.traduzDias(data.getDayOfWeek());

        List<LocalTime> horariosDisponiveis = this.disponibilidadeDeHorarioConsultas(medico, data);

        int duracaoConsulta = medico.duracaoAtendimento(dia);
        if(duracaoConsulta == 0)
            throw new Exception("O Medico "+ medico.getNome() + " Não realiza consultas nesse dia!");

        if(horariosDisponiveis.contains(horarioInicioConsulta))
        {
            LocalTime horarioFimConsulta = horarioInicioConsulta.plusMinutes(duracaoConsulta);

            HorarioAtendimento horarioDaConsulta = new HorarioAtendimento(dia,horarioInicioConsulta,horarioFimConsulta, duracaoConsulta);
            Consulta consulta = new Consulta(medico,paciente, horarioDaConsulta, data,StatusConsulta.AGENDADA);

            medico.novaConsulta(consulta);
            paciente.novaConsulta(consulta);
            DadosHospital.consultas.add(consulta);
            DadosHospital.salvarDados();
            return;
        }

        throw new IllegalArgumentException("Impossível Agendar Consulta, Horario Indisponível!");
    }

    public List<Consulta> monitoraFaltas()
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
        DadosHospital.salvarDados();
        return consultasFaltadas;
    }

    public void registrarFalta(Consulta consulta)
    {
        if(consulta.getStatusConsulta().equals(StatusConsulta.AGENDADA))
            consulta.setStatusConsulta(StatusConsulta.NAO_COMPARECEU);

        DadosHospital.salvarDados();
    }
}
