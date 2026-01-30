package br.ufjf.dcc025.exceptions;

public class HorarioInvalidoException extends Exception {
    public HorarioInvalidoException(String motivo) {
        super("Não foi possível agendar: " + motivo);
    }
}