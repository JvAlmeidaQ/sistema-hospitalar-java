package br.ufjf.dcc025.model;

import java.util.ArrayList;
import java.util.List;

public class Consulta {
    private Medico medico;
    private Paciente paciente;
    private HorarioAtendimento horarioConsulta;
    private StatusConsulta statusConsulta;
    private List<DocumentoMedico> documentoMedico = new ArrayList<>();

    public Consulta(Medico medico, Paciente paciente, HorarioAtendimento horarioConsulta, StatusConsulta statusConsulta) {
        this.medico = medico;
        this.paciente = paciente;
        this.horarioConsulta = horarioConsulta;
        this.statusConsulta = StatusConsulta.AGENDADA;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    public void setHorarioConsulta(HorarioAtendimento horarioConsulta) {
        this.horarioConsulta = horarioConsulta;
    }
    public void setStatusConsulta(StatusConsulta statusConsulta) {
        this.statusConsulta = statusConsulta;
    }
    public void adicionaDocumentoMedico(DocumentoMedico documentoMedico) {
        this.documentoMedico.add(documentoMedico);
    }
    public List<DocumentoMedico> getDocumentoMedico() {
        return documentoMedico;
    }
    public Medico getMedico() {
        return medico;
    }
    public Paciente getPaciente() {
        return paciente;
    }
    public HorarioAtendimento getHorarioConsulta() {
        return horarioConsulta;
    }
    public StatusConsulta getStatusConsulta() {
        return statusConsulta;
    }

}
