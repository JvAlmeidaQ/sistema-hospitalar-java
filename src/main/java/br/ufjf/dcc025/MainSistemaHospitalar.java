package br.ufjf.dcc025;

import br.ufjf.dcc025.model.HorarioAtendimento;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Usuario;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Hello world!
 *
 */
public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {
        String c = "Carlos";
        String email = "CarlosAlmeida@gmail.com";
        String senha = "A06k28@";
        String cpf = "11249175607";
        Medico medico = new Medico(c, email, senha, cpf);

        System.out.println("Nome do Medico: " +  medico.getNome());
        System.out.println("CPF do Medico: " +  medico.getCpf());
        System.out.println("Email do Medico: " +  medico.getEmail());

        medico.setHorariosDisponiveis(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(18, 0), 30 );
        medico.setHorariosDisponiveis(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(15, 0), 1 );

        System.out.println("Horarios de Trabalho do medico " + medico.getNome());
        for( HorarioAtendimento h :  medico.getHorariosDisponiveis())
            {
                DayOfWeek dia = h.getDia();
            LocalTime horaInicio = h.getInicio();
            LocalTime horaFim = h.getFim();
            int duracaoAtendimento =  h.getDuracaoAtendimento();
            System.out.println("Dia: " + dia + " Horario de Incio:  " + horaInicio + " Horario de Fim: " + horaFim +  " Duração de cada Atendimento: " + duracaoAtendimento);
            }

    }
}
