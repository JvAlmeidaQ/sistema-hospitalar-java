package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.DiasDaSemana;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.DadosHospital;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.time.LocalTime;

public class MedicoControllerTeste {

    private MedicoController controller;
    private Medico medico;

    @Before
    public void setup() {
        DadosHospital.medicos.clear();
        controller = new MedicoController();
        medico = new Medico("Dr. House", "house@tv.com", "123", "70984896031", "Diagnóstico");
    }

    @Test
    public void testAdicionarHorarioValido() {

        controller.adicionarHorarioTrabalho(medico, DiasDaSemana.QUARTA, LocalTime.of(8,0), LocalTime.of(12,0), 30);

        Assert.assertEquals(1, medico.getHorarioDeTrabalho().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErroHorarioInicioAposFim() {
        controller.adicionarHorarioTrabalho(medico, DiasDaSemana.QUARTA, LocalTime.of(18,0), LocalTime.of(12,0), 30);
    }

    @Test
    public void testLimparHorarios() {
        controller.adicionarHorarioTrabalho(medico, DiasDaSemana.SEXTA, LocalTime.of(8,0), LocalTime.of(12,0), 30);
        Assert.assertFalse(medico.getHorarioDeTrabalho().isEmpty());

        controller.limparHorarios(medico);
        Assert.assertTrue("A lista de horários deve estar vazia após limpar", medico.getHorarioDeTrabalho().isEmpty());
    }
}