
package br.ufjf.dcc025;

import br.ufjf.dcc025.model.*;
import br.ufjf.dcc025.view.*;


import javax.swing.*;


public class MainSistemaHospitalar
{
    public static void main( String[] args ) {

        DadosHospital.getInstance().carregarDados();

        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}
