package br.ufjf.dcc025.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioAtendimento {

    private DiasDaSemana dia;
    private LocalTime inicio;
    private LocalTime fim;
    private int duracaoAtendimento;

    public HorarioAtendimento(DiasDaSemana dia, LocalTime inicio, LocalTime fim,  int duracaoAtendimento) {
        this.dia = dia;
        if(fim.isBefore(inicio) || fim.equals(inicio)) { throw  new IllegalArgumentException("Horario Invalido"); }
        this.inicio = inicio;
        this.fim = fim;
        this.duracaoAtendimento = duracaoAtendimento;
    }
    public DiasDaSemana getDia() {
        return dia;
    }
    public LocalTime getInicio() {
        return inicio;
    }
    public LocalTime getFim() {
        return fim;
    }
    public int getDuracaoAtendimento() {
        return duracaoAtendimento;
    }
}
