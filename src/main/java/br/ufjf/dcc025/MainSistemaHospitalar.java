package br.ufjf.dcc025;

import br.ufjf.dcc025.controller.Autenticar;
import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {

        /*DadosHospital.consultas.clear();
        DadosHospital.secretarias.clear();
        DadosHospital.pacientes.clear();
        DadosHospital.medicos.clear();*/

        DadosHospital.carregarDados();

        Medico almeida = new Medico("Almeida","almeida@gmail.com", "500", "81284544044", "Geral");
        Medico ph = new Medico("Pedro", "pedro@gmail.com", "24300", "60597854092", "Geral");

        almeida.adicionarHorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(14,0), LocalTime.of(19,0), 45);
        almeida.adicionarHorarioAtendimento(DiasDaSemana.QUINTA, LocalTime.of(14,0), LocalTime.of(19,0), 45);
        almeida.adicionarHorarioAtendimento(DiasDaSemana.SEXTA, LocalTime.of(14,0), LocalTime.of(19,0), 45);

        ph.adicionarHorarioAtendimento(DiasDaSemana.SEXTA, LocalTime.of(0,0), LocalTime.of(7,0), 30);
        ph.adicionarHorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(14,0), LocalTime.of(19,0), 45);

        Endereco endereco = new Endereco("74080290", "Dos Bobos", "55", "", "Martelos", "Barbacena", "MG");
        Paciente paciente = new Paciente("Teste", "60597854092", "teste@gmail.com", "84810", "34921905558",endereco,"SUS");


        Secretaria secretaria = new Secretaria("Julia", "JuliaSecretaria@gmail.com", "juSec1428@", "84770895070");
        secretaria.cadastrarMedicos(almeida);
        secretaria.cadastrarMedicos(ph);
        secretaria.cadastrarPaciente(paciente);
        DadosHospital.secretarias.add(secretaria);

        HorarioAtendimento horarioAtendimento = new HorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(15,15), LocalTime.of(16,0), 45);
        Consulta consulta = new Consulta(almeida, paciente, horarioAtendimento, LocalDate.now(), StatusConsulta.AGENDADA);
        DadosHospital.consultas.add(consulta);

        almeida.novaConsulta(consulta);
        paciente.novaConsulta(consulta);

        SwingUtilities.invokeLater(()-> {
            new TelaPrincipalMedico(almeida).setVisible(true);
        });

        //DadosHospital.salvarDados();
    }
}
