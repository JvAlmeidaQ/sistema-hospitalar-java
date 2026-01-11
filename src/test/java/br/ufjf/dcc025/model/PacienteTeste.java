package br.ufjf.dcc025.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class PacienteTeste {

    @Test
    public void testCriacaoPacienteValido() {
        Paciente p = new Paciente("Nome", "07406115008", "paciente@gmail.com", "senha", "38937217714", null, "Unimed");
        assertEquals("Nome", p.getNome());
        assertEquals("Unimed", p.getConvenio());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCpfVazio() {
         new Paciente("Nome", "", "email", "senha", "tel", null, "SUS");
    }
}