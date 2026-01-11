package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.StatusConsulta;
import br.ufjf.dcc025.view.MedicoView.TelaAtendimentoMedico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaPrincipalMedico extends JFrame {

    private Medico medicoLogado;
    private MedicoController controller;

    private JTable tabelaAgenda;
    private DefaultTableModel modeloTabela;

    // Lista auxiliar para manter referência aos objetos Consulta mostrados na tabela
    private List<Consulta> consultasDoDia;

    public TelaPrincipalMedico(Medico medico) {
        this.medicoLogado = medico;
        this.controller = new MedicoController();

        setTitle("Estação de Trabalho - Dr(a). " + medico.getNome());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TOPO (Info do Médico + Logout) ---
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(0, 102, 204));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblNome = new JLabel("Médico: " + medico.getNome());
        lblNome.setForeground(Color.WHITE);
        lblNome.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> logout());

        painelTopo.add(lblNome, BorderLayout.WEST);
        painelTopo.add(btnSair, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        // --- CENTRO (Agenda do Dia) ---
        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblAgenda = new JLabel("Minha Agenda de Hoje");
        lblAgenda.setFont(new Font("Arial", Font.BOLD, 18));
        lblAgenda.setForeground(new Color(60, 60, 60));
        painelCentro.add(lblAgenda, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Horário", "Paciente", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaAgenda = new JTable(modeloTabela);
        tabelaAgenda.setRowHeight(25);
        tabelaAgenda.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(tabelaAgenda);
        painelCentro.add(scroll, BorderLayout.CENTER);

        add(painelCentro, BorderLayout.CENTER);

        // --- RODAPÉ (Ações) ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        JButton btnAtualizar = new JButton("Atualizar Agenda");
        JButton btnAtender = new JButton("INICIAR ATENDIMENTO");

        // Estilo do botão principal
        btnAtender.setBackground(new Color(0, 153, 76));
        btnAtender.setForeground(Color.WHITE);
        btnAtender.setFont(new Font("Arial", Font.BOLD, 14));
        btnAtender.setPreferredSize(new Dimension(200, 40));

        painelAcoes.add(btnAtualizar);
        painelAcoes.add(btnAtender);
        add(painelAcoes, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnAtualizar.addActionListener(e -> carregarAgenda());

        btnAtender.addActionListener(e -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha >= 0) {
                abrirTelaAtendimento(linha);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um paciente na lista para atender.");
            }
        });

        // Carregar dados ao abrir
        carregarAgenda();
    }

    private void carregarAgenda() {
        modeloTabela.setRowCount(0);

        // CHAMA CONTROLLER (Método Novo que pedi pra você criar)
        consultasDoDia = controller.consultasDoDia(medicoLogado);

        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

        if (consultasDoDia.isEmpty()) {
            // Pode mostrar uma linha vazia ou mensagem, ou deixar em branco
        } else {
            for (Consulta c : consultasDoDia) {
                modeloTabela.addRow(new Object[]{
                        c.getHorarioConsulta().getInicio().format(horaFmt),
                        c.getPaciente().getNome(),
                        c.getStatusConsulta() // AGENDADA, REALIZADA, etc.
                });
            }
        }
    }

    private void abrirTelaAtendimento(int indexConsulta) {
        Consulta consultaSelecionada = consultasDoDia.get(indexConsulta);

        if (consultaSelecionada.getStatusConsulta() == StatusConsulta.CANCELADA ||
                consultaSelecionada.getStatusConsulta() == StatusConsulta.NAO_COMPARECEU) {
            JOptionPane.showMessageDialog(this, "Esta consulta foi cancelada ou o paciente faltou.");
            return;
        }

        // AQUI ESTÁ A INTEGRAÇÃO:
        // Passamos a consulta direto para a tela de atendimento.
        // Se você já tem a TelaAtendimentoMedico, instancie ela aqui:

        TelaAtendimentoMedico tela = new TelaAtendimentoMedico(consultaSelecionada);
        tela.setVisible(true);
        this.dispose(); // Opcional: fechar a agenda enquanto atende ou deixar aberta atrás

        JOptionPane.showMessageDialog(this, "Aqui abrirá a TelaAtendimentoMedico para: " + consultaSelecionada.getPaciente().getNome());
    }

    private void logout() {
        new TelaLogin().setVisible(true);
        dispose();
    }
}