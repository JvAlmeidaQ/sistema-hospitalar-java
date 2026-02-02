package br.ufjf.dcc025.view.MedicoView;

import br.ufjf.dcc025.controller.MedicoController;
import br.ufjf.dcc025.model.DiasDaSemana;
import br.ufjf.dcc025.model.HorarioAtendimento;
import br.ufjf.dcc025.model.Medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TelaHorariosTrabalho extends JFrame {

    private Medico medico;
    private MedicoController controller;
    private List<HorarioAtendimento> listaVisual;

    // Componentes
    private JComboBox<DiasDaSemana> cbDias;
    private JFormattedTextField txtInicio;
    private JFormattedTextField txtFim;
    private JTextField txtDuracao;
    private JTable tabelaHorarios;
    private DefaultTableModel modeloTabela;

    public TelaHorariosTrabalho(Medico medico) {
        this.medico = medico;
        this.controller = new MedicoController();
        this.listaVisual = new ArrayList<>(medico.getHorarioDeTrabalho());

        setTitle("Configurar Agenda de Atendimento");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTopo.setBackground(new Color(240, 248, 255));
        JLabel lblTitulo = new JLabel("Meus Turnos de Trabalho");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"Dia da Semana", "Início", "Fim", "Duração (min)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelaHorarios = new JTable(modeloTabela);
        tabelaHorarios.setRowHeight(25);
        tabelaHorarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabelaHorarios);
        scroll.setBorder(BorderFactory.createTitledBorder("Turnos Atuais"));

        add(scroll, BorderLayout.CENTER);

        JPanel containerSul = new JPanel(new BorderLayout());

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Adicionar Novo Turno"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("Dia:"), gbc);

        gbc.gridx = 1;
        cbDias = new JComboBox<>(DiasDaSemana.values());
        painelForm.add(cbDias, gbc);

        gbc.gridx = 2;
        painelForm.add(new JLabel("Início:"), gbc);

        gbc.gridx = 3;
        txtInicio = criarCampoHora();
        txtInicio.setColumns(5);
        painelForm.add(txtInicio, gbc);

        gbc.gridx = 4;
        painelForm.add(new JLabel("Fim:"), gbc);

        gbc.gridx = 5;
        txtFim = criarCampoHora();
        txtFim.setColumns(5);
        painelForm.add(txtFim, gbc);

        gbc.gridx = 6;
        painelForm.add(new JLabel("Duração (min):"), gbc);

        gbc.gridx = 7;
        txtDuracao = new JTextField("30", 3);
        painelForm.add(txtDuracao, gbc);


        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdicionar = new JButton("Adicionar Turno");
        JButton btnRemover = new JButton("Remover Selecionado");
        JButton btnLimpar = new JButton("Limpar Tudo");

        btnAdicionar.setBackground(new Color(0, 153, 76)); // Verde
        btnAdicionar.setForeground(Color.WHITE);

        btnLimpar.setBackground(new Color(204, 0, 0)); // Vermelho
        btnLimpar.setForeground(Color.WHITE);

        btnRemover.setBackground(new Color(255, 204, 0)); // Amarelo
        btnRemover.setForeground(Color.BLACK);

        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAdicionar);

        containerSul.add(painelForm, BorderLayout.CENTER);
        containerSul.add(painelBotoes, BorderLayout.SOUTH);

        add(containerSul, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarHorario());

        btnRemover.addActionListener(e -> {
            int linhaSelecionada = tabelaHorarios.getSelectedRow();

            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um horário na tabela para remover.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            HorarioAtendimento horarioParaRemover = this.listaVisual.get(linhaSelecionada);

            int confirmacao = JOptionPane.showConfirmDialog(this, "Remover este horário?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    controller.removerHorarioTrabalho(medico, horarioParaRemover);
                    carregarTabela();
                    JOptionPane.showMessageDialog(this, "Removido com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLimpar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza? Isso apagará TODOS os seus horários.",
                    "Limpar Agenda", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.limparHorarios(medico);
                    carregarTabela();
                    JOptionPane.showMessageDialog(this, "Agenda limpa com sucesso.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        carregarTabela();
    }

    private JFormattedTextField criarCampoHora() {
        try {
            MaskFormatter mask = new MaskFormatter("##:##");
            mask.setPlaceholderCharacter('_');
            return new JFormattedTextField(mask);
        } catch (Exception e) {
            return new JFormattedTextField();
        }
    }

    private void adicionarHorario() {
        try {
            DiasDaSemana dia = (DiasDaSemana) cbDias.getSelectedItem();
            String inicioStr = txtInicio.getText();
            String fimStr = txtFim.getText();
            String duracaoStr = txtDuracao.getText();

            if (inicioStr.contains("_") || fimStr.contains("_") || duracaoStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha os horários completos (HH:mm).", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalTime inicio = LocalTime.parse(inicioStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime fim = LocalTime.parse(fimStr, DateTimeFormatter.ofPattern("HH:mm"));
            int duracao = Integer.parseInt(duracaoStr);

            controller.adicionarHorarioTrabalho(medico, dia, inicio, fim, duracao);

            carregarTabela();
            JOptionPane.showMessageDialog(this, "Horário adicionado com sucesso!");

            txtInicio.setValue(null);
            txtFim.setValue(null);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duração deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, ex.getMessage(), "Não foi possível adicionar", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        this.listaVisual = new ArrayList<>(medico.getHorarioDeTrabalho());

        Collections.sort(this.listaVisual, Comparator.comparing(HorarioAtendimento::getDia)
                .thenComparing(HorarioAtendimento::getInicio));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        for (HorarioAtendimento h : this.listaVisual) {
            modeloTabela.addRow(new Object[]{
                    h.getDia(),
                    h.getInicio().format(fmt),
                    h.getFim().format(fmt),
                    h.getDuracaoAtendimento()
            });
        }
    }
}