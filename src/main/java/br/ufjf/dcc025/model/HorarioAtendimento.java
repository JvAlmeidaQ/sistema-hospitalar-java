//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.model;

import br.ufjf.dcc025.exceptions.HorarioInvalidoException;

import java.time.LocalTime;

public class HorarioAtendimento {

    private DiasDaSemana dia;
    private LocalTime inicio;
    private LocalTime fim;
    private int duracaoAtendimento;

    public HorarioAtendimento(DiasDaSemana dia, LocalTime inicio, LocalTime fim,  int duracaoAtendimento)
    throws HorarioInvalidoException
    {
        this.dia = dia;
        if(fim.isBefore(inicio) || fim.equals(inicio)) { throw  new HorarioInvalidoException("Horário Invalido"); }
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
    public void setDuracaoAtendimento(int duracaoAtendimento) {
        this.duracaoAtendimento = duracaoAtendimento;
    }
}
