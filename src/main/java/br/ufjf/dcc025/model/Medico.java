package br.ufjf.dcc025.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Medico extends Usuario {

    private String especialidade;
    private List<HorarioAtendimento> horariosDisponiveis;
    private transient List<Consulta> consultasAgendadas;
    private Boolean status;

    public Medico(String nome, String email, String senha, String cpf, String especialidade) {

        super(nome, email, senha, cpf);
        this.especialidade = especialidade;
        this.horariosDisponiveis = new ArrayList<>();
        this.consultasAgendadas = new ArrayList<>();
        this.status = true;
    }

    public void adicionarHorarioAtendimento(DiasDaSemana dia, LocalTime horaInicio, LocalTime horaFim, int duracaoAtendimento)
    {
        HorarioAtendimento horarioAtendimento = new HorarioAtendimento(dia, horaInicio, horaFim, duracaoAtendimento);
        this.horariosDisponiveis.add(horarioAtendimento);
    }

    public List<HorarioAtendimento> getHorariosDisponiveis()
    {
        return horariosDisponiveis; //mudar dps, para não retornar a lista original
    }

    public List<Consulta> getConsultasMarcadas()
    {
        if(this.consultasAgendadas == null)
            this.consultasAgendadas = new ArrayList<>();
        return consultasAgendadas; //mudar dps, para não retornar a lista original, criar metodo para adcionar(Controller);
    }

    public void alteraStatusVisitas(Paciente paciente, boolean status) {
        paciente.setPodeReceberVisitas(status);
    }

    public String getEspecialidade(){ return especialidade; }
    public void setEspecialidade(String novaEspecialidade){ this.especialidade = novaEspecialidade; }

    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
}
