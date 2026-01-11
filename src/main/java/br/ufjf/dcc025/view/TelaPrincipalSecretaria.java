package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.AgendamentoController;
import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Secretaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class TelaPrincipalSecretaria extends JFrame {

    private Secretaria secretariaLogada;

    // Controllers
    private final AgendamentoController agendamentoController;
    private final MedicoController medicoController;

    // Componentes Visuais
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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o app ao sair
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- MENU SUPERIOR ---
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo"); // Mudei de "Cadastros" para "Arquivo" ficar mais padrão
        JMenu menuCadastros = new JMenu("Cadastros");

        JMenuItem itemNovoPaciente = new JMenuItem("Novo Paciente");
        JMenuItem itemNovoFuncionario = new JMenuItem("Novo Funcionário");
        JMenuItem itemSair = new JMenuItem("Sair / Logout");

        // Montando Menu Cadastros
        menuCadastros.add(itemNovoPaciente);
        menuCadastros.add(itemNovoFuncionario);

        // Montando Menu Arquivo
        menuArquivo.add(menuCadastros); // Submenu
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);

        menuBar.add(menuArquivo);

        // Adiciona um botão de Sair direto na barra de menu (lado direito)
        menuBar.add(Box.createHorizontalGlue()); // Empurra para a direita
        JButton btnSairBarra = new JButton("Sair");
        btnSairBarra.setFocusPainted(false);
        btnSairBarra.setBackground(new Color(200, 50, 50));
        btnSairBarra.setForeground(Color.WHITE);
        menuBar.add(btnSairBarra);

        setJMenuBar(menuBar);

        // --- ABAS ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard & Monitoramento", criarPainelDashboard());
        tabbedPane.addTab("Gerenciar Médicos", criarPainelGestaoMedicos());

        add(tabbedPane, BorderLayout.CENTER);

        // --- RODAPÉ (Relógio simples) ---
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.lblRelogio = new JLabel("Data: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        rodape.add(this.lblRelogio);
        add(rodape, BorderLayout.SOUTH);

        this.atualizarRelogio();

        // --- AÇÕES ---
        itemNovoPaciente.addActionListener(e -> new TelaCadastroPaciente().setVisible(true));
        itemNovoFuncionario.addActionListener(e -> new TelaCadastroFuncionario().setVisible(true));
        ActionListener acaoSair = e -> realizarLogout();

        itemSair.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose();
        });
        itemSair.addActionListener(acaoSair);
        btnSairBarra.addActionListener(acaoSair);

        // Evento ao abrir a tela: Verificar faltas automaticamente
        SwingUtilities.invokeLater(this::verificarFaltasAutomaticamente);
    }

    // ---------------------------------------------------------
    // ABA 1: DASHBOARD (Médicos no Turno + Alerta de Faltas)
    // ---------------------------------------------------------
    private JPanel criarPainelDashboard() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Topo: Botões de Ação Rápida
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAtualizar = new JButton("Atualizar Dashboard");
        JButton btnVerificarFaltas = new JButton("Verificar Ausências");

        // Estilo alerta para o botão de faltas
        btnVerificarFaltas.setBackground(new Color(204, 0, 0));
        btnVerificarFaltas.setForeground(Color.WHITE);

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnVerificarFaltas);
        painel.add(painelBotoes, BorderLayout.NORTH);

        // Centro: Tabela de Quem está trabalhando AGORA
        String[] colunas = {"Médico", "Especialidade", "Status"};
        modeloPlantao = new DefaultTableModel(colunas, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        tabelaMedicosPlantao = new JTable(modeloPlantao);

        JScrollPane scroll = new JScrollPane(tabelaMedicosPlantao);
        scroll.setBorder(BorderFactory.createTitledBorder("Médicos em Atendimento Agora (Turno Vigente)"));
        painel.add(scroll, BorderLayout.CENTER);

        // Listeners
        btnAtualizar.addActionListener(e -> {
            this.carregarMedicosNoTurno();
            this.atualizarRelogio();
            }
        );
        btnVerificarFaltas.addActionListener(e -> verificarFaltasAutomaticamente());

        // Carga inicial
        carregarMedicosNoTurno();

        return painel;
    }

    private void realizarLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente sair do sistema?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new TelaLogin().setVisible(true);
            dispose(); // Fecha a tela atual
        }
    }

    // ---------------------------------------------------------
    // ABA 2: GESTÃO DE MÉDICOS (Ativar/Inativar)
    // ---------------------------------------------------------
    private JPanel criarPainelGestaoMedicos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Nome", "Especialidade", "Situação"};
        modeloGestao = new DefaultTableModel(colunas, 0) {
            @Override // Bloqueia edição direta na célula
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaGestaoMedicos = new JTable(modeloGestao);

        JScrollPane scroll = new JScrollPane(tabelaGestaoMedicos);
        painel.add(scroll, BorderLayout.CENTER);

        // Botões de Ação
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAtualizarLista = new JButton("Recarregar Lista");
        JButton btnAlternarStatus = new JButton("Ativar/Desativar Selecionado");

        painelSul.add(btnAtualizarLista);
        painelSul.add(btnAlternarStatus);
        painel.add(painelSul, BorderLayout.SOUTH);

        // Ações
        btnAtualizarLista.addActionListener(e -> carregarTodosMedicos());

        btnAlternarStatus.addActionListener(e -> {
            int linhaSelecionada = tabelaGestaoMedicos.getSelectedRow();
            if (linhaSelecionada >= 0) {
                // Pega o médico correspondente na lista (Cuidado: assume ordem igual à lista estática)
                // Ideal: Usar um 'getValueAt' com ID ou CPF para buscar.
                // Aqui simplificamos pegando pelo índice da view mapeado na lista do model.
                Medico m = DadosHospital.medicos.get(linhaSelecionada);
                alternarStatusMedico(m);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um médico na tabela.");
            }
        });

        carregarTodosMedicos();
        return painel;
    }
    private void atualizarRelogio()
    {
        lblRelogio.setText(
                "Data: " + LocalDateTime.of(2026,1,12,15,0).format(//LocalDateTime.now().format( //Original;
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );
    }
    // --- CONEXÃO COM CONTROLLERS ---

    private void carregarMedicosNoTurno() {
        modeloPlantao.setRowCount(0);
        // CHAMA AGENDAMENTO CONTROLLER: Quem está ativo AGORA?
        LocalDate dataHj = LocalDate.now();
        LocalTime turno = LocalTime.now();

        LocalDateTime dataHorarioAtual = LocalDateTime.of(dataHj, turno);

        //List<Medico> medicosNoTurno = agendamentoController.medicosDisponiveisAgora(dataHj, turno); //Tem que ser assim!!!

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
        // CHAMA AGENDAMENTO CONTROLLER: Processa faltas passadas
        List<Consulta> faltasDetectadas = agendamentoController.monitoraFaltas();

        if (!faltasDetectadas.isEmpty()) {
            StringBuilder msg = new StringBuilder("ALERTA: O sistema detectou " + faltasDetectadas.size() + " paciente(s) que não compareceram:\n\n");
            for (Consulta c : faltasDetectadas) {
                msg.append("- ").append(c.getPaciente().getNome())
                        .append(" (").append(c.getDataConsulta().format(DateTimeFormatter.ofPattern("dd/MM"))).append(")\n");
            }
            msg.append("\nO status dessas consultas foi alterado para 'NÃO COMPARECEU'.");

            JOptionPane.showMessageDialog(this, msg.toString(), "Monitoramento de Assiduidade", JOptionPane.WARNING_MESSAGE);
        } else {
            // Apenas para feedback manual do botão, se for automático na inicialização pode remover o else
            // JOptionPane.showMessageDialog(this, "Nenhuma falta recente detectada.");
        }
    }

    private void carregarTodosMedicos() {
        modeloGestao.setRowCount(0);
        // Lista geral (pode vir de DadosHospital direto pois é listagem administrativa)
        for (Medico m : DadosHospital.medicos) {
            modeloGestao.addRow(new Object[]{
                    m.getNome(),
                    m.getEspecialidade(),
                    m.getStatus() ? "ATIVO" : "INATIVO" // Mostra texto amigável
            });
        }
    }

    private void alternarStatusMedico(Medico medico) {
        boolean novoStatus = !medico.getStatus(); // Inverte o status atual
        String acao = novoStatus ? "ATIVAR" : "DESATIVAR";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja " + acao + " o médico " + medico.getNome() + "?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // CHAMA MEDICO CONTROLLER
                medicoController.alterarStatusMedicos(medico, novoStatus);

                carregarTodosMedicos(); // Atualiza tabela
                carregarMedicosNoTurno(); // Atualiza dashboard também (se ele saiu, some de lá)

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }
}