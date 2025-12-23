package br.ufjf.dcc025.model;

import java.util.List;

public class Medico extends Usuario {

    private List<HorarioAtendimento> horariosDisponiveis;
    private List<Consulta> consultasAgendadas;

    public Medico(String nome, String email, String senha, String cpf) {
        super(nome, email, senha, cpf);
    }

    public void alteraStatusVisitas(Paciente paciente, boolean status) {
        paciente.setVisitas(status);
    }

    public List<HorarioAtendimento> getHorariosDisponiveis()
    {
        return horariosDisponiveis;
    }
    public List<Consulta> getConsultasMarcadas()
    {
        return consultasAgendadas;
    }
}
