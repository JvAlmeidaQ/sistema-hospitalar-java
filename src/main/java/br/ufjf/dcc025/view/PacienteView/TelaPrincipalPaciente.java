//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.view.PacienteView;

import br.ufjf.dcc025.model.Paciente;
import br.ufjf.dcc025.view.TelaAgendamento;
import br.ufjf.dcc025.view.TelaControleVisitas;
import br.ufjf.dcc025.view.TelaLogin;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipalPaciente extends JFrame {

    private Paciente pacienteLogado;

    public TelaPrincipalPaciente(Paciente paciente) {
        this.pacienteLogado = paciente;

        setTitle("Área do Paciente - Bem-vindo(a)");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // cabeçalho
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(0, 102, 204));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel painelTexto = new JPanel(new GridLayout(2, 1));
        painelTexto.setOpaque(false);

        JLabel lblBemVindo = new JLabel("Olá, " + paciente.getNome());
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 22));
        lblBemVindo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Painel de Controle do Paciente");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(200, 220, 255));

        painelTexto.add(lblBemVindo);
        painelTexto.add(lblSubtitulo);

        JButton btnEditarPerfil = new JButton("Editar Perfil");
        btnEditarPerfil.setFont(new Font("Arial", Font.BOLD, 11));
        btnEditarPerfil.setBackground(new Color(255, 255, 255));
        btnEditarPerfil.setForeground(new Color(0, 102, 204));
        btnEditarPerfil.setFocusPainted(false);
        btnEditarPerfil.setPreferredSize(new Dimension(110, 30));

        painelTopo.add(painelTexto, BorderLayout.CENTER); // Texto ocupa o meio/esquerda
        painelTopo.add(btnEditarPerfil, BorderLayout.EAST); // Botão no canto direito

        add(painelTopo, BorderLayout.NORTH);

        //menu de opções
        JPanel painelMenu = new JPanel(new GridLayout(2, 2, 15, 15));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        painelMenu.setBackground(new Color(245, 245, 245));

        // Botão 1: Verificar Visitas
        JButton btnVisitas = criarBotaoMenu("Verificar Visitas", "Consulte pacientes internados");
        btnVisitas.addActionListener(e -> {
            // Abre a tela de controle de visitas
            new TelaControleVisitas().setVisible(true);
        });

        // Botão 2: Histórico Médico
        JButton btnHistorico = criarBotaoMenu("Histórico Médico", "Veja receitas, exames e atestados");
        btnHistorico.addActionListener(e -> {
            new TelaSelecaoMedico(pacienteLogado).setVisible(true);
        });

        // Botão 3: Gerenciar Agendamentos
        JButton btnMeusAgendamentos = criarBotaoMenu("Meus Agendamentos", "Veja e gerencie suas consultas futuras");
        btnMeusAgendamentos.addActionListener(e -> {
            new TelaGerenciarConsultas(pacienteLogado).setVisible(true);
        });

        // Botão 4: Nova Consulta
        JButton btnNovaConsulta = criarBotaoMenu("Nova Consulta", "Busque médicos e horários disponíveis");
        btnNovaConsulta.addActionListener(e -> {
            new TelaAgendamento(pacienteLogado).setVisible(true);
        });

        painelMenu.add(btnVisitas);
        painelMenu.add(btnHistorico);
        painelMenu.add(btnMeusAgendamentos);
        painelMenu.add(btnNovaConsulta);

        add(painelMenu, BorderLayout.CENTER);

        // rodapé
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        painelRodape.setBackground(Color.WHITE);

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(200, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Arial", Font.BOLD, 12));

        btnSair.addActionListener(e -> realizarLogout());

        painelRodape.add(btnSair);
        add(painelRodape, BorderLayout.SOUTH);

        //Botão editar perfil
        btnEditarPerfil.addActionListener(e -> {
            new TelaEdicaoPaciente(pacienteLogado).setVisible(true);
        });
    }

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