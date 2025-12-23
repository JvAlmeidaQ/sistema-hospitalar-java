package br.ufjf.dcc025.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class  DocumentoMedico {
    protected Medico medico;
    protected Paciente paciente;
    protected String doenca;
    protected LocalDateTime dataExpedicao;

    protected static final DateTimeFormatter DATA_FORMATADA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DocumentoMedico(Medico medico, Paciente paciente, String doenca,  LocalDateTime dataExpedicao) {
        this.medico = medico;
        this.paciente = paciente;
        this.doenca = doenca;
        this.dataExpedicao = dataExpedicao;
    }

    public abstract String imprimeDocumento();

    public Medico getMedico() { return medico; }
    public Paciente getPaciente() { return paciente; }
    public LocalDateTime getDataExpedicao() { return dataExpedicao; }
}
