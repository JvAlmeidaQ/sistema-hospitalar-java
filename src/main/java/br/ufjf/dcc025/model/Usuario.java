package br.ufjf.dcc025.model;

import br.ufjf.dcc025.util.ValidaDados;

public abstract class Usuario {
    private String nome;
    private String email;
    private String senha;
    private final String cpf;

    public Usuario(String nome, String email, String senha, String cpf) {
        if (!ValidaDados.ValidaCPF(cpf)) {
            throw new IllegalArgumentException("CPF Invalido: " + cpf);
        }
        this.cpf = cpf;
        this.setNome(nome);
        this.setEmail(email);
        this.setSenha(senha);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.length() < 2)
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (ValidaDados.validaEmail(email))
            throw new IllegalArgumentException("Email inválido: " + email);
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    protected void setSenha(String senha) {
        if (!ValidaDados.validaSenha(senha))
            throw new IllegalArgumentException("Senha inválida");
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }
}