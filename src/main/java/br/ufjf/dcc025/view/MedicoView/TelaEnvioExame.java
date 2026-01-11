package br.ufjf.dcc025.view.MedicoView;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaEnvioExame extends JFrame {

    private Medico medico;
    private Paciente paciente;
    private Consulta consulta;

    // Componentes
    private JTextField txtTipoExame;
    private JTextArea txtResultado;
    private JLabel lblData;

    public TelaEnvioExame(Consulta consulta) {
        this.consulta = consulta;
        this.medico = consulta.getMedico();
        this.paciente = consulta.getPaciente();

        setTitle("Solicitação de Exame");
        setSize(500, 450);
        setResizable(false);
        // DISPOSE_ON_CLOSE garante que apenas esta janela fecha, mantendo a anterior aberta
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. CABEÇALHO ---
        JPanel painelTopo = new JPanel(new GridLayout(3, 1, 5, 5));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        painelTopo.setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("Novo Exame Médico");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));

        JLabel lblPaciente = new JLabel("Paciente: " + paciente.getNome());
        lblPaciente.setFont(new Font("Arial", Font.PLAIN, 14));

        // Data atual formatada
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        lblData = new JLabel("Data de Emissão: " + agora.format(formatter));
        lblData.setFont(new Font("Arial", Font.ITALIC, 12));

        painelTopo.add(lblTitulo);
        painelTopo.add(lblPaciente);
        painelTopo.add(lblData);
        add(painelTopo, BorderLayout.NORTH);

        // --- 2. FORMULÁRIO ---
        JPanel painelForm = new JPanel(new BorderLayout(10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Tipo de Exame (Pequeno)
        JPanel painelTipo = new JPanel(new BorderLayout(5, 5));
        painelTipo.add(new JLabel("Tipo de Exame:"), BorderLayout.NORTH);
        txtTipoExame = new JTextField();
        painelTipo.add(txtTipoExame, BorderLayout.CENTER);

        // Resultado (Grande)
        JPanel painelResultado = new JPanel(new BorderLayout(5, 5));
        painelResultado.add(new JLabel("Resultado / Laudo:"), BorderLayout.NORTH);
        txtResultado = new JTextArea();
        txtResultado.setLineWrap(true); // Quebra de linha automática
        txtResultado.setWrapStyleWord(true);
        JScrollPane scrollResultado = new JScrollPane(txtResultado); // Barra de rolagem
        painelResultado.add(scrollResultado, BorderLayout.CENTER);

        // Adiciona ao centro
        painelForm.add(painelTipo, BorderLayout.NORTH);
        painelForm.add(painelResultado, BorderLayout.CENTER); // Ocupa o resto do espaço

        add(painelForm, BorderLayout.CENTER);

        // --- 3. BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnEnviar = new JButton("Enviar Exame");

        btnEnviar.setBackground(new Color(0, 102, 204));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnEnviar);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES ---

        // Botão Cancelar: Fecha apenas esta tela
        btnCancelar.addActionListener(e -> dispose());

        // Botão Enviar
        btnEnviar.addActionListener(e -> enviarExame());
    }

    private void enviarExame() {
        MedicoController medicoController = new MedicoController();
        String tipoDeExame = txtTipoExame.getText();
        String resultado = txtResultado.getText();

        if (tipoDeExame.trim().isEmpty() || resultado.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha o Tipo do Exame e o Resultado.",
                    "Campos Obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            medicoController.geraExame(consulta, tipoDeExame, resultado, null);

            JOptionPane.showMessageDialog(this, "Exame enviado com sucesso!");
            dispose(); // Fecha a tela após enviar

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao enviar exame: " + e.getMessage());
        }
    }
}