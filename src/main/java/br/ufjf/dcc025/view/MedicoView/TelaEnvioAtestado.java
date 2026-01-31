

package br.ufjf.dcc025.view.MedicoView;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaEnvioAtestado extends JFrame {

    private Consulta consulta;
    private Medico medico;
    private Paciente paciente;
    private JSpinner spnDias;
    private JTextArea txtDiagnostico; // Mudamos para JTextArea (Caixa grande)
    private JLabel lblData;

    public TelaEnvioAtestado(Consulta consulta) {
        this.consulta = consulta;
        this.medico = consulta.getMedico();
        this.paciente = consulta.getPaciente();

        setTitle("Emissão de Atestado");
        setSize(500, 450); // Dimensão solicitada
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // cabeçalho
        JPanel painelTopo = new JPanel(new GridLayout(3, 1, 5, 5));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        painelTopo.setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("Novo Atestado Médico");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));

        JLabel lblPaciente = new JLabel("Paciente: " + paciente.getNome());
        lblPaciente.setFont(new Font("Arial", Font.PLAIN, 14));

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        lblData = new JLabel("Data de Emissão: " + agora.format(formatter));
        lblData.setFont(new Font("Arial", Font.ITALIC, 12));

        painelTopo.add(lblTitulo);
        painelTopo.add(lblPaciente);
        painelTopo.add(lblData);
        add(painelTopo, BorderLayout.NORTH);

        // formulario
        JPanel painelForm = new JPanel(new BorderLayout(10, 10)); // Layout para dividir Dias e Diagnóstico
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Dias de Afastamento
        JPanel painelDias = new JPanel(new BorderLayout(5, 5));
        painelDias.add(criarLabel("Dias de Afastamento:"), BorderLayout.NORTH);

        spnDias = new JSpinner(new SpinnerNumberModel(1, 0, 90, 1));
        ((JSpinner.DefaultEditor) spnDias.getEditor()).getTextField().setFont(new Font("Arial", Font.BOLD, 14));
        painelDias.add(spnDias, BorderLayout.CENTER);

        // Diagnóstico
        JPanel painelDiagnostico = new JPanel(new BorderLayout(5, 5));
        painelDiagnostico.add(criarLabel("Diagnóstico / Motivo (CID opcional):"), BorderLayout.NORTH);

        txtDiagnostico = new JTextArea();
        txtDiagnostico.setLineWrap(true);       // Quebra linha automaticamente
        txtDiagnostico.setWrapStyleWord(true);  // Quebra palavras inteiras
        JScrollPane scrollDiagnostico = new JScrollPane(txtDiagnostico); // Barra de rolagem
        painelDiagnostico.add(scrollDiagnostico, BorderLayout.CENTER);

        painelForm.add(painelDias, BorderLayout.NORTH);      // Fica no topo
        painelForm.add(painelDiagnostico, BorderLayout.CENTER); // Ocupa o resto

        add(painelForm, BorderLayout.CENTER);

        // botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnEmitir = new JButton("Emitir Atestado");

        btnEmitir.setBackground(new Color(0, 102, 204));
        btnEmitir.setForeground(Color.WHITE);
        btnEmitir.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnEmitir);
        add(painelBotoes, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());

        btnEmitir.addActionListener(e -> emitirAtestado());
    }

    private void emitirAtestado() {
        MedicoController medicoController = new MedicoController();

        int dias = (Integer) spnDias.getValue();
        String diagnostico = txtDiagnostico.getText();

        if (diagnostico.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe o diagnóstico ou motivo do afastamento.",
                    "Campo Obrigatório",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            medicoController.geraAtestado(consulta, dias, diagnostico);
            JOptionPane.showMessageDialog(this, "Atestado emitido com sucesso!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao emitir atestado: " + e.getMessage());
        }
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
}