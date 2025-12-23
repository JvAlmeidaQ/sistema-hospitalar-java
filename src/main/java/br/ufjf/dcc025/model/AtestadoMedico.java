package br.ufjf.dcc025.model;

import java.time.LocalDateTime;

public class AtestadoMedico extends DocumentoMedico {
    private int diasAfastamento;

    public AtestadoMedico(Medico medico, Paciente paciente, String doenca, int diasAfastamento,  LocalDateTime dataExpedicao) {
        super(medico, paciente, doenca, dataExpedicao);
        this.diasAfastamento = diasAfastamento;
    }
    @Override
    public String imprimeDocumento() {
        return "=== ATESTADO MÉDICO ===\n" +
                "Data: " + this.dataExpedicao.format(DATA_FORMATADA) + "\n" +
                "Médico: " + this.medico.getNome() + " (CPF: " + this.medico.getCpf() + ")\n" +
                "Paciente: " + this.paciente.getNome() + "\n" +
                "Diagnóstico: " + this.doenca + "\n" +
                "Declaro para os devidos fins que o paciente necessita de " +
                this.diasAfastamento + " dia(s) de repouso.\n" +
                "=======================";
    }
}
