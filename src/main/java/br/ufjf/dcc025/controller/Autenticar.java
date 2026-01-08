package br.ufjf.dcc025.controller;

import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.TelaLogin;

import javax.swing.*;

public class Autenticar {
    private TelaLogin telaLogin;

    public Autenticar(TelaLogin telaLogin){
        this.telaLogin = telaLogin;
    }

    public void autenticarUser(String email, String senha){
        for (Medico m : DadosHospital.medicos){
            if(m.getEmail().equals(email) && m.getSenha().equals(senha)){
                JOptionPane.showMessageDialog(telaLogin, "Bem vindo Dr. " + m.getNome());
                //abrir tela médico e fechar login
                return;
            }
        }
        for(Paciente p : DadosHospital.pacientes){
            if(p.getEmail().equals(email) && p.getSenha().equals(senha)){
                JOptionPane.showMessageDialog(telaLogin, "Bem vindo " + p.getNome());
                //abrir tela paciente e fechar login
                return;
            }
        }
        for(Secretaria s : DadosHospital.secretarias){
            if(s.getEmail().equals(email) && s.getSenha().equals(senha)){
                JOptionPane.showMessageDialog(telaLogin, "Bem vindo " + s.getNome());
                //abrir tela secretaria e fechar login
                return;
            }
        }
        JOptionPane.showMessageDialog(telaLogin, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }
}
