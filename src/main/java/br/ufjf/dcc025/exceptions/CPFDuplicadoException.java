package br.ufjf.dcc025.exceptions;

public class CPFDuplicadoException extends Exception {
    public CPFDuplicadoException() {
        super("Este CPF já está cadastrado no sistema.");
    }
}
