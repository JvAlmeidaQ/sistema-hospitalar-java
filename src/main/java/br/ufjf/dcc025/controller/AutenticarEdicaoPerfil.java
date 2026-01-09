package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.*;

import javax.swing.*;

public class AutenticarEdicaoPerfil {

    public AutenticarEdicaoPerfil(){}
//PRECISA VERIFICAR A NOVA SENHA, NOME E EMAIL ANTES DE CHAMAR ESSA FUNÇÃO!!!
    public boolean autenticarEdicao(Usuario usuarioAtivo, String senha, String senhaNova, String novoNome, String novoEmail){
        if(usuarioAtivo.ValidacaoSetSenha(senha, senhaNova)){
            usuarioAtivo.setNome(novoNome);
            usuarioAtivo.setEmail(novoEmail);
            return true;
        }
        return false;
    }
}