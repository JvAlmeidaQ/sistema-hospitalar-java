package br.ufjf.dcc025.model;

import org.junit.Assert;
import org.junit.Test;

public class TestesClassesSimples {

    @Test
    public void testeEnderecoFormatado() {
        Endereco endereco = new Endereco("36000-000", "Centro", "82", " ", "Rio Branco", "Juiz de Fora", "MG");

        Assert.assertEquals("Juiz de Fora", endereco.getCidade());
        Assert.assertEquals("MG", endereco.getEstado());

        Assert.assertNotNull(endereco.toString());
    }

    @Test
    public void testeFluxoStatusConsulta() {

        StatusConsulta status = StatusConsulta.AGENDADA;

        Assert.assertEquals("AGENDADA", status.name());

        // Simula mudança de estado
        status = StatusConsulta.CONCLUIDA;
        Assert.assertEquals(StatusConsulta.CONCLUIDA, status);
    }

    @Test
    public void testeHerancaFuncionario() {

        Medico m = new Medico("medicoTeste", "medico@gmail.com", "Qw1!Er", "80594589061", "Geral");

        Assert.assertEquals("medicoTeste", m.getNome());
        Assert.assertNotNull(m.getHorarioDeTrabalho());
    }
}