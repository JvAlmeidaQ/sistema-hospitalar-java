//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Secretaria;

public class FuncionarioController {
    public void cadastrarFuncionario(String nome, String cpf, String email, String senha,
                                     boolean isMedico, String especialidade) throws Exception {

        if (nome.trim().isEmpty() || cpf.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
            throw new Exception("Preencha todos os campos obrigatórios.");
        }

        if (cpfJaExiste(cpf)) {
            throw new Exception("Já existe um funcionário cadastrado com este CPF.");
        }

        // 3. Criação e Persistência
        if (isMedico) {
            if (especialidade.trim().isEmpty()) throw new Exception("Especialidade é obrigatória para médicos.");
            Medico novoMedico = new Medico(nome, email, senha, cpf, especialidade);
            DadosHospital.medicos.add(novoMedico);

        } else {
            // Cria Secretária
            Secretaria novaSecretaria = new Secretaria(nome, email, senha, cpf);
            DadosHospital.secretarias.add(novaSecretaria);
        }

        // 4. Salvar no Disco
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