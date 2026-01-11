package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TelaEnvioReceita extends JFrame {

    private Consulta consulta;
    private Medico medico;
    private Paciente paciente;

    // Componentes
    private JTextField txtUsoIndicado; // Campo "doenca" ou "uso"
    private JTextField txtNovoRemedio; // Campo para digitar o remédio
    private JList<String> listaVisualRemedios; // A lista visual
    private DefaultListModel<String> modeloLista; // Onde os dados da lista ficam
    private JLabel lblData;

    public TelaEnvioReceita(Consulta consulta) {
        this.consulta = consulta;
        this.medico = consulta.getMedico();
        this.paciente = consulta.getPaciente();

        setTitle("Prescrever Receita");
        setSize(500, 450);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. CABEÇALHO ---
        JPanel painelTopo = new JPanel(new GridLayout(3, 1, 5, 5));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        painelTopo.setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("Nova Receita Médica");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));

        JLabel lblPaciente = new JLabel("Paciente: " + paciente.getNome());
        lblPaciente.setFont(new Font("Arial", Font.PLAIN, 14));

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        lblData = new JLabel("Data de Emissão: " + agora.format(formatter));
        lblData.setFont(new Font("Arial", Font.ITALIC, 12));

        painelTopo.add(lblTitulo);
        painelTopo.add(lblPaciente);
        painelTopo.add(lblData);
        add(painelTopo, BorderLayout.NORTH);

        // --- 2. FORMULÁRIO (CENTRO) ---
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 2.1 Uso Indicado (Topo do Centro)
        JPanel painelUso = new JPanel(new BorderLayout(5, 5));
        painelUso.add(new JLabel("Uso indicado para (Diagnóstico):"), BorderLayout.NORTH);
        txtUsoIndicado = new JTextField();
        painelUso.add(txtUsoIndicado, BorderLayout.CENTER);

        painelCentral.add(painelUso, BorderLayout.NORTH);

        // 2.2 Lista de Remédios (Centro do Centro)
        JPanel painelLista = new JPanel(new BorderLayout(5, 5));
        painelLista.setBorder(BorderFactory.createTitledBorder("Medicamentos Prescritos"));

        // Input de remédio + Botão Adicionar
        JPanel painelInputRemedio = new JPanel(new BorderLayout(5, 0));
        txtNovoRemedio = new JTextField();
        JButton btnAdicionar = new JButton("+");
        btnAdicionar.setToolTipText("Adicionar à lista");

        painelInputRemedio.add(txtNovoRemedio, BorderLayout.CENTER);
        painelInputRemedio.add(btnAdicionar, BorderLayout.EAST);

        painelLista.add(painelInputRemedio, BorderLayout.NORTH);

        // A Lista em si
        modeloLista = new DefaultListModel<>();
        listaVisualRemedios = new JList<>(modeloLista);
        JScrollPane scrollLista = new JScrollPane(listaVisualRemedios);
        painelLista.add(scrollLista, BorderLayout.CENTER);

        // Botão Remover (Abaixo da lista)
        JButton btnRemoverItem = new JButton("Remover Selecionado");
        btnRemoverItem.setFont(new Font("Arial", Font.PLAIN, 11));
        painelLista.add(btnRemoverItem, BorderLayout.SOUTH);

        painelCentral.add(painelLista, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // --- 3. BOTÕES (RODAPÉ) ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnEmitir = new JButton("Emitir Receita");

        btnEmitir.setBackground(new Color(0, 102, 204));
        btnEmitir.setForeground(Color.WHITE);
        btnEmitir.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnEmitir);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES ---

        // Adicionar Remédio na Lista (Botão +)
        btnAdicionar.addActionListener(e -> adicionarRemedioNaLista());

        // Adicionar Remédio ao apertar ENTER no campo de texto
        txtNovoRemedio.addActionListener(e -> adicionarRemedioNaLista());

        // Remover Item da Lista
        btnRemoverItem.addActionListener(e -> {
            int index = listaVisualRemedios.getSelectedIndex();
            if (index != -1) {
                modeloLista.remove(index);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
        btnEmitir.addActionListener(e -> emitirReceita());
    }

    private void adicionarRemedioNaLista() {
        String texto = txtNovoRemedio.getText().trim();
        if (!texto.isEmpty()) {
            modeloLista.addElement(texto);
            txtNovoRemedio.setText(""); // Limpa campo
            txtNovoRemedio.requestFocus(); // Volta o foco para digitar o próximo
        }
    }

    private void emitirReceita() {
        MedicoController medicoController = new MedicoController();

        String uso = txtUsoIndicado.getText();

        // Validação
        if (uso.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o uso indicado (Doença/Diagnóstico).");
            return;
        }
        if (modeloLista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um medicamento à lista.");
            return;
        }

        // Converte o DefaultListModel do Swing para uma List<String> do Java padrão
        List<String> listaFinalRemedios = new ArrayList<>();
        for (int i = 0; i < modeloLista.size(); i++) {
            listaFinalRemedios.add(modeloLista.get(i));
        }

        try {
            // Envia a lista pronta para o Controller
            //medicoController.geraReceita(consulta, uso, listaFinalRemedios);

            JOptionPane.showMessageDialog(this, "Receita emitida com sucesso!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao emitir receita: " + e.getMessage());
        }
    }
}