package br.ufjf.dcc025.model;

import java.time.LocalDateTime;

public class ExameMedico extends DocumentoMedico{
    private String tipoDeExame;
    private String resultado;

    public ExameMedico(Medico medico, Paciente paciente, String tipoDeExame, String resultado, String doenca, LocalDateTime dataExpedicao) {
        super(medico, paciente, doenca, dataExpedicao);
        this.tipoDeExame = tipoDeExame;
        this.resultado = resultado;
    }

    public String getTipoDeExame() {
        return tipoDeExame;
    }

    public String getResultado() {
        return resultado;
    }

    @Override
    public String imprimeDocumento() {
        return "=== EXAME MÉDICO ===\n" +
                "Data: " + this.dataExpedicao.format(DATA_FORMATADA) + "\n" +
                "Médico: " + this.medico.getNome() + "\n" +
                "Paciente: " + this.paciente.getNome() + "\n" +
                "Exame realizado: " + this.tipoDeExame + "\n" +
                "Indicação clínica: " + this.doenca + "\n" +
                "Resultado:\n" +
                this.resultado + "\n" +
                "====================";
    }
}
