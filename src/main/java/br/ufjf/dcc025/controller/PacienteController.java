//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.exceptions.CPFDuplicadoException;
import br.ufjf.dcc025.exceptions.ConsultaInvalidaException;
import br.ufjf.dcc025.exceptions.DadosInvalidosException;
import br.ufjf.dcc025.model.*;

import java.util.*;

public class PacienteController {

    public List<Paciente> listarTodosPacientes() {
        return DadosHospital.pacientes;
    }

    public List<Paciente> listarPacientesInternados() {
        List<Paciente> listaInternados = new ArrayList<>();
        for (Paciente p : DadosHospital.pacientes) {
            if (p.getInternado() != null && p.getInternado()) {
                listaInternados.add(p);
            }
        }
        listaInternados.sort((p1, p2) -> p1.getNome().compareToIgnoreCase(p2.getNome()));

        return listaInternados;
    }

    public List<Paciente> buscarInternadosPorNome(String termoBusca) {
        List<Paciente> todosInternados = listarPacientesInternados();
        List<Paciente> filtrados = new ArrayList<>();
        for (Paciente p : todosInternados) {
            if (p.getNome().toLowerCase().contains(termoBusca.toLowerCase())) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    public void atualizarPaciente(Paciente paciente, String senhaAtual, String novaSenha, String nome, String email, String telefone)
    throws DadosInvalidosException
    {
        Autenticar autenticacao = new Autenticar();

        if(paciente == null)
            throw new DadosInvalidosException("Paciente inexistente");

        if (nome == null || nome.length() < 2)
            throw new DadosInvalidosException("Nome invalido");

        if(telefone == null || telefone.length() < 9)
            throw new DadosInvalidosException("Telefone invalido");

        if(!email.contains("@"))
            throw new DadosInvalidosException("Email invalido");

        paciente.setNome(nome);
        paciente.setEmail(email);
        paciente.setTelefone(telefone);

       if(novaSenha != null && !novaSenha.isBlank())
       {
           if(!autenticacao.validarSenha(paciente, senhaAtual)) {
               throw new DadosInvalidosException("Senha Digitada Incorreta!");
           }
           paciente.ValidacaoSetSenha(novaSenha);
       }


        DadosHospital.salvarDados();
    }

    public void atualizarEndereco(Paciente paciente, String cep, String rua, String numero, String complemento,
                                  String estado, String bairro, String cidade) throws DadosInvalidosException
    {

        if (cep.trim().isEmpty() || rua.trim().isEmpty() || numero.trim().isEmpty())
            throw new DadosInvalidosException("CEP, Rua e Numero são obrigatórios.");
        Endereco novoEndereco = new Endereco(cep, rua, numero, complemento, estado, bairro, cidade);
        paciente.setEndereco(novoEndereco);

        DadosHospital.salvarDados();
    }

    public void cadastrarPaciente(String nome, String cpf, String email, String senha, String telefone, String convenio,
                                  String cep, String rua, String numero, String complemento, String bairro, String cidade, String estado) throws Exception {

        if (nome.trim().isEmpty() || cpf.trim().isEmpty() || senha.trim().isEmpty()) {
            throw new DadosInvalidosException("Campos obrigatórios não preenchidos.");
        }

        for (Paciente p : DadosHospital.pacientes) {
            if (p.getCpf().equals(cpf))
                throw new CPFDuplicadoException();
        }

        Endereco novoEndereco = new Endereco(cep, estado, cidade, bairro, rua, numero, complemento);

        Paciente novoPaciente = new Paciente(nome, cpf, email, senha, telefone, novoEndereco, convenio);

        DadosHospital.pacientes.add(novoPaciente);
        DadosHospital.salvarDados();
    }

    public java.util.List<Consulta> buscarConsultasPorMedico(Paciente paciente, Medico medico) {
        java.util.List<Consulta> consultasFiltradas = new java.util.ArrayList<>();
        for (Consulta c : paciente.getMinhasConsultas()) {
            if (c.getMedico().getCpf().equals(medico.getCpf())) {
                consultasFiltradas.add(c);
            }
        }
        return consultasFiltradas;
    }

    public List<Medico> listarMedicosDoPaciente(Paciente paciente) {
        Set<Medico> medicosUnicos = new HashSet<>();
        if (paciente.getMinhasConsultas() != null) {
            for (Consulta consulta : paciente.getMinhasConsultas()) {
                if (consulta.getMedico() != null) {
                    medicosUnicos.add(consulta.getMedico());
                }
            }
        }
        List<Medico> listaMedicos = new ArrayList<>(medicosUnicos);
        listaMedicos.sort((m1, m2) -> m1.getNome().compareToIgnoreCase(m2.getNome()));

        return listaMedicos;
    }

    public List<DocumentoMedico> documentosPorTipo(Paciente paciente, int num)
    {
        List<Consulta> minhasConsultas = paciente.getMinhasConsultas();
        List<DocumentoMedico> documentos = new ArrayList<>();
        for(Consulta consulta : minhasConsultas){
            if(num == 1)
            {
                for(DocumentoMedico atestadoMedico : consulta.getDocumentoMedico())
                {
                    if(atestadoMedico instanceof AtestadoMedico)
                        documentos.add(atestadoMedico);
                }
            }
            if(num == 2)
            {
                for(DocumentoMedico exames : consulta.getDocumentoMedico())
                {
                    if(exames instanceof ExameMedico)
                        documentos.add(exames);
                }
            }
            if(num == 3)
            {
                for(DocumentoMedico receitas : consulta.getDocumentoMedico())
                {
                    if(receitas instanceof ReceitaMedica)
                        documentos.add(receitas);
                }
            }
        }
        if(num == 4)
            return paciente.meusDocumentos();
        return documentos;
    }

    public DocumentoMedico documentoUnicoId(Paciente paciente, int id) {
        List<Consulta> minhasConsultas = paciente.getMinhasConsultas();
        for(Consulta consulta : minhasConsultas){
            for(DocumentoMedico doc : consulta.getDocumentoMedico())
                if(doc.getId() == id)
                    return doc;
        }
        return null;
    }

    public List<String> HistoricoMedico(Paciente paciente){
        List<Consulta> minhasConsultas = paciente.getMinhasConsultas();
        List<RegistroClinico> historicoClinico = new ArrayList<>();
        for(Consulta consulta : minhasConsultas){
            historicoClinico.add(consulta);
            historicoClinico.addAll(consulta.getDocumentoMedico());
        }

        historicoClinico.sort(Comparator.comparing(RegistroClinico::getDataRegistro));

        List<String> historico = new ArrayList<>();

        for(RegistroClinico registroClinico : historicoClinico){
            String linhaHistorico = registroClinico.getDataRegistro().format(DocumentoMedico.DATA_FORMATADA) +
                    " | " + registroClinico.getTipoRegistroClinico() + " | " +
                    registroClinico.getDescricao();
            historico.add(linhaHistorico);
        }
        return historico;
    }

    public List<Consulta> listarConsultasPendentes(Paciente paciente) {
        List<Consulta> pendentes = new ArrayList<>();

        if (paciente.getMinhasConsultas() != null) {
            for (Consulta c : paciente.getMinhasConsultas()) {
                StatusConsulta st = c.getStatusConsulta();
                if (st == StatusConsulta.AGENDADA || st == StatusConsulta.REMARCADA) {
                    pendentes.add(c);
                }
            }
        }
        pendentes.sort(Comparator.comparing(Consulta::getDataHoraConsulta));
        return pendentes;
    }

    public void cancelarConsulta(Consulta consulta) throws ConsultaInvalidaException {
        if (consulta == null)
            throw new ConsultaInvalidaException("Selecione uma consulta.");
        consulta.getPaciente().cancelaConsulta(consulta);
        DadosHospital.salvarDados(); // Se houver persistência
    }
}
