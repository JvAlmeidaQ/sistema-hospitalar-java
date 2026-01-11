package br.ufjf.dcc025.model;

import br.ufjf.dcc025.model.util.ValidaDados;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class Paciente extends Usuario {
    private transient List<Consulta> minhasConsultas;
    private String telefone;
    private Endereco endereco;
    private Boolean podeReceberVisitas = false;
    private Boolean internado = false;
    private String convenio;


    public Paciente(String nome, String cpf, String email, String senha, String telefone, Endereco endereco, String convenio) {
        super(nome, email, senha, cpf);
        this.minhasConsultas = new ArrayList<>();
        this.setTelefone(telefone);
        this.endereco = endereco;
        this.convenio = convenio;
    }

    public void novaConsulta(Consulta consulta){
        this.minhasConsultas.add(consulta);
    }
    public void cancelaConsulta(Consulta consulta){
        if(minhasConsultas.contains(consulta))
        {
            if(consulta.getStatusConsulta() == StatusConsulta.AGENDADA)
                consulta.setStatusConsulta(StatusConsulta.CANCELADA);
            else
                throw new IllegalStateException("Não é possivel Cancelar uma Consulta Realizada ou já Cancelada");
        }
        else
            throw new IllegalArgumentException("Consulta não encontrada");
    }
    public void remarcarConsulta(Consulta consulta, HorarioAtendimento novoHorario){
        if(minhasConsultas.contains(consulta)) {
            if(consulta.getStatusConsulta() == StatusConsulta.AGENDADA) {
                consulta.setStatusConsulta(StatusConsulta.REMARCADA);
                consulta.setHorarioConsulta(novoHorario);
            }
            else
                throw new IllegalStateException("Apenas consultas agendadas podem ser remarcadas.");
        }
        else
            throw new IllegalArgumentException("Consulta não encontrada.");
    }

    public List<Consulta> getMinhasConsultas() {
        if(this.minhasConsultas == null)
            this.minhasConsultas = new ArrayList<>();
        return Collections.unmodifiableList(minhasConsultas);
    }

    public List<DocumentoMedico> meusDocumentos()
    {
        List<DocumentoMedico> documentos = new ArrayList<>();
        for(Consulta consulta : minhasConsultas){
            documentos.addAll(consulta.getDocumentoMedico());
        }
        return documentos;
    }


    public void setTelefone(String telefone) {
        if(!ValidaDados.validaTelefone(telefone))
            throw new IllegalArgumentException("Número de telefone Inválido");
        this.telefone = telefone;
    }
    public String getTelefone() {
        return telefone;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    public Endereco getEndereco() {
        return endereco;
    }

    public void setInternado(Boolean internado) { this.internado = internado; }
    public Boolean getInternado() { return internado; }

    public void setPodeReceberVisitas(Boolean podeReceberVisitas) {
        this.podeReceberVisitas = podeReceberVisitas;
    }
    public Boolean getPodeReceberVisitas() {
        return podeReceberVisitas;
    }

    public String getConvenio() {
        return convenio;
    }
    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
