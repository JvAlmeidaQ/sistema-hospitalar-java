

package br.ufjf.dcc025.view.MedicoView;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TelaStatusPaciente extends JFrame {

    private Medico medico;
    private Paciente paciente;
    private MedicoController controller;

    private JCheckBox chkInternado;
    private JCheckBox chkAptoVisitas;
    private JCheckBox chkNaoAptoVisitas;
    private JPanel painelVisitas;

    public TelaStatusPaciente(Medico medico, Paciente paciente) {
        this.medico = medico;
        this.paciente = paciente;
        this.controller = new MedicoController();

        setTitle("Gerenciar Status - " + paciente.getNome());
        setSize(400, 250);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //painel
        JPanel painelStatus = new JPanel();
        painelStatus.setLayout(new BoxLayout(painelStatus, BoxLayout.Y_AXIS));
        painelStatus.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Status Clínico",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)
        ));

        // Checkbox
        chkInternado = new JCheckBox("Paciente Internado");
        chkInternado.setFont(new Font("Arial", Font.BOLD, 13));
        chkInternado.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Painel para as opções de visita
        painelVisitas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelVisitas.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelVisitas.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        chkAptoVisitas = new JCheckBox("Apto para Visitas");
        chkNaoAptoVisitas = new JCheckBox("Não Apto para Visitas");

        ButtonGroup grupoVisitas = new ButtonGroup();
        grupoVisitas.add(chkAptoVisitas);
        grupoVisitas.add(chkNaoAptoVisitas);

        painelVisitas.add(chkAptoVisitas);
        painelVisitas.add(chkNaoAptoVisitas);

        painelStatus.add(Box.createVerticalStrut(10));
        painelStatus.add(chkInternado);
        painelStatus.add(painelVisitas);

        JPanel containerCentral = new JPanel(new BorderLayout());
        containerCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        containerCentral.add(painelStatus, BorderLayout.CENTER);

        add(containerCentral, BorderLayout.CENTER);

        // botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnSalvar = new JButton("Salvar Status");

        btnSalvar.setBackground(new Color(0, 102, 204));
        btnSalvar.setForeground(Color.WHITE);

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        boolean estaInternado = paciente.getInternado() != null && paciente.getInternado();
        chkInternado.setSelected(estaInternado);
        painelVisitas.setVisible(estaInternado);

        if (estaInternado) {
            boolean podeReceber = paciente.getPodeReceberVisitas() != null && paciente.getPodeReceberVisitas();
            if (podeReceber) chkAptoVisitas.setSelected(true);
            else chkNaoAptoVisitas.setSelected(true);
        }

        chkInternado.addActionListener(e -> {
            boolean selecionado = chkInternado.isSelected();
            painelVisitas.setVisible(selecionado);
            if (!selecionado) {
                grupoVisitas.clearSelection();
            } else if (grupoVisitas.getSelection() == null) {
                chkNaoAptoVisitas.setSelected(true);
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        btnSalvar.addActionListener(e -> salvarAlteracoes());
    }

    private void salvarAlteracoes() {
        try {
            boolean internado = chkInternado.isSelected();
            boolean aptoVisita = false;

            if (internado) {
                aptoVisita = chkAptoVisitas.isSelected();
                if (!chkAptoVisitas.isSelected() && !chkNaoAptoVisitas.isSelected()) {
                    JOptionPane.showMessageDialog(this, "Se o paciente está internado, defina se pode receber visitas.");
                    return;
                }
            }

            controller.StatusPaciente(medico, paciente, internado, aptoVisita);

            JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar status: " + ex.getMessage());
        }
    }
}