package br.ufjf.dcc025.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Medico extends Usuario {

    private String especialidade;
    private List<HorarioAtendimento> horarioDeTrabalho;
    private transient List<Consulta> consultasAgendadas;
    private Boolean status;

    public Medico(String nome, String email, String senha, String cpf, String especialidade) {

        super(nome, email, senha, cpf);
        this.especialidade = especialidade;
        this.horarioDeTrabalho = new ArrayList<>();
        this.consultasAgendadas = new ArrayList<>();
        this.status = true;
    }

    public void adicionarHorarioAtendimento(DiasDaSemana dia, LocalTime horaInicio, LocalTime horaFim, int duracaoAtendimento)
    {
        HorarioAtendimento horarioAtendimento = new HorarioAtendimento(dia, horaInicio, horaFim, duracaoAtendimento);
        this.horarioDeTrabalho.add(horarioAtendimento);
    }

    public List<HorarioAtendimento> getHorarioDeTrabalho()
    {
        return Collections.unmodifiableList(horarioDeTrabalho);
    }

    public List<LocalTime> slotsParaConsultas(DiasDaSemana dia)
    {
        List<LocalTime> slotsParaConsultas = new ArrayList<>();

        LocalTime horaInicio, horaFim;
        for(HorarioAtendimento ht : this.getHorarioDeTrabalho())
        {
            if(ht.getDia().equals(dia))
            {
                horaInicio = ht.getInicio();
                horaFim = ht.getFim();
                if(ht.getDuracaoAtendimento() <= 0)
                    continue;
                while(horaInicio.isBefore(horaFim))
                {
                    slotsParaConsultas.add(horaInicio);
                    horaInicio = horaInicio.plusMinutes(ht.getDuracaoAtendimento());
                }
            }
        }
        return slotsParaConsultas;
    }
    public List<Consulta> consultasMarcadas()
    {
        if(this.consultasAgendadas == null)
            this.consultasAgendadas = new ArrayList<>();
        return Collections.unmodifiableList(consultasAgendadas);
    }

    public void novaConsulta(Consulta consulta)
    {
        this.consultasAgendadas.add(consulta);
    }

    public int duracaoAtendimento(DiasDaSemana dia)
    {
        for(HorarioAtendimento ht : this.getHorarioDeTrabalho())
        {
            if(ht.getDia().equals(dia))
                return ht.getDuracaoAtendimento();
        }
        return 0;
    }
    public void alteraStatusVisitas(Paciente paciente, boolean status) {
        paciente.setPodeReceberVisitas(status);
    }
    public void alteraStatusPaciente(Paciente paciente, boolean internado, boolean aptoVisita) {
        if(internado){
            paciente.setInternado(true);
            paciente.setPodeReceberVisitas(aptoVisita);
        }
        else{
            paciente.setInternado(false);
            paciente.setPodeReceberVisitas(false);
        }
    }

    public String getEspecialidade(){ return especialidade; }
    public void setEspecialidade(String novaEspecialidade){ this.especialidade = novaEspecialidade; }

    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
