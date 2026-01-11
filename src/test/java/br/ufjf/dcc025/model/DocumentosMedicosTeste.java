package br.ufjf.dcc025.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DocumentosMedicosTeste {

    @Test
    public void testPolimorfismoDocumentos() {

        Medico medico = new Medico("Dr teste Documento", "medico@gmail.com", "123", "79539836050", "Geral");
        Paciente paciente = new Paciente("Paciente teste Documento", "40132753090", "paciente@gmail.com", "123", "38933379247", null, "SUS");

        List<String> remedios = Arrays.asList("Dipirona", "Vitamina C");
        ReceitaMedica receita = new ReceitaMedica(medico, paciente, "Gripe", remedios, LocalDateTime.now());

        AtestadoMedico atestado = new AtestadoMedico(medico, paciente, "Repouso", 5, LocalDateTime.now());

        ExameMedico exame = new ExameMedico(medico, paciente, "Hemograma", "Aguardando", "Checkup", LocalDateTime.now());

        Assert.assertTrue(receita instanceof DocumentoMedico);
        Assert.assertTrue(atestado instanceof DocumentoMedico);
        Assert.assertTrue(exame instanceof DocumentoMedico);

        Assert.assertEquals("Gripe", receita.doenca);
        Assert.assertEquals(5, atestado.getDiasAfastamento());
        Assert.assertEquals("Hemograma", exame.getTipoDeExame());
    }
}