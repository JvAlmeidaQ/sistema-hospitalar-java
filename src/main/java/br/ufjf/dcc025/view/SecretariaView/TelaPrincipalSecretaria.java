package br.ufjf.dcc025.view.SecretariaView;

import br.ufjf.dcc025.controller.AgendamentoController;
import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Secretaria;
import br.ufjf.dcc025.view.MedicoView.TelaHorariosTrabalho;
import br.ufjf.dcc025.view.TelaControleVisitas;
import br.ufjf.dcc025.view.TelaLogin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaPrincipalSecretaria extends JFrame {

    private Secretaria secretariaLogada;

    private final AgendamentoController agendamentoController;
    private final MedicoController medicoController;

    private JTable tabelaMedicosPlantao;
    private JTable tabelaGestaoMedicos;
    private DefaultTableModel modeloPlantao;
    private DefaultTableModel modeloGestao;
    private JLabel lblRelogio;

    public TelaPrincipalSecretaria(Secretaria secretaria) {
        this.secretariaLogada = secretaria;
        this.agendamentoController = new AgendamentoController();
        this.medicoController = new MedicoController();

        setTitle("Painel Administrativo - " + secretaria.getNome());
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuCadastros = new JMenu("Cadastros");
        JMenuItem itemNovoPaciente = new JMenuItem("Novo Paciente");
        JMenuItem itemNovoFuncionario = new JMenuItem("Novo Funcionário");
        JMenuItem itemSair = new JMenuItem("Sair / Logout");

        menuCadastros.add(itemNovoPaciente);
        menuCadastros.add(itemNovoFuncionario);
        menuArquivo.add(menuCadastros);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);

        JMenu menuPerfil = new JMenu("Meu Perfil");
        JMenuItem itemEditarPerfil = new JMenuItem("Editar Meus Dados");
        menuPerfil.add(itemEditarPerfil);

        menuBar.add(menuArquivo);
        menuBar.add(menuPerfil);

        menuBar.add(Box.createHorizontalGlue());
        JButton btnSairBarra = new JButton("Sair");
        btnSairBarra.setFocusPainted(false);
        btnSairBarra.setBackground(new Color(200, 50, 50));
        btnSairBarra.setForeground(Color.WHITE);
        menuBar.add(btnSairBarra);

        setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard & Operacional", criarPainelDashboard());
        tabbedPane.addTab("Gerenciar Médicos", criarPainelGestaoMedicos());

        add(tabbedPane, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.lblRelogio = new JLabel("Data: --/--/----");
        rodape.add(this.lblRelogio);
        add(rodape, BorderLayout.SOUTH);

        this.atualizarRelogio();

        itemNovoPaciente.addActionListener(e -> new TelaCadastroPaciente().setVisible(true));
        itemNovoFuncionario.addActionListener(e -> new TelaCadastroFuncionario().setVisible(true));

        itemEditarPerfil.addActionListener(e -> {
            new TelaEdicaoSecretaria(secretariaLogada).setVisible(true);
        });

        ActionListener acaoSair = e -> realizarLogout();
        itemSair.addActionListener(acaoSair);
        btnSairBarra.addActionListener(acaoSair);

        // Evento ao abrir: Verificar faltas
        SwingUtilities.invokeLater(this::verificarFaltasAutomaticamente);
    }

    private JPanel criarPainelDashboard() {
        JPanel painelGlobal = new JPanel(new BorderLayout(10, 10));
        painelGlobal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel painelAcoesRapidas = new JPanel(new GridLayout(1, 4, 15, 0));
        painelAcoesRapidas.setBorder(BorderFactory.createTitledBorder("Acesso Rápido"));
        painelAcoesRapidas.setPreferredSize(new Dimension(0, 80));

        JButton btnNovoPaciente = new JButton("Novo Paciente");
        JButton btnControleVisitas = new JButton("Controle de Visitas");
        JButton btnVerificarFaltas = new JButton("Verificar Ausências");
        JButton btnAtualizar = new JButton("Atualizar Painel");

        btnNovoPaciente.setFont(new Font("Arial", Font.BOLD, 12));
        btnNovoPaciente.setForeground(new Color(0, 102, 204));

        btnControleVisitas.setFont(new Font("Arial", Font.BOLD, 12));
        btnControleVisitas.setForeground(new Color(0, 102, 102));

        btnVerificarFaltas.setBackground(new Color(204, 0, 0));
        btnVerificarFaltas.setForeground(Color.WHITE);

        painelAcoesRapidas.add(btnNovoPaciente);
        painelAcoesRapidas.add(btnControleVisitas);
        painelAcoesRapidas.add(btnVerificarFaltas);
        painelAcoesRapidas.add(btnAtualizar);

        painelGlobal.add(painelAcoesRapidas, BorderLayout.NORTH);

        String[] colunas = {"Médico", "Especialidade", "Status"};
        modeloPlantao = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaMedicosPlantao = new JTable(modeloPlantao);
        JScrollPane scroll = new JScrollPane(tabelaMedicosPlantao);
        scroll.setBorder(BorderFactory.createTitledBorder("Médicos em Atendimento Agora (Turno Vigente)"));

        painelGlobal.add(scroll, BorderLayout.CENTER);

        btnNovoPaciente.addActionListener(e -> new TelaCadastroPaciente().setVisible(true));

        btnControleVisitas.addActionListener(e -> {
            new TelaControleVisitas().setVisible(true);
        });

        btnAtualizar.addActionListener(e -> {
            this.carregarMedicosNoTurno();
            this.atualizarRelogio();
        });
        btnVerificarFaltas.addActionListener(e -> verificarFaltasAutomaticamente());

        carregarMedicosNoTurno();

        return painelGlobal;
    }

    private JPanel criarPainelGestaoMedicos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Nome", "Especialidade", "Situação"};

        modeloGestao = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaGestaoMedicos = new JTable(modeloGestao);

        JScrollPane scroll = new JScrollPane(tabelaGestaoMedicos);
        painel.add(scroll, BorderLayout.CENTER);

        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnNovoMedico = new JButton("Cadastrar Novo Médico");
        JButton btnAgenda = new JButton("Gerenciar Agenda/Horários");
        JButton btnAtualizarLista = new JButton("Recarregar Lista");
        JButton btnAlternarStatus = new JButton("Ativar/Desativar");

        // Estilo
        btnAgenda.setBackground(new Color(0, 102, 204));
        btnAgenda.setForeground(Color.WHITE);

        painelSul.add(btnNovoMedico);
        painelSul.add(btnAgenda);
        painelSul.add(btnAtualizarLista);
        painelSul.add(btnAlternarStatus);

        painel.add(painelSul, BorderLayout.SOUTH);


        btnAgenda.addActionListener(e -> {
            int linhaSelecionada = tabelaGestaoMedicos.getSelectedRow();
            if (linhaSelecionada >= 0) {
                Medico medicoSelecionado = DadosHospital.medicos.get(linhaSelecionada);
                new TelaHorariosTrabalho(medicoSelecionado).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um médico na tabela para configurar a agenda dele.");
            }
        });

        btnAtualizarLista.addActionListener(e -> carregarTodosMedicos());


        carregarTodosMedicos();

        return painel;
    }

    private void realizarLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente sair do sistema?", "Sair", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            for (java.awt.Window window : java.awt.Window.getWindows()) {
                window.dispose();
            }
            new TelaLogin().setVisible(true);
        }
    }

    private void atualizarRelogio() {
        // data de teste
        lblRelogio.setText("Data Simulada: " + LocalDateTime.of(2026,1,12,15,0).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }

    private void carregarMedicosNoTurno() {
        modeloPlantao.setRowCount(0);

        // dados de teste
        List<Medico> medicosNoTurno = agendamentoController.medicosDisponiveisAgora(LocalDate.of(2026,1,12), LocalTime.of(14,0));

        for (Medico m : medicosNoTurno) {
            modeloPlantao.addRow(new Object[]{
                    m.getNome(),
                    m.getEspecialidade(),
                    "EM TURNO"
            });
        }
    }

    private void verificarFaltasAutomaticamente() {
        List<Consulta> faltasDetectadas = agendamentoController.monitoraFaltas();

        if (!faltasDetectadas.isEmpty()) {
            StringBuilder msg = new StringBuilder("ALERTA: " + faltasDetectadas.size() + " ausências detectadas:\n\n");
            for (Consulta c : faltasDetectadas) {
                msg.append("- ").append(c.getPaciente().getNome())
                        .append(" (").append(c.getDataConsulta().format(DateTimeFormatter.ofPattern("dd/MM"))).append(")\n");
            }
            msg.append("\nStatus alterado para 'NÃO COMPARECEU'.");
            JOptionPane.showMessageDialog(this, msg.toString(), "Assiduidade", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void carregarTodosMedicos() {
        modeloGestao.setRowCount(0);
        for (Medico m : DadosHospital.medicos) {
            modeloGestao.addRow(new Object[]{
                    m.getNome(),
                    m.getEspecialidade(),
                    m.getStatus() ? "ATIVO" : "INATIVO"
            });
        }
    }

    private void alternarStatusMedico(Medico medico) {
        boolean novoStatus = !medico.getStatus();
        String acao = novoStatus ? "ATIVAR" : "DESATIVAR";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja " + acao + " o médico " + medico.getNome() + "?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                medicoController.alterarStatusMedicos(medico, novoStatus);
                carregarTodosMedicos();
                carregarMedicosNoTurno();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }
}