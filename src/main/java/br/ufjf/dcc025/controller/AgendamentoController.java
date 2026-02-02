

package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.exceptions.ConsultaInvalidaException;
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
        for(Medico m : DadosHospital.getInstance().getMedicos())
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
        for(Medico m : DadosHospital.getInstance().getMedicos())
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
        for (Medico medico : DadosHospital.getInstance().getMedicos()) {
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

    public List<LocalDate> listarDiasTrabalhadosProximos30Dias(Medico medico) {
        List<LocalDate> diasValidos = new ArrayList<>();
        LocalDate dataAnalise = LocalDate.now();


        for (int i = 0; i < 30; i++) {
            DiasDaSemana diaSemanaAnalise = TraduzDias.traduzDias(dataAnalise.getDayOfWeek());

            for (HorarioAtendimento h : medico.getHorarioDeTrabalho()) {
                if (h.getDia() == diaSemanaAnalise) {
                    diasValidos.add(dataAnalise);
                    break;
                }
            }

            dataAnalise = dataAnalise.plusDays(1);
        }

        return diasValidos;
    }

    public void agendarConsulta(Medico medico, Paciente paciente, LocalDate data, LocalTime horarioInicioConsulta) throws Exception
    {

        DiasDaSemana dia = TraduzDias.traduzDias(data.getDayOfWeek());

        List<LocalTime> horariosDisponiveis = this.disponibilidadeDeHorarioConsultas(medico, data);

        int duracaoConsulta = medico.duracaoAtendimento(dia);
        if(duracaoConsulta == 0)
            throw new ConsultaInvalidaException("O Medico "+ medico.getNome() + " Não realiza consultas nesse dia!");

        if(horariosDisponiveis.contains(horarioInicioConsulta))
        {
            LocalTime horarioFimConsulta = horarioInicioConsulta.plusMinutes(duracaoConsulta);

            HorarioAtendimento horarioDaConsulta = new HorarioAtendimento(dia,horarioInicioConsulta,horarioFimConsulta, duracaoConsulta);
            Consulta consulta = new Consulta(medico,paciente, horarioDaConsulta, data,StatusConsulta.AGENDADA);

            medico.novaConsulta(consulta);
            paciente.novaConsulta(consulta);
           DadosHospital.getInstance().addConsulta(consulta);
            DadosHospital.getInstance().salvarDados();
            return;
        }

        throw new ConsultaInvalidaException("Impossível Agendar Consulta, Horario Indisponível!");
    }

    public List<Consulta> monitoraFaltas()
    {
        List<Consulta> consultasFaltadas = new ArrayList<>();
        LocalDateTime agora =  LocalDateTime.now();

        for(Consulta consulta : DadosHospital.getInstance().getConsultas())
        {
            if(consulta.getDataHoraConsulta().isBefore(agora) && consulta.getStatusConsulta() == StatusConsulta.AGENDADA){
                consulta.setStatusConsulta(StatusConsulta.NAO_COMPARECEU);
                consultasFaltadas.add(consulta);
            }
        }
        DadosHospital.getInstance().salvarDados();
        return consultasFaltadas;
    }

    public String gerarRelatorioNotificacoes(Medico medico) {
        StringBuilder relatorio = new StringBuilder();
        LocalDateTime agora = LocalDateTime.now();
        int novasConsultas = 0;
        int faltasRecentes = 0;

        for (Consulta c : DadosHospital.getInstance().getConsultas()) {
            if (c.getMedico().equals(medico)) {

                if (c.getStatusConsulta() == StatusConsulta.NAO_COMPARECEU &&
                        c.getDataConsulta().isAfter(LocalDate.now().minusDays(2))) { // Faltas dos últimos 2 dias
                    relatorio.append("- FALTA: ").append(c.getPaciente().getNome())
                            .append(" em ").append(c.getDataConsulta()).append("\n");
                    faltasRecentes++;
                }

                if (c.getStatusConsulta() == StatusConsulta.AGENDADA &&
                        c.getDataConsulta().isAfter(LocalDate.now().minusDays(1))) {
                    novasConsultas++;
                }
            }
        }

        if (faltasRecentes > 0 || novasConsultas > 0) {
            return "Resumo de Atualizações:\n\n" +
                    "📅 Consultas Futuras/Hoje: " + novasConsultas + "\n" +
                    "⚠️ Faltas Recentes: " + faltasRecentes + "\n" +
                    relatorio.toString();
        }
        return null;
    }
}
