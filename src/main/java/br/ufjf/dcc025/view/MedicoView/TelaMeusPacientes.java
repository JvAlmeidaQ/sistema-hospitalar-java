

package br.ufjf.dcc025.view.MedicoView;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;
import br.ufjf.dcc025.view.MedicoView.TelaStatusPaciente;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaMeusPacientes extends JFrame {

    private Medico medicoLogado;
    private MedicoController controller;

    private JList<Paciente> listaPacientes;
    private DefaultListModel<Paciente> listModel;

    public TelaMeusPacientes(Medico medico) {
        this.medicoLogado = medico;
        this.controller = new MedicoController();

        setTitle("Gestão de Pacientes - Dr(a). " + medico.getNome());
        setSize(600, 500); // Um pouco mais largo para ler bem
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTopo.setBackground(new Color(240, 248, 255));
        JLabel lblTitulo = new JLabel("Meus Pacientes (Histórico e Gestão)");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);


        listModel = new DefaultListModel<>();
        listaPacientes = new JList<>(listModel);


        listaPacientes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Paciente) {
                    Paciente paciente = (Paciente) value;
                    setText("👤 " + paciente.getNome() + "  |  CPF: " + paciente.getCpf());
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Espaçamento interno
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaPacientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pacientes que já consultaram com você"));
        add(scrollPane, BorderLayout.CENTER);


        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnVoltar = new JButton("Voltar");
        JButton btnStatus = new JButton("Gerenciar Internação");
        JButton btnHistorico = new JButton("Ver Prontuário Completo");


        btnStatus.setBackground(new Color(255, 140, 0));
        btnStatus.setForeground(Color.WHITE);

        btnHistorico.setBackground(new Color(0, 102, 204));
        btnHistorico.setForeground(Color.WHITE);

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnStatus);
        painelBotoes.add(btnHistorico);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarPacientes();

        btnVoltar.addActionListener(e -> dispose());

        btnStatus.addActionListener(e -> {
            Paciente p = listaPacientes.getSelectedValue();
            if (p != null) {
                new TelaStatusPaciente(medicoLogado, p).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um paciente.");
            }
        });

        btnHistorico.addActionListener(e -> mostrarHistorico());
    }

    private void carregarPacientes() {
        listModel.clear();
        List<Paciente> pacientes = controller.listarPacientesDoMedico(medicoLogado);

        if (pacientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum paciente encontrado no seu histórico.");
        }
        for (Paciente p : pacientes) {
            listModel.addElement(p);
        }
    }

    private void mostrarHistorico() {
        Paciente paciente = listaPacientes.getSelectedValue();
        if (paciente == null) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente para ver o prontuário.");
            return;
        }

        String textoHistorico = controller.gerarTextoHistorico(paciente, medicoLogado);


        JTextArea textArea = new JTextArea(textoHistorico);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scroll, "Prontuário: " + paciente.getNome(), JOptionPane.PLAIN_MESSAGE);
    }
}