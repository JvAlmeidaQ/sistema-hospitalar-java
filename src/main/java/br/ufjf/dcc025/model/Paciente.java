package br.ufjf.dcc025.model;

import br.ufjf.dcc025.util.ValidaDados;

import java.util.Collections;
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
    /* Sobre o acesso aos Documentos Medicos, o Paciente apenas deve, poder ve-los,
    as operações de adição de documentos não deve ser feito, dentro da classe paciente*/

    /*Agora, pensando na operação de Acesso a esses documentos, preciso dar opção de listar todos os documentos, ou listar por tipo(exame/atestado/Receita)
    * e é necessário a opção de listar apenas um único documento que ele deseje ver, logo entramos numa encruzilhada,
    *  para resolver penso em adicionar um identificador em cada documento ou trocar para um MAP onde a chave é a data de expedição ou de realização
    * logo seria facil iterar sobre a lista, sabendo que só preciso achar o identifacor unico do documento*/
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
        /*vamos ter um menu, com 4 opções onde cada uma mostra por tipo:
        * 1. Atestados, 2. Exames, 3. receitas, 4. todos os documentos
        * opção escolhida passada por parametro*/
        List<DocumentoMedico> documentos = new ArrayList<>();
        for(Consulta consulta : minhasConsultas){
            if(num == 1)
            {
                for(DocumentoMedico atestadoMedico : consulta.getDocumentoMedico())
                {
                    if(atestadoMedico instanceof AtestadoMedico)
                        documentos.add(atestadoMedico);
                }
                return  documentos;
            }
            if(num == 2)
            {
                for(DocumentoMedico exames : consulta.getDocumentoMedico())
                {
                    if(exames instanceof ExameMedico)
                        documentos.add(exames);
                }
                return  documentos;
            }
            if(num == 3)
            {
                for(DocumentoMedico receitas : consulta.getDocumentoMedico())
                {
                    if(receitas instanceof ReceitaMedica)
                        documentos.add(receitas);
                }
                return  documentos;
            }
        }
        return this.meusDocumentos();
    }








    /*Sobre essa Parte:
    *O cliente deve ter acesso integral ao seu histórico de saúde na instituição,
    * visualizando um registro cronológico de todas as consultas
    * e exames realizados anteriormente.
    * Como faria isso? Pois Consultas e Documentos são de tipos diferentes.
    * Pensei na seguinte maneira, como ambas tem datas, eu iria percorrer as duas listas simultaneamente procurando datas iguais ou imprimindo as datas diferentes em ordem cronológica;
    * e seria um metodo apenas de impressão, já que como eles são de tipos diferentes um retorno é dificl.
* */


































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
