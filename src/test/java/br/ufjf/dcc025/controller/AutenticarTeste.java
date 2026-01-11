package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AutenticarTeste {

    private Autenticar controller;

    @Before
    public void setup() {
        DadosHospital.medicos.clear();
        DadosHospital.secretarias.clear();
        DadosHospital.pacientes.clear();

        controller = new Autenticar();

        Medico medico = new Medico("Dr teste Login", "medico@hospital.com", "senha123", "07406115008", "Geral");
        Secretaria secretaria = new Secretaria("Sec teste Login", "sec@hospital.com", "senha123", "96962912056");
        Paciente paciente = new Paciente("Paciente teste Login", "96962912056", "pac@hospital.com", "senha123", "37923538209", null, "Unimed");

        DadosHospital.medicos.add(medico);
        DadosHospital.secretarias.add(secretaria);
        DadosHospital.pacientes.add(paciente);
    }

    @Test
    public void testLoginMedicoSucesso() {
        Usuario usuario = controller.login("medico@hospital.com", "senha123");
        Assert.assertNotNull("Deveria retornar um usuário", usuario);
        Assert.assertTrue("O usuário deve ser do tipo Medico", usuario instanceof Medico);
    }

    @Test
    public void testLoginSecretariaSucesso() {
        Usuario usuario = controller.login("sec@hospital.com", "senha123");
        Assert.assertTrue("O usuário deve ser do tipo Secretaria", usuario instanceof Secretaria);
    }

    @Test
    public void testLoginFalhaSenhaIncorreta() {
        Usuario usuario = controller.login("medico@hospital.com", "senhaErrada");
        Assert.assertNull("Login com senha errada deve retornar null", usuario);
    }

    @Test
    public void testLoginFalhaUsuarioNaoExiste() {
        Usuario usuario = controller.login("fantasma@hospital.com", "senha123");
        Assert.assertNull("Usuário inexistente deve retornar null", usuario);
    }
}