package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.PacienteController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaSelecaoConsulta extends JFrame {

    private Paciente paciente;
    private Medico medico;
    private PacienteController pacienteController;

    private JList<Consulta> listaConsultas;
    private DefaultListModel<Consulta> listModel;

    public TelaSelecaoConsulta(Paciente paciente, Medico medico) {
        this.paciente = paciente;
        this.medico = medico;
        this.pacienteController = new PacienteController();

        setTitle("Histórico de Consultas");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- CABEÇALHO ---
        JPanel painelTopo = new JPanel(new GridLayout(2, 1));
        painelTopo.setBackground(new Color(240, 248, 255));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Selecione uma Consulta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0, 102, 204));

        JLabel lblInfo = new JLabel("Paciente: " + paciente.getNome() + " | Médico: " + medico.getNome(), SwingConstants.CENTER);

        painelTopo.add(lblTitulo);
        painelTopo.add(lblInfo);
        add(painelTopo, BorderLayout.NORTH);

        // --- LISTA DE CONSULTAS ---
        listModel = new DefaultListModel<>();
        listaConsultas = new JList<>(listModel);

        // Renderizador customizado para mostrar a Data bonitinha na lista
        listaConsultas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Consulta) {
                    Consulta c = (Consulta) value;
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    // Exibe: "Data: 15/05/2025 - Status: REALIZADA"
                    setText("Data: " + c.getDataConsulta().format(fmt) + " - " + c.getHorarioConsulta().getInicio() + " (" + c.getStatusConsulta() + ")");
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaConsultas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Consultas Encontradas"));
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar");
        JButton btnVerDocumentos = new JButton("Ver Documentos");

        btnVerDocumentos.setBackground(new Color(0, 102, 204));
        btnVerDocumentos.setForeground(Color.WHITE);

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnVerDocumentos);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES ---
        carregarConsultas();

        btnVoltar.addActionListener(e -> dispose());

        btnVerDocumentos.addActionListener(e -> {
            Consulta consultaSelecionada = listaConsultas.getSelectedValue();
            if (consultaSelecionada != null) {
                // Abre a próxima tela passando a consulta selecionada
                new TelaSelecaoDocumento(consultaSelecionada).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma consulta para prosseguir.");
            }
        });
    }

    private void carregarConsultas() {
        // Usa o Controller para pegar a lista filtrada
        List<Consulta> consultas = pacienteController.buscarConsultasPorMedico(paciente, medico);

        listModel.clear();
        if (consultas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma consulta encontrada entre este Paciente e Médico.");
        } else {
            for (Consulta c : consultas) {
                listModel.addElement(c);
            }
        }
    }
}