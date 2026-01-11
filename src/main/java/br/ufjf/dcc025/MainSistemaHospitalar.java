package br.ufjf.dcc025;

import br.ufjf.dcc025.controller.Autenticar;
import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.*;

import javax.swing.*;
import java.time.LocalTime;

public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {

        DadosHospital.carregarDados();

       Autenticar autenticar = new Autenticar();

//        DadosHospital.pacientes.clear();
//        DadosHospital.medicos.clear();
//        DadosHospital.secretarias.clear();

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

//        secretaria.cadastrarMedicos(almeida);
//        secretaria.cadastrarMedicos(ph);
//        secretaria.cadastrarPaciente(paciente);
//        DadosHospital.secretarias.add(secretaria);

        //DadosHospital.salvarDados();

        SwingUtilities.invokeLater(()-> {
            //new TelaEdicaoPaciente(doente).setVisible(true);
            //new TelaEdicaoMedico(house).setVisible(true);
            //new TelaEdicaoSecretaria(secretaria).setVisible(true);
            //new TelaLogin().setVisible(true);
            new TelaAgendamento(secretaria).setVisible(true);
        });
    }
}
