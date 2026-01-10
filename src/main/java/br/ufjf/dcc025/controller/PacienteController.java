package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Endereco;
import br.ufjf.dcc025.model.Paciente;

public class PacienteController {

    public void atualizarPaciente(Paciente paciente, String senhaAtual,
      String novaSenha, String nome, String email, String telefone) throws Exception {
        Autenticar autenticacao = new Autenticar();

        if(paciente == null)
            throw new Exception("Paciente inexistente");

        if(autenticacao.validarSenha(paciente, senhaAtual))
            throw new Exception("Senha Digitada Incorreta!");

        if (nome == null || nome.length() < 2)
            throw new Exception("Nome invalido");

        if(telefone == null || telefone.length() < 9)
            throw new Exception("Telefone invalido");

        if(!email.contains("@"))
            throw new Exception("Email invalido");

        paciente.setNome(nome);
        paciente.setEmail(email);
        paciente.setTelefone(telefone);

        if(!paciente.ValidacaoSetSenha(novaSenha))
            throw new Exception("Nova Senha Invalida!");


        DadosHospital.salvarDados();
    }

    public void atualizarEndereco(Paciente paciente, String cep, String rua, String numero, String complemento,
                                  String estado, String bairro, String cidade) throws Exception
    {

        if (cep.trim().isEmpty() || rua.trim().isEmpty() || numero.trim().isEmpty())
            throw new Exception("CEP, Rua e Numero são obrigatórios.");
        Endereco novoEndereco = new Endereco(cep, rua, numero, complemento, estado, bairro, cidade);
        paciente.setEndereco(novoEndereco);

        DadosHospital.salvarDados();
    }

    public void cadastrarPaciente(String nome, String cpf, String email, String senha, String telefone, String convenio,
                                  String cep, String rua, String numero, String complemento, String bairro, String cidade, String estado) throws Exception {

        if (nome.trim().isEmpty() || cpf.trim().isEmpty() || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Campos obrigatórios não preenchidos.");
        }

        for (Paciente p : DadosHospital.pacientes) {
            if (p.getCpf().equals(cpf))
                throw new IllegalArgumentException("Paciente já cadastrado com este CPF.");
        }

        Endereco novoEndereco = new Endereco(cep, estado, cidade, bairro, rua, numero, complemento);

        Paciente novoPaciente = new Paciente(nome, cpf, email, senha, telefone, novoEndereco, convenio);

        DadosHospital.pacientes.add(novoPaciente);
        DadosHospital.salvarDados();
    }
}
