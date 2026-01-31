

package br.ufjf.dcc025.model;

import br.ufjf.dcc025.exceptions.DadosInvalidosException;

public class Secretaria extends Usuario {

    public Secretaria(String nome, String email, String senha, String cpf)
    throws DadosInvalidosException
    {
        super(nome, email, senha, cpf);
    }
}
