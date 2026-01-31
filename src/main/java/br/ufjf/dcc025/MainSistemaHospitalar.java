

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

      DadosHospital.getInstance().carregarDados();

        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}
