package br.ufjf.dcc025;

import br.ufjf.dcc025.controller.Autenticar;
import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.TelaLogin; // Essa Importação não deve exisitr

import javax.swing.*;
import java.time.LocalTime;

public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {

       //DadosHospital.carregarDados();

       Autenticar autenticar = new Autenticar();

       Secretaria secretaria = new Secretaria("Julia", "JuliaSecretaria@gmail.com", "juSec1428@", "84770895070");
       Medico house = new Medico("Dr.House", "House@gmail.com", "456", "12906714607", "Geral");
       //Endereco enderco = new Endereco("123", "Dos Bobos", "0", "", "Martelos", "Barbacena", "MG");
       //Paciente doente = new Paciente("Snow", "12906714607", "g@g.com", "123", "32991333288", enderco, "Ipseng");

        /*SwingUtilities.invokeLater(()-> {
            //new TelaEdicaoPaciente(doente).setVisible(true);
            //new TelaEdicaoMedico(house).setVisible(true);
            //new TelaEdicaoSecretaria(autenticarEdicaoPerfil, secretaria).setVisible(true);
            new TelaLogin().setVisible(true);
        });*/

        house.adicionarHorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(10,0), LocalTime.of(16, 0), 60);
        house.adicionarHorarioAtendimento(DiasDaSemana.TERCA, LocalTime.of(10,0), LocalTime.of(16, 0), 60);
        house.adicionarHorarioAtendimento(DiasDaSemana.QUINTA, LocalTime.of(10,0), LocalTime.of(16, 0), 60);
        house.adicionarHorarioAtendimento(DiasDaSemana.SEXTA, LocalTime.of(10,0), LocalTime.of(16, 0), 60);

        for(HorarioAtendimento ht : house.getHorarioDeTrabalho()) {
            String horarioTrabalho = ht.getDia().toString() + " | Inicio do Atendimento: " + ht.getInicio().toString() + " | Fim do Atendimento: " + ht.getFim().toString() + " | Duração de Cada Atendimento: " + ht.getDuracaoAtendimento() + " minutos";
            System.out.println(horarioTrabalho);
        }

        for(LocalTime horarios : house.slotsParaConsultas(DiasDaSemana.SEGUNDA))
        {
            System.out.println("Horarios disponiveis: " + horarios);
        }
    }
}
