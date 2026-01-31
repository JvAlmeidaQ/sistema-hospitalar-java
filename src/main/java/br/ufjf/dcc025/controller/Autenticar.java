

package br.ufjf.dcc025.controller;
import br.ufjf.dcc025.model.*;

import javax.swing.*;

public class Autenticar {
    public Usuario login(String email, String senha) {
        for (Medico m : DadosHospital.getInstance().getMedicos()) {
            if (m.getEmail().equals(email) && m.getSenha().equals(senha)) {
                return m;
            }
        }
        for (Paciente p : DadosHospital.getInstance().getPacientes()) {
            if (p.getEmail().equals(email) && p.getSenha().equals(senha)) {
                return p;
            }
        }
        for (Secretaria s : DadosHospital.getInstance().getSecretarias()) {
            if (s.getEmail().equals(email) && s.getSenha().equals(senha)) {
                return s;
            }
        }
        return null;
    }

    public boolean validarSenha(Usuario usuario, String senhaDigitada) {
        if (usuario == null)
            return false;
        return usuario.getSenha().equals(senhaDigitada);
    }
}
