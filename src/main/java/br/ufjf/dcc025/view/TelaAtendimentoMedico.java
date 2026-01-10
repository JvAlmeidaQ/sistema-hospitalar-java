package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TelaAtendimentoMedico extends JFrame {

    private Medico medico;
    private Paciente paciente;

    // Componentes de Interface
    private JCheckBox chkInternado;
    private JCheckBox chkAptoVisitas;
    private JCheckBox chkNaoAptoVisitas;
    private JPanel painelVisitas;

    public TelaAtendimentoMedico(Medico medico, Paciente paciente) {
        this.medico = medico;
        this.paciente = paciente;

        setTitle("Atendimento Médico - Dr(a). " + medico.getNome());

        // --- 1. ALTERAÇÃO: Tamanho fixo e maior ---
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
        lblMedico.setFont(new Font("Arial", Font.BOLD, 16)); // Fonte um pouco maior

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
                "Status Clínico",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        // Checkbox Principal
        chkInternado = new JCheckBox("Paciente Internado");
        chkInternado.setFont(new Font("Arial", Font.BOLD, 14));

        // --- 2. ALTERAÇÃO: Fixar alinhamento para não "andar" ---
        chkInternado.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Painel secundário para as opções de visita
        painelVisitas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelVisitas.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Indentação leve apenas nas filhas
        painelVisitas.setAlignmentX(Component.LEFT_ALIGNMENT); // Garante que o painel também fique a esquerda

        chkAptoVisitas = new JCheckBox("Apto para Visitas");
        chkNaoAptoVisitas = new JCheckBox("Não Apto para Visitas");
        chkAptoVisitas.setFont(new Font("Arial", Font.PLAIN, 13));
        chkNaoAptoVisitas.setFont(new Font("Arial", Font.PLAIN, 13));

        ButtonGroup grupoVisitas = new ButtonGroup();
        grupoVisitas.add(chkAptoVisitas);
        grupoVisitas.add(chkNaoAptoVisitas);

        painelVisitas.add(chkAptoVisitas);
        painelVisitas.add(chkNaoAptoVisitas);

        painelVisitas.setVisible(false);

        painelStatus.add(Box.createVerticalStrut(10)); // Espacinho
        painelStatus.add(chkInternado);
        painelStatus.add(painelVisitas);
        painelStatus.add(Box.createVerticalStrut(10));

        // --- BOTÕES DE AÇÃO (Documentos) ---
        // --- 3. ALTERAÇÃO: Mudança de Layout e Tamanho dos Botões ---

        // Usamos FlowLayout agora para os botões não esticarem
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

        // Colocamos o status no Norte do centro para ele não esticar infinitamente para baixo
        painelCentral.add(painelStatus, BorderLayout.NORTH);
        painelCentral.add(painelAcoes, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // --- RODAPÉ ---
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnFinalizar = new JButton("Finalizar Atendimento");
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
                chkNaoAptoVisitas.setSelected(true);
            }
            revalidate();
            repaint();
        });

        btnAtestado.addActionListener(e -> abrirTelaAtestado());
        btnExame.addActionListener(e -> abrirTelaExame());
        btnReceita.addActionListener(e -> abrirTelaReceita());
        btnFinalizar.addActionListener(e -> finalizarAtendimento());
    }

    // Método auxiliar ajustado para botões menores e fixos
    private JButton criarBotaoAcao(String texto) {
        JButton btn = new JButton("<html><center>" + texto.replace(" ", "<br>") + "</center></html>");
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        // Define um tamanho fixo razoável (Largura, Altura)
        btn.setPreferredSize(new Dimension(180, 80));
        return btn;
    }

    private void finalizarAtendimento() {
        MedicoController medicoController = new MedicoController();
        if(chkInternado.isSelected()){
            medicoController.StatusPaciente(medico, paciente, true, chkAptoVisitas.isSelected());
            if(chkAptoVisitas.isSelected())
                JOptionPane.showMessageDialog(this, "Paciente internado.\nStatus: Apto para visitas");
            else
                JOptionPane.showMessageDialog(this, "Paciente internado.\nStatus: Não apto para visitas");
        }
        else
            medicoController.StatusPaciente(medico, paciente, false, false);
    }

    private void abrirTelaAtestado() {
        JOptionPane.showMessageDialog(this, "Abrir tela de Atestado...");
    }

    private void abrirTelaExame() {
        JOptionPane.showMessageDialog(this, "Abrir tela de Exame...");
    }

    private void abrirTelaReceita() {
        JOptionPane.showMessageDialog(this, "Abrir tela de Receita...");
    }
}