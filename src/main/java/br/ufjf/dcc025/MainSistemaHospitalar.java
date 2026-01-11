package br.ufjf.dcc025;

import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.*;
import br.ufjf.dcc025.view.MedicoView.TelaPrincipalMedico;
import br.ufjf.dcc025.view.SecretariaView.TelaPrincipalSecretaria;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {
// 1. LIMPEZA E DADOS INICIAIS
        DadosHospital.medicos.clear();
        DadosHospital.secretarias.clear();
        DadosHospital.pacientes.clear();
        DadosHospital.consultas.clear();

        System.out.println("--- Iniciando Carga de Dados Fake ---");

        // 2. CRIANDO ATORES
        // Médico
        Medico drHouse = new Medico("Dr. House", "house@hospital.com", "senha123", "45337449079", "Diagnóstico");
        DadosHospital.medicos.add(drHouse);

        // Secretária
        Secretaria pam = new Secretaria("Pam Beesly", "pam@hospital.com", "senha123", "92188480031");
        DadosHospital.secretarias.add(pam);

        // Paciente
        Endereco end = new Endereco("69053430", "MG", "JF", "Centro", "Rua A", "10", "");
        Paciente michael = new Paciente("Michael Scott", "20565589040", "michael@email.com", "123", "31934711947", end, "Unimed");
        DadosHospital.pacientes.add(michael);

        // 3. CRIANDO UMA CONSULTA PARA HOJE (Para testar o Dashboard)
        Consulta consultaHoje = new Consulta(
                drHouse,
                michael,
                new HorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(14, 0), LocalTime.of(15, 0), 60),
                LocalDate.now(), // HOJE
                StatusConsulta.AGENDADA
        );
        drHouse.novaConsulta(consultaHoje);
        DadosHospital.consultas.add(consultaHoje);

        System.out.println("Dados carregados com sucesso!");

        // 4. LANÇANDO AS TELAS (Lado a Lado)
        SwingUtilities.invokeLater(() -> {

            TelaPrincipalSecretaria telaSec = new TelaPrincipalSecretaria(pam);
            telaSec.setVisible(true);

            TelaPrincipalMedico telaMed = new TelaPrincipalMedico(drHouse);
            telaMed.setVisible(true);

        });
    }
}
