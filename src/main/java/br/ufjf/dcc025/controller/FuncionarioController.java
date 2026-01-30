//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.exceptions.CPFDuplicadoException;
import br.ufjf.dcc025.exceptions.DadosInvalidosException;
import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Secretaria;

public class FuncionarioController {
    public void cadastrarFuncionario(String nome, String cpf, String email, String senha,
                                     boolean isMedico, String especialidade) throws Exception {

        if (nome.trim().isEmpty() || cpf.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
            throw new DadosInvalidosException("Preencha todos os campos obrigatórios.");
        }

        if (cpfJaExiste(cpf)) {
            throw new CPFDuplicadoException();
        }

        if (isMedico) {
            if (especialidade.trim().isEmpty())
                throw new DadosInvalidosException("Especialidade é obrigatória para médicos.");

            Medico novoMedico = new Medico(nome, email, senha, cpf, especialidade);
            DadosHospital.medicos.add(novoMedico);

        } else {
            Secretaria novaSecretaria = new Secretaria(nome, email, senha, cpf);
            DadosHospital.secretarias.add(novaSecretaria);
        }

        DadosHospital.salvarDados();
    }

    private boolean cpfJaExiste(String cpf) {
        for (Medico m : DadosHospital.medicos) {
            if (m.getCpf().equals(cpf)) return true;
        }
        for (Secretaria s : DadosHospital.secretarias) {
            if(s.getCpf().equals(cpf)) return true;
        }
        return false;
    }
}