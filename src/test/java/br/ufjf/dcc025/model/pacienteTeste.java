package br.ufjf.dcc025.model;

import br.ufjf.dcc025.exceptions.DadosInvalidosException;
import org.junit.Test;
import static org.junit.Assert.*;

public class pacienteTeste {

    @Test
    public void testCriacaoPacienteValido() throws Exception {
        Paciente p = new Paciente("Nome", "07406115008", "paciente@gmail.com", "Xy9#Kp", "38937217714", null, "Unimed");
        assertEquals("Nome", p.getNome());
        assertEquals("Unimed", p.getConvenio());
    }

    @Test(expected = DadosInvalidosException.class)
    public void testCpfVazio() throws Exception {
         new Paciente("Nome", "", "email", "senha", "tel", null, "SUS");
    }
}