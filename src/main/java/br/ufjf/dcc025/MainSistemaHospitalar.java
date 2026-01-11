package br.ufjf.dcc025;

import br.ufjf.dcc025.controller.Autenticar;
import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.*;
import br.ufjf.dcc025.view.MedicoView.TelaAtendimentoMedico;
import br.ufjf.dcc025.view.MedicoView.TelaEdicaoMedico;
import br.ufjf.dcc025.view.MedicoView.TelaSelecaoPaciente;
import br.ufjf.dcc025.view.MedicoView.TelaStatusPaciente;
import br.ufjf.dcc025.view.PacienteView.TelaEdicaoPaciente;
import br.ufjf.dcc025.view.PacienteView.TelaPrincipalPaciente;
import br.ufjf.dcc025.view.PacienteView.TelaSelecaoMedico;
import br.ufjf.dcc025.view.SecretariaView.TelaEdicaoSecretaria;
import br.ufjf.dcc025.view.SecretariaView.TelaPrincipalSecretaria;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {

        DadosHospital.carregarDados();

       Autenticar autenticar = new Autenticar();

       Secretaria secretaria = new Secretaria("Julia", "JuliaSecretaria@gmail.com", "juSec1428@", "84770895070");
       Medico house = new Medico("Dr.House", "House@gmail.com", "456", "12906714607", "Geral");
       Endereco enderco = new Endereco("74080290", "Dos Bobos", "55", "", "Martelos", "Barbacena", "MG");
       Paciente doente = new Paciente("Snow", "12906714607", "g@g.com", "123", "32991333288", enderco, "Ipseng");
       HorarioAtendimento horarioAtendimento = new HorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(19,30), LocalTime.of(20,30), 60);
       Consulta consulta = new Consulta(house, doente, horarioAtendimento, LocalDate.of(2026, 1, 12), StatusConsulta.CONCLUIDA);
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

//        secretaria.cadastrarMedicos(almeida);
//        secretaria.cadastrarMedicos(ph);
//        secretaria.cadastrarPaciente(paciente);
//        DadosHospital.secretarias.add(secretaria);

        //DadosHospital.salvarDados();

        SwingUtilities.invokeLater(()-> {
            new TelaPrincipalPaciente(doente).setVisible(true);
            new TelaAtendimentoMedico(consulta).setVisible(true);
            new TelaEdicaoPaciente(doente).setVisible(true);
            new TelaEdicaoMedico(house).setVisible(true);
            new TelaEdicaoSecretaria(secretaria).setVisible(true);
            new TelaLogin().setVisible(true);
            new TelaAgendamento(secretaria).setVisible(true);
            new TelaSelecaoMedico(doente).setVisible(true);
            new TelaSelecaoPaciente(house).setVisible(true);
            new TelaSelecaoConsulta(doente, house).setVisible(true);
            new TelaSelecaoDocumento(consulta).setVisible(true);
            new TelaStatusPaciente(house, doente).setVisible(true);
            new TelaPrincipalSecretaria(secretaria).setVisible(true);
        });
    }
}
