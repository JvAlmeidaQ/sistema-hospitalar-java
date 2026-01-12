//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025;

import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.*;
import br.ufjf.dcc025.view.MedicoView.TelaPrincipalMedico;
import br.ufjf.dcc025.view.SecretariaView.TelaPrincipalSecretaria;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainSistemaHospitalar
{
    public static void main( String[] args )
    {

      DadosHospital.carregarDados();

        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}
