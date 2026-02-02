

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

        if (DadosHospital.getInstance().cpfJaExiste(cpf)) {
            throw new CPFDuplicadoException();
        }

        if (isMedico) {
            if (especialidade.trim().isEmpty())
                throw new DadosInvalidosException("Especialidade é obrigatória para médicos.");

            Medico novoMedico = new Medico(nome, email, senha, cpf, especialidade);
            DadosHospital.getInstance().addMedico(novoMedico);

        } else {
            Secretaria novaSecretaria = new Secretaria(nome, email, senha, cpf);
            DadosHospital.getInstance().addSecretaria(novaSecretaria);
        }

        DadosHospital.getInstance().salvarDados();
    }
}