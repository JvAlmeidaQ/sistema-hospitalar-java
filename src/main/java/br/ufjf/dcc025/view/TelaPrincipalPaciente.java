package br.ufjf.dcc025.view;

import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipalPaciente extends JFrame {

    private Paciente pacienteLogado;

    public TelaPrincipalPaciente(Paciente paciente) {
        this.pacienteLogado = paciente;

        setTitle("Área do Paciente - Bem-vindo(a)");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha a aplicação se fechar esta janela
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. CABEÇALHO (Boas-vindas) ---
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(0, 102, 204));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblBemVindo = new JLabel("Olá, " + paciente.getNome());
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 22));
        lblBemVindo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Painel de Controle do Paciente");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(200, 220, 255));

        painelTopo.add(lblBemVindo, BorderLayout.NORTH);
        painelTopo.add(lblSubtitulo, BorderLayout.SOUTH);
        add(painelTopo, BorderLayout.NORTH);

        // --- 2. MENU DE OPÇÕES (GRID CENTRAL) ---
        JPanel painelMenu = new JPanel(new GridLayout(2, 2, 15, 15)); // 2 linhas, 2 colunas
        painelMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        painelMenu.setBackground(new Color(245, 245, 245));

        // Botão 1: Editar Perfil
        JButton btnPerfil = criarBotaoMenu("Editar Meu Perfil", "Atualize seus dados e endereço");
        btnPerfil.addActionListener(e -> {
            // Abre a tela de edição que já criamos
            new TelaEdicaoPaciente(pacienteLogado).setVisible(true);
        });

        // Botão 2: Histórico Médico
        JButton btnHistorico = criarBotaoMenu("Histórico Médico", "Veja receitas, exames e atestados");
        btnHistorico.addActionListener(e -> {
            // Abre o fluxo de histórico: Selecionar Médico -> Ver Consultas -> Ver Docs
            new TelaSelecaoMedico(pacienteLogado).setVisible(true);
        });

        // Botão 3: Gerenciar Agendamentos (Consultas Marcadas)
        JButton btnMeusAgendamentos = criarBotaoMenu("Meus Agendamentos", "Veja e gerencie suas consultas futuras");
        btnMeusAgendamentos.addActionListener(e -> {
            // TODO: Criar TelaMeusAgendamentos
            JOptionPane.showMessageDialog(this, "Funcionalidade 'Meus Agendamentos' será implementada a seguir.");
        });

        // Botão 4: Nova Consulta (Disponibilidade)
        JButton btnNovaConsulta = criarBotaoMenu("Nova Consulta", "Busque médicos e horários disponíveis");
        btnNovaConsulta.setBackground(new Color(230, 240, 255)); // Destaque leve
        btnNovaConsulta.addActionListener(e -> {
            new TelaAgendamento(pacienteLogado).setVisible(true);
        });

        painelMenu.add(btnPerfil);
        painelMenu.add(btnHistorico);
        painelMenu.add(btnMeusAgendamentos);
        painelMenu.add(btnNovaConsulta);

        add(painelMenu, BorderLayout.CENTER);

        // --- 3. RODAPÉ (LOGOUT) ---
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        painelRodape.setBackground(Color.WHITE);

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(200, 50, 50)); // Vermelho
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Arial", Font.BOLD, 12));

        btnSair.addActionListener(e -> realizarLogout());

        painelRodape.add(btnSair);
        add(painelRodape, BorderLayout.SOUTH);
    }

    // --- MÉTODOS AUXILIARES ---

    private JButton criarBotaoMenu(String titulo, String subtitulo) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblSub = new JLabel(subtitulo, SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 10));
        lblSub.setForeground(Color.GRAY);

        btn.add(lblTitulo, BorderLayout.CENTER);
        btn.add(lblSub, BorderLayout.SOUTH);

        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        return btn;
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
}