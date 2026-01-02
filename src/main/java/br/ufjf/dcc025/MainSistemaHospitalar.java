package br.ufjf.dcc025;

import br.ufjf.dcc025.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Hello world!
 *
 */
public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {
       DadosHospital.carregarDados();

       Medico house = new Medico("Dr.House", "House@gmail.com", "H0243615dr!", "11249175607");

       house.adicionarHorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(14,30), LocalTime.of(15,30), 60);

       Endereco endereco = new Endereco("23062851", "João God", "88", "","Almeidas", "Barbacena", "MG" );
       Paciente p = new Paciente("João", "11249175607", "joao@gmail.com", "123Joao?", "32984329170", endereco, "Sus");

       Secretaria secretaria = new Secretaria("Julia", "JuliaSecretaria@gmail.com", "juSec1428@", "84770895070");

       secretaria.cadastroMedicos(house);
       secretaria.cadastrarPaciente(p);


        HorarioAtendimento horarioConsulta = new HorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(14,30), LocalTime.of(15,30), 1);
        Consulta consulta = new Consulta(house, p, horarioConsulta, LocalDate.of(2025,1,5), StatusConsulta.AGENDADA);

       AtestadoMedico atestado = new AtestadoMedico(house,p,"Gripe", 2, LocalDateTime.of(2025,1,7,0,0));
       consulta.adicionaDocumentoMedico(atestado);

       DadosHospital.consultas.add(consulta);

       p.getMinhasConsultas().add(consulta);
       house.getConsultasMarcadas().add(consulta);

       DadosHospital.salvarDados();

       System.out.println("Testado com sucesso!");
    }
}
