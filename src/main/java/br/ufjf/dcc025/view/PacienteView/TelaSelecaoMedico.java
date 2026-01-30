//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.view.PacienteView;

import br.ufjf.dcc025.controller.PacienteController;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;
import br.ufjf.dcc025.view.TelasConsulta.TelaSelecaoConsulta;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaSelecaoMedico extends JFrame {

    private Paciente pacienteLogado;
    private PacienteController controller;

    private JList<Medico> listaMedicos;
    private DefaultListModel<Medico> listModel;

    public TelaSelecaoMedico(Paciente paciente) {
        this.pacienteLogado = paciente;
        this.controller = new PacienteController();

        setTitle("Meu Histórico - Paciente: " + paciente.getNome());
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // cabeçalho
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTopo.setBackground(new Color(240, 248, 255));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblTitulo = new JLabel("Selecione um Médico para ver o Histórico");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0, 102, 204));

        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaMedicos = new JList<>(listModel);

        // mostrar Nome e Especialidade
        listaMedicos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Medico) {
                    Medico m = (Medico) value;
                    setText(m.getNome() + " - " + m.getEspecialidade());
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaMedicos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Médicos Consultados"));
        add(scrollPane, BorderLayout.CENTER);

        // botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar");
        JButton btnVerConsultas = new JButton("Ver Consultas");

        btnVerConsultas.setBackground(new Color(0, 102, 204));
        btnVerConsultas.setForeground(Color.WHITE);
        btnVerConsultas.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnVerConsultas);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarMedicos();

        btnVoltar.addActionListener(e -> dispose());

        btnVerConsultas.addActionListener(e -> {
            Medico medicoSelecionado = listaMedicos.getSelectedValue();

            if (medicoSelecionado != null) {
                new TelaSelecaoConsulta(pacienteLogado, medicoSelecionado).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um médico da lista.");
            }
        });
    }

    private void carregarMedicos() {
        listModel.clear();
        List<Medico> medicos = controller.listarMedicosDoPaciente(pacienteLogado);
        if (medicos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Você ainda não possui histórico de consultas com médicos cadastrados.");
        } else {
            for (Medico m : medicos) {
                listModel.addElement(m);
            }
        }
    }
}