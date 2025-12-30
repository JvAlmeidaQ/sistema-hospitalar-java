package br.ufjf.dcc025.model;

import br.ufjf.dcc025.util.ValidaDados;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class Paciente extends Usuario {
    private List<Consulta> minhasConsultas;
    private String telefone;
    private Endereco endereco;
    private Boolean podeReceberVisitas = false;


    public Paciente(String nome, String cpf, String email, String senha, String telefone, Endereco endereco) {
        super(nome, cpf, email, senha);
        this.minhasConsultas = new ArrayList<>();
        this.setTelefone(telefone);
        this.endereco = endereco;
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
    public List<DocumentoMedico> documentosPorTipo(int num)
    {
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
            return this.meusDocumentos();
        return documentos;
    }

    public DocumentoMedico documentoUnicoId(int id)
    {
        for(Consulta consulta : minhasConsultas){
            for(DocumentoMedico doc : consulta.getDocumentoMedico())
                if(doc.getId() == id)
                    return doc;
        }
        return null;
    }

    public List<String> HistoricoMedico(){

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

    public void setTelefone(String telefone) {
        if(!ValidaDados.validaTelefone(telefone))
            throw new IllegalArgumentException("Numero Invalido");
        this.telefone = "+55 ".concat(telefone);
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

    public void setPodeReceberVisitas(Boolean podeReceberVisitas) {
        this.podeReceberVisitas = podeReceberVisitas;
    }
    public Boolean getPodeReceberVisitas() {
        return podeReceberVisitas;
    }
}
