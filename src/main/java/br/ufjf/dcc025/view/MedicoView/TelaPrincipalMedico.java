package br.ufjf.dcc025.view.MedicoView;

import br.ufjf.dcc025.controller.AgendamentoController;
import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.StatusConsulta;
import br.ufjf.dcc025.view.TelaLogin;
import br.ufjf.dcc025.view.TelaMeusPacientes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaPrincipalMedico extends JFrame {

    private Medico medicoLogado;
    private MedicoController controller;

    // Componentes Visuais
    private JTable tabelaAgenda;
    private DefaultTableModel modeloTabela;
    private List<Consulta> consultasDoDia;

    public TelaPrincipalMedico(Medico medico) {
        this.medicoLogado = medico;
        this.controller = new MedicoController();

        setTitle("Estação de Trabalho - Dr(a). " + medico.getNome());
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. MENU SUPERIOR INTEGRADO ---
        JMenuBar menuBar = new JMenuBar();

        // Menu: Meu Perfil
        JMenu menuPerfil = new JMenu("Meu Perfil");
        JMenuItem itemEditar = new JMenuItem("Editar Meus Dados");
        JMenuItem itemHorarios = new JMenuItem("Configurar Agenda/Horários"); // Integração TelaConfigurarHorarios
        JMenuItem itemSair = new JMenuItem("Sair / Logout");

        menuPerfil.add(itemEditar);
        menuPerfil.add(itemHorarios);
        menuPerfil.addSeparator();
        menuPerfil.add(itemSair);

        // Menu: Gestão Clínica
        JMenu menuPacientes = new JMenu("Gestão Clínica");
        JMenuItem itemMeusPacientes = new JMenuItem("Meus Pacientes e Prontuários"); // Integração TelaMeusPacientes
        menuPacientes.add(itemMeusPacientes);

        // Adiciona à barra
        menuBar.add(menuPerfil);
        menuBar.add(menuPacientes);

        // Botão Sair na direita (Visual)
        menuBar.add(Box.createHorizontalGlue());
        JButton btnSairBarra = new JButton("Sair");
        btnSairBarra.setBackground(new Color(200, 50, 50));
        btnSairBarra.setForeground(Color.WHITE);
        btnSairBarra.setFocusPainted(false);
        menuBar.add(btnSairBarra);

        setJMenuBar(menuBar);

        // --- 2. CABEÇALHO INFORMATIVO ---
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.setBackground(new Color(0, 102, 204));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblNome = new JLabel("Médico Logado: " + medico.getNome());
        lblNome.setForeground(Color.WHITE);
        lblNome.setFont(new Font("Arial", Font.BOLD, 14));
        painelTopo.add(lblNome);

        add(painelTopo, BorderLayout.NORTH);

        // --- 3. CENTRO: TABELA DE AGENDA ---
        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblAgenda = new JLabel("Minha Agenda de Hoje (" + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        lblAgenda.setFont(new Font("Arial", Font.BOLD, 18));
        lblAgenda.setForeground(new Color(60, 60, 60));
        painelCentro.add(lblAgenda, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Horário", "Paciente", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaAgenda = new JTable(modeloTabela);
        tabelaAgenda.setRowHeight(30);
        tabelaAgenda.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(tabelaAgenda);
        painelCentro.add(scroll, BorderLayout.CENTER);

        add(painelCentro, BorderLayout.CENTER);

        // --- 4. RODAPÉ: AÇÕES ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        JButton btnAtualizar = new JButton("Atualizar Agenda");
        JButton btnAtender = new JButton("INICIAR ATENDIMENTO");

        // Estilo do botão principal
        btnAtender.setBackground(new Color(0, 153, 76));
        btnAtender.setForeground(Color.WHITE);
        btnAtender.setFont(new Font("Arial", Font.BOLD, 14));
        btnAtender.setPreferredSize(new Dimension(220, 45));

        painelAcoes.add(btnAtualizar);
        painelAcoes.add(btnAtender);
        add(painelAcoes, BorderLayout.SOUTH);


        itemEditar.addActionListener(e -> {
            try {
                new TelaEdicaoMedico(medicoLogado).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir edição: " + ex.getMessage());
            }
        });

        // Menu: Configurar Horários (A classe que criamos!)
        itemHorarios.addActionListener(e -> {
            new TelaHorariosTrabalho(medicoLogado).setVisible(true);
        });

        // Menu: Meus Pacientes (A classe adaptada)
        itemMeusPacientes.addActionListener(e -> {
            new TelaMeusPacientes(medicoLogado).setVisible(true);
        });


        itemSair.addActionListener(e -> logout());
        btnSairBarra.addActionListener(e -> logout());

        btnAtualizar.addActionListener(e -> carregarAgenda());


        btnAtender.addActionListener(e -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha >= 0) {
                abrirTelaAtendimento(linha);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um paciente na lista para atender.");
            }
        });


        carregarAgenda();

        SwingUtilities.invokeLater(this::verificarNotificacoes);
    }

    // --- MÉTODOS AUXILIARES ---

    private void carregarAgenda() {
        modeloTabela.setRowCount(0);
        consultasDoDia = controller.consultasDoDia(medicoLogado);
        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

        for (Consulta c : consultasDoDia) {
            modeloTabela.addRow(new Object[]{
                    c.getHorarioConsulta().getInicio().format(horaFmt),
                    c.getPaciente().getNome(),
                    c.getStatusConsulta()
            });
        }
    }

    private void abrirTelaAtendimento(int indexConsulta) {
        Consulta consultaSelecionada = consultasDoDia.get(indexConsulta);

        if (consultaSelecionada.getStatusConsulta() == StatusConsulta.CANCELADA ||
                consultaSelecionada.getStatusConsulta() == StatusConsulta.NAO_COMPARECEU) {
            JOptionPane.showMessageDialog(this, "Esta consulta não pode ser iniciada (Cancelada/Faltou).");
            return;
        }

        if (consultaSelecionada.getStatusConsulta() == StatusConsulta.CONCLUIDA) {
            JOptionPane.showMessageDialog(this, "Esta consulta já foi finalizada.");
            return;
        }

        new TelaAtendimentoMedico(consultaSelecionada).setVisible(true);
        dispose();
    }

    private void verificarNotificacoes() {

        AgendamentoController agendamentoCtrl = new AgendamentoController();
        List<Consulta> faltas = agendamentoCtrl.monitoraFaltas();

        long consultasHoje = consultasDoDia.stream()
                .filter(c -> c.getStatusConsulta() == StatusConsulta.AGENDADA)
                .count();

        StringBuilder msg = new StringBuilder();

        if (!faltas.isEmpty()) {
            msg.append("⚠ ALERTA DE ASSIDUIDADE:\n");
            msg.append(faltas.size()).append(" paciente(s) faltaram recentemente e foram marcados como 'NÃO COMPARECEU'.\n\n");
        }

        if (consultasHoje > 0) {
            msg.append("📅 AGENDA DE HOJE:\n");
            msg.append("Você tem ").append(consultasHoje).append(" atendimentos agendados pendentes.");
        }

        if (msg.length() > 0) {
            JOptionPane.showMessageDialog(this, msg.toString(), "Notificações do Sistema", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (Window window : Window.getWindows()) {
                window.dispose();
            }
            new TelaLogin().setVisible(true);
        }
    }
}