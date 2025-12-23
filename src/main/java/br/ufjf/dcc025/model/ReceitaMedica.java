package br.ufjf.dcc025.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceitaMedica extends DocumentoMedico {

    private List <String> remedios = new ArrayList<>();

    public ReceitaMedica(Medico medico, Paciente paciente, String doenca, List <String> remedios,  LocalDateTime dataExpedicao) {
        super(medico, paciente, doenca, dataExpedicao);
        this.remedios = remedios;
    }
    @Override
    public String imprimeDocumento() {
        return "=== RECEITA MÉDICA ===\n" +
                "Data: " + this.dataExpedicao.format(DATA_FORMATADA) + "\n" +
                "Médico: " + this.medico.getNome() + "\n" +
                "Uso indicado para: " + this.doenca + "\n" +
                "Prescrição:\n" +
                " - " + String.join("\n - ", remedios) + "\n" +
                "======================";
    }
}
