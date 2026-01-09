package br.ufjf.dcc025;

import br.ufjf.dcc025.controller.AutenticarEdicaoPerfil;
import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.TelaEdicaoMedico; // Essa Importação não deve exisitr
import br.ufjf.dcc025.view.TelaEdicaoPaciente; // Essa Importação não deve exisitr
import br.ufjf.dcc025.view.TelaEdicaoSecretaria; // Essa Importação não deve exisitr
import br.ufjf.dcc025.view.TelaLogin; // Essa Importação não deve exisitr

import javax.swing.*;

public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {

       DadosHospital.carregarDados();

       AutenticarEdicaoPerfil autenticarEdicaoPerfil = new AutenticarEdicaoPerfil();

       Secretaria secretaria = new Secretaria("Julia", "JuliaSecretaria@gmail.com", "juSec1428@", "84770895070");
       Medico house = new Medico("Dr.House", "House@gmail.com", "456", "12906714607", "Geral");
       Endereco enderco = new Endereco("123", "Dos Bobos", "0", "", "Martelos", "Barbacena", "MG");
       Paciente doente = new Paciente("Snow", "12906714607", "g@g.com", "123", "32991333288", enderco, "Ipseng");

        SwingUtilities.invokeLater(()-> {
            //new TelaEdicaoPaciente(doente).setVisible(true);
            //new TelaEdicaoMedico(house).setVisible(true);
            //new TelaEdicaoSecretaria(autenticarEdicaoPerfil, secretaria).setVisible(true);
            new TelaLogin().setVisible(true);
        });
        /*
       Medico house = new Medico("Dr.House", "House@gmail.com", "456", "", "Geral");

       house.adicionarHorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(14,30), LocalTime.of(15,30), 60);

       Endereco endereco = new Endereco("23062851", "João God", "88", "","Almeidas", "Barbacena", "MG" );
       Paciente p = new Paciente("João", "", "joao@gmail.com", "123", "32984329170", endereco, "Sus");

       Secretaria secretaria = new Secretaria("Julia", "JuliaSecretaria@gmail.com", "juSec1428@", "84770895070");

       secretaria.cadastrarMedicos(house);
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


        if (DadosHospital.pacientes.isEmpty()) {
            System.out.println("ERRO: Nenhhum paciente carregado. Rode a rotina de criação primeiro.");
            return;
        }

        p = DadosHospital.pacientes.get(0);
        System.out.println("--- Teste de Integridade: Paciente " + p.getNome() + " ---");

        List<Consulta> consultasDoPaciente = p.getMinhasConsultas();
        System.out.println("Consultas recuperadas: " + consultasDoPaciente.size());

        if (!consultasDoPaciente.isEmpty()) {
            Consulta c = consultasDoPaciente.getFirst();

            System.out.println("Médico da consulta: " + c.getMedico().getNome());


            if (!c.getDocumentoMedico().isEmpty()) {
                System.out.println("Documento anexo: " + c.getDocumentoMedico().getFirst().getClass().getSimpleName());
            } else {
                System.out.println("AVISO: Consulta sem documentos.");
            }
        } else {
            System.out.println("ERRO GRAVE: As consultas existem no arquivo mas não foram vinculadas ao paciente na memória.");
        }*/


    }
}
