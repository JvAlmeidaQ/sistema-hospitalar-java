package br.ufjf.dcc025.view.PacienteView;

import br.ufjf.dcc025.controller.PacienteController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaGerenciarConsultas extends JFrame {

    private Paciente pacienteLogado;
    private PacienteController controller;

    private JList<Consulta> listaConsultas;
    private DefaultListModel<Consulta> listModel;

    public TelaGerenciarConsultas(Paciente paciente) {
        this.pacienteLogado = paciente;
        this.controller = new PacienteController();

        setTitle("Meus Agendamentos");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // cabeçalho
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTopo.setBackground(new Color(240, 248, 255));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblTitulo = new JLabel("Gerenciar Consultas Agendadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));

        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaConsultas = new JList<>(listModel);

        listaConsultas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Consulta) {
                    Consulta c = (Consulta) value;
                    DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    String texto = String.format("Data: %s às %s - %s (%s) - [%s]",
                            c.getDataConsulta().format(fmtData),
                            c.getHorarioConsulta().getInicio(),
                            c.getMedico().getNome(),
                            c.getMedico().getEspecialidade(),
                            c.getStatusConsulta()); // Mostra AGENDADA ou REMARCADA

                    setText(texto);
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                    if (isSelected) {
                        setBackground(new Color(200, 230, 255));
                        setForeground(Color.BLACK);
                    }
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaConsultas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Próximos Compromissos"));
        add(scrollPane, BorderLayout.CENTER);

        // botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnVoltar = new JButton("Voltar");
        JButton btnCancelar = new JButton("Cancelar Consulta");
        JButton btnRemarcar = new JButton("Remarcar Consulta");

        // Estilos
        btnCancelar.setBackground(new Color(220, 50, 50)); // Vermelho
        btnCancelar.setForeground(Color.WHITE);

        btnRemarcar.setBackground(new Color(220, 212, 50)); // Amarelo
        btnRemarcar.setForeground(Color.WHITE);

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnRemarcar);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarConsultas();

        btnVoltar.addActionListener(e -> dispose());

        btnCancelar.addActionListener(e -> {
            Consulta selecionada = listaConsultas.getSelectedValue();
            if (selecionada != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja CANCELAR esta consulta?\nEssa ação não pode ser desfeita.",
                        "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        controller.cancelarConsulta(selecionada);
                        JOptionPane.showMessageDialog(this, "Consulta cancelada com sucesso!");
                        carregarConsultas(); // Atualiza a lista removendo a cancelada
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma consulta para cancelar.");
            }
        });

        btnRemarcar.addActionListener(e -> {
            Consulta selecionada = listaConsultas.getSelectedValue();
            if (selecionada != null) {
                // Aqui abriríamos a tela de Seleção de Horário
                // Como ainda não temos, exibimos um aviso ou chamamos um placeholder
                JOptionPane.showMessageDialog(this,
                        "Para remarcar, você será redirecionado para a escolha de novo horário.\n" +
                                "(Funcionalidade a ser conectada com a tela de Busca de Médicos)",
                        "Remarcar", JOptionPane.INFORMATION_MESSAGE);
                // new TelaBuscaHorario(selecionada.getMedico(), consultaParaRemarcar).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma consulta para remarcar.");
            }
        });
    }

    private void carregarConsultas() {
        listModel.clear();
        List<Consulta> pendentes = controller.listarConsultasPendentes(pacienteLogado);

        if (pendentes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Você não possui consultas agendadas no momento.");
        } else {
            for (Consulta c : pendentes) {
                listModel.addElement(c);
            }
        }
    }
}