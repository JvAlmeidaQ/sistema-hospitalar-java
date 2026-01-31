package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class agendamentoControllerTeste {

    private AgendamentoController controller;
    private Medico medico;
    private Paciente paciente;


    @Before
    public void setup() throws Exception {
        DadosHospital.getInstance().getConsultas().clear();
        DadosHospital.getInstance().getMedicos().clear();
        DadosHospital.getInstance().getPacientes().clear();

        controller = new AgendamentoController();


        medico = new Medico("Dr. Teste", "email@teste.com", "12389%PK!!j", "50884005003", "Geral");

        medico.adicionarHorarioAtendimento(DiasDaSemana.SEGUNDA, LocalTime.of(14, 0), LocalTime.of(15, 0), 60);

        paciente = new Paciente("Paciente Teste", "11750323010", "p@email.com", "123opKL$", "32926075284", null, "Unimed");
    }

    @Test
    public void testDeveListarHorarioLivre() {

        LocalDate segundaFeira = LocalDate.of(2026, 10, 12);


        List<LocalTime> horarios = controller.disponibilidadeDeHorarioConsultas(medico, segundaFeira);


        Assert.assertFalse("A lista não deve estar vazia", horarios.isEmpty());
        Assert.assertTrue("Deve conter o horário das 14:00", horarios.contains(LocalTime.of(14, 0)));
    }

    @Test
    public void testNaoDeveListarHorarioOcupado() throws Exception {

        LocalDate data = LocalDate.of(2026, 1, 12);
        LocalTime hora = LocalTime.of(14, 0);

        controller.agendarConsulta(medico, paciente, data, hora);

        List<LocalTime> horarios = controller.disponibilidadeDeHorarioConsultas(medico, data);

        Assert.assertFalse("Horário ocupado não deve aparecer na lista", horarios.contains(hora));
    }

    @Test(expected = Exception.class)
    public void testErroAoAgendarHorarioOcupado() throws Exception {

        LocalDate data = LocalDate.of(2026, 10, 12);
        LocalTime hora = LocalTime.of(14, 0);

        controller.agendarConsulta(medico, paciente, data, hora); //

        controller.agendarConsulta(medico, new Paciente("Outro", "CPF2", "e", "s", "t", null, "c"), data, hora);
    }
}