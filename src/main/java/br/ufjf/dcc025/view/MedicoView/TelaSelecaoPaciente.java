package br.ufjf.dcc025.view.MedicoView;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;
import br.ufjf.dcc025.view.TelaSelecaoConsulta;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaSelecaoPaciente extends JFrame {

    private Medico medicoLogado;
    private MedicoController controller;

    private JList<Paciente> listaPacientes;
    private DefaultListModel<Paciente> listModel;

    public TelaSelecaoPaciente(Medico medico) {
        this.medicoLogado = medico;
        this.controller = new MedicoController();

        setTitle("Meus Pacientes - Dr(a). " + medico.getNome());
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- CABEÇALHO ---
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTopo.setBackground(new Color(240, 248, 255));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // 1. ALTERAÇÃO: Título modificado conforme solicitado
        JLabel lblTitulo = new JLabel("Selecione um Paciente");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0, 102, 204));

        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        // --- LISTA DE PACIENTES ---
        listModel = new DefaultListModel<>();
        listaPacientes = new JList<>(listModel);

        listaPacientes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Paciente) {
                    Paciente p = (Paciente) value;
                    setText(p.getNome() + " (CPF: " + p.getCpf() + ")");
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaPacientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pacientes Atendidos"));
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar");

        // 2. ALTERAÇÃO: Novo botão adicionado
        JButton btnStatus = new JButton("Status Paciente");
        JButton btnVerConsultas = new JButton("Ver Consultas");

        // Estilização
        btnVerConsultas.setBackground(new Color(0, 102, 204));
        btnVerConsultas.setForeground(Color.WHITE);
        btnVerConsultas.setFont(new Font("Arial", Font.BOLD, 12));

        btnStatus.setBackground(new Color(255, 153, 51)); // Cor laranja para destacar
        btnStatus.setForeground(Color.WHITE);
        btnStatus.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnStatus); // Adiciona o botão na tela
        painelBotoes.add(btnVerConsultas);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES ---
        carregarPacientes();

        btnVoltar.addActionListener(e -> dispose());

        btnVerConsultas.addActionListener(e -> {
            Paciente pacienteSelecionado = listaPacientes.getSelectedValue();
            if (pacienteSelecionado != null) {
                new TelaSelecaoConsulta(pacienteSelecionado, medicoLogado).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um paciente da lista.");
            }
        });

        // 3. ALTERAÇÃO: Ação do botão Status
        btnStatus.addActionListener(e -> {
            Paciente pacienteSelecionado = listaPacientes.getSelectedValue();
            if (pacienteSelecionado != null) {
                // Abre a nova tela de Status (código abaixo)
                new TelaStatusPaciente(medicoLogado, pacienteSelecionado).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um paciente para alterar o status.");
            }
        });
    }

    private void carregarPacientes() {
        listModel.clear();
        List<Paciente> pacientes = controller.listarPacientesDoMedico(medicoLogado);

        if (pacientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Você ainda não possui atendimentos registrados com pacientes.");
        } else {
            for (Paciente p : pacientes) {
                listModel.addElement(p);
            }
        }
    }
}