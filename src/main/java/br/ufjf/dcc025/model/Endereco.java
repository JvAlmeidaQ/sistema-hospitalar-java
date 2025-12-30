package br.ufjf.dcc025.model;

import br.ufjf.dcc025.util.ValidaDados;

public class Endereco {
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;

    public Endereco(String cep, String rua, String numero, String complemento, String bairro, String cidade, String estado) {
        setCep(cep);
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }
    public void setCep(String cep) {
        if(!ValidaDados.validaCEP(cep))
            throw new IllegalArgumentException("CEP invalido");
        this.cep = cep;
    }
    public String getCep() {
        return cep;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }
    public String getRua() {
        return rua;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getNumero() {
        return numero;
    }
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
    public String getComplemento() {
        return complemento;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public String getBairro() {
        return bairro;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getCidade() {
        return cidade;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(rua)
                .append(", ")
                .append(numero);

        if (complemento != null && !complemento.isBlank()) {
            sb.append(" - ").append(complemento);
        }

        sb.append("\n")
                .append(bairro)
                .append(" - ")
                .append(cidade)
                .append("/")
                .append(estado)
                .append("\nCEP: ")
                .append(cep);

        return sb.toString();
    }
}
