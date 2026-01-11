package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TelaAtendimentoMedico extends JFrame {

    private Medico medico;
    private Paciente paciente;
    private Consulta consulta;

    // Controller para realizar as ações finais
    private MedicoController controller;

    // Componentes de Interface
    private JCheckBox chkInternado;
    private JCheckBox chkAptoVisitas;
    private JCheckBox chkNaoAptoVisitas;
    private JPanel painelVisitas;
    private ButtonGroup grupoVisitas;

    public TelaAtendimentoMedico(Consulta consulta) {
        this.consulta = consulta;
        this.medico = consulta.getMedico();
        this.paciente = consulta.getPaciente();
        this.controller = new MedicoController(); // Instancia a controller

        setTitle("Atendimento Médico - Dr(a). " + medico.getNome());
        setSize(850, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- CABEÇALHO ---
        JPanel painelCabecalho = new JPanel(new GridLayout(2, 1));
        painelCabecalho.setBackground(new Color(240, 248, 255));
        painelCabecalho.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblMedico = new JLabel("Médico Responsável: " + medico.getNome());
        lblMedico.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel lblPaciente = new JLabel("Paciente em Atendimento: " + paciente.getNome());
        lblPaciente.setFont(new Font("Arial", Font.PLAIN, 16));

        painelCabecalho.add(lblMedico);
        painelCabecalho.add(lblPaciente);
        add(painelCabecalho, BorderLayout.NORTH);

        // --- STATUS DO PACIENTE ---
        JPanel painelStatus = new JPanel();
        painelStatus.setLayout(new BoxLayout(painelStatus, BoxLayout.Y_AXIS));
        painelStatus.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Status Clínico (Pós-Consulta)",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        // Checkbox Principal
        chkInternado = new JCheckBox("Paciente precisa ser Internado?");
        chkInternado.setFont(new Font("Arial", Font.BOLD, 14));
        chkInternado.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Painel Visitas
        painelVisitas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelVisitas.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        painelVisitas.setAlignmentX(Component.LEFT_ALIGNMENT);

        chkAptoVisitas = new JCheckBox("Apto para Visitas");
        chkNaoAptoVisitas = new JCheckBox("Isolamento (Não Apto)");
        chkAptoVisitas.setFont(new Font("Arial", Font.PLAIN, 13));
        chkNaoAptoVisitas.setFont(new Font("Arial", Font.PLAIN, 13));

        grupoVisitas = new ButtonGroup();
        grupoVisitas.add(chkAptoVisitas);
        grupoVisitas.add(chkNaoAptoVisitas);

        painelVisitas.add(chkAptoVisitas);
        painelVisitas.add(chkNaoAptoVisitas);

        painelVisitas.setVisible(false);

        painelStatus.add(Box.createVerticalStrut(10));
        painelStatus.add(chkInternado);
        painelStatus.add(painelVisitas);
        painelStatus.add(Box.createVerticalStrut(10));

        // --- BOTÕES DE DOCUMENTOS ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 40));
        painelAcoes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAtestado = criarBotaoAcao("Emitir Atestado");
        JButton btnExame = criarBotaoAcao("Solicitar Exame");
        JButton btnReceita = criarBotaoAcao("Prescrever Receita");

        painelAcoes.add(btnAtestado);
        painelAcoes.add(btnExame);
        painelAcoes.add(btnReceita);

        // Organização Central
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        painelCentral.add(painelStatus, BorderLayout.NORTH);
        painelCentral.add(painelAcoes, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // --- RODAPÉ (Finalizar) ---
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnFinalizar = new JButton("Concluir Atendimento");
        btnFinalizar.setBackground(new Color(0, 102, 51));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinalizar.setPreferredSize(new Dimension(200, 45));

        painelRodape.add(btnFinalizar);
        add(painelRodape, BorderLayout.SOUTH);

        // --- EVENTOS ---
        chkInternado.addActionListener(e -> {
            boolean estaInternado = chkInternado.isSelected();
            painelVisitas.setVisible(estaInternado);
            if (!estaInternado) {
                grupoVisitas.clearSelection();
            } else {
                chkNaoAptoVisitas.setSelected(true); // Padrão seguro
            }
            revalidate();
            repaint();
        });

        // Chamada das Telas de Documentos (Passando a consulta!)
        btnAtestado.addActionListener(e -> abrirTelaAtestado());
        btnExame.addActionListener(e -> abrirTelaExame());
        btnReceita.addActionListener(e -> abrirTelaReceita());

        // Chamada da Finalização
        btnFinalizar.addActionListener(e -> finalizarAtendimento());
    }

    private JButton criarBotaoAcao(String texto) {
        JButton btn = new JButton("<html><center>" + texto.replace(" ", "<br>") + "</center></html>");
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 80));
        return btn;
    }

    private void finalizarAtendimento() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente encerrar este atendimento?\nO status da consulta será alterado para REALIZADA.",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // 1. Atualiza Status do Paciente (Internação)
            if (chkInternado.isSelected()) {
                boolean apto = chkAptoVisitas.isSelected();
                controller.StatusPaciente(medico, paciente, true, apto);

                String msgStatus = apto ? "Apto para visitas" : "Isolamento";
                JOptionPane.showMessageDialog(this, "Status do paciente atualizado: INTERNADO (" + msgStatus + ")");
            } else {
                // Se não está internado, garante que status de visita é false/false
                controller.StatusPaciente(medico, paciente, false, false);
            }

            // 2. Atualiza Status da Consulta (MUITO IMPORTANTE)
            controller.finalizarConsulta(consulta);

            // 3. Fecha a tela
            JOptionPane.showMessageDialog(this, "Atendimento finalizado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar: " + ex.getMessage());
        }
    }


    private void abrirTelaAtestado() {
        new TelaEnvioAtestado(consulta).setVisible(true);
        JOptionPane.showMessageDialog(this, "Abrindo Tela Atestado..."); // Placeholder se vc não tiver a classe aqui
    }

    private void abrirTelaExame() {
        new TelaEnvioExame(consulta).setVisible(true);
        JOptionPane.showMessageDialog(this, "Abrindo Tela Exame...");
    }

    private void abrirTelaReceita() {
        new TelaEnvioReceita(consulta).setVisible(true);
        JOptionPane.showMessageDialog(this, "Abrindo Tela Receita...");
    }
}