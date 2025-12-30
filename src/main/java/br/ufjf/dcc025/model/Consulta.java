package br.ufjf.dcc025.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Consulta implements RegistroClinico {
    private Medico medico;
    private Paciente paciente;
    private HorarioAtendimento horarioConsulta;
    private LocalDate dataConsulta;
    private StatusConsulta statusConsulta;
    private List<DocumentoMedico> documentoMedico = new ArrayList<>();

    public Consulta(Medico medico, Paciente paciente, HorarioAtendimento horarioConsulta, LocalDate dataConsulta, StatusConsulta statusConsulta) {
        this.medico = medico;
        this.paciente = paciente;
        this.horarioConsulta = horarioConsulta;
        this.dataConsulta = dataConsulta;
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
    public  LocalDateTime getDataHoraConsulta() {
        return LocalDateTime.of(dataConsulta,horarioConsulta.getInicio());
    }
    public StatusConsulta getStatusConsulta() {
        return statusConsulta;
    }

    @Override
    public LocalDateTime getDataRegistro() {
        return this.getDataHoraConsulta();
    }

    @Override
    public String getTipoRegistroClinico() {
        return "Consulta";
    }

    @Override
    public String getDescricao() {
        return "Dr(a). " + this.medico.getNome() +
                " - Status: " + this.statusConsulta.toString();
    }
}
