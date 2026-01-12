//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.model;

public class Secretaria extends Usuario {

    public Secretaria(String nome, String email, String senha, String cpf) {
        super(nome, email, senha, cpf);
    }

    public void cadastrarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente Inexistente");
        }
        DadosHospital.pacientes.add(paciente);
    }

    public boolean autorizarVisitas(Paciente paciente) {
        if (DadosHospital.pacientes.contains(paciente)){
            if (paciente.getPodeReceberVisitas())
                return true;
            else
                return false;
        }
        throw new IllegalArgumentException("Paciente Inexistente");
    }

    public void cadastrarMedicos(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("Medico Inexistente");
        }
        DadosHospital.medicos.add(medico);
    }
}
