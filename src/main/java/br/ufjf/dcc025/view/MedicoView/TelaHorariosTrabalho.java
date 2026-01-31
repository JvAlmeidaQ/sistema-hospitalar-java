

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
    private List<HorarioAtendimento> horariosAtendimento;

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
        this.horariosAtendimento = new ArrayList<>(medico.getHorarioDeTrabalho());

        setTitle("Configurar Agenda de Atendimento");
        setSize(700, 500);
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

        JScrollPane scroll = new JScrollPane(tabelaHorarios);
        scroll.setBorder(BorderFactory.createTitledBorder("Turnos Atuais"));
        add(scroll, BorderLayout.CENTER);

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
        painelForm.add(new JLabel("Início (HH:mm):"), gbc);

        gbc.gridx = 3;
        txtInicio = criarCampoHora();
        painelForm.add(txtInicio, gbc);

        gbc.gridx = 4;
        painelForm.add(new JLabel("Fim (HH:mm):"), gbc);

        gbc.gridx = 5;
        txtFim = criarCampoHora();
        painelForm.add(txtFim, gbc);

        gbc.gridx = 6;
        painelForm.add(new JLabel("Duração (min):"), gbc);

        gbc.gridx = 7;
        txtDuracao = new JTextField("30", 3);
        painelForm.add(txtDuracao, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdicionar = new JButton("Adicionar Turno");
        JButton btnRemover = new JButton("Remover Turno");
        JButton btnLimpar = new JButton("Limpar Toda Agenda");

        btnAdicionar.setBackground(new Color(0, 153, 76));
        btnAdicionar.setForeground(Color.WHITE);

        btnLimpar.setBackground(new Color(204, 0, 0));
        btnLimpar.setForeground(Color.WHITE);

        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnAdicionar);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 8;
        painelForm.add(painelBotoes, gbc);

        add(painelForm, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarHorario());

        btnRemover.addActionListener(e -> {
            int linhaSelecionada = tabelaHorarios.getSelectedRow();

            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um horário para remover.");
                return;
            }

            HorarioAtendimento horarioParaRemover = this.horariosAtendimento.get(linhaSelecionada);

            int confirmacao = JOptionPane.showConfirmDialog(this, "Remover este horário?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {

                    controller.removerHorarioTrabalho(medico, horarioParaRemover);

                    carregarTabela();
                    JOptionPane.showMessageDialog(this, "Removido com sucesso!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            }
        });

        btnLimpar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza? Isso apagará todos os seus horários de atendimento.",
                    "Limpar Agenda", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.limparHorarios(medico);
                    carregarTabela();
                    JOptionPane.showMessageDialog(this, "Agenda limpa com sucesso.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
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

            if (inicioStr.contains("_") || fimStr.contains("_") || duracaoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente.");
                return;
            }

            LocalTime inicio = LocalTime.parse(inicioStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime fim = LocalTime.parse(fimStr, DateTimeFormatter.ofPattern("HH:mm"));
            int duracao = Integer.parseInt(duracaoStr);


            controller.adicionarHorarioTrabalho(medico, dia, inicio, fim, duracao);

            carregarTabela();
            JOptionPane.showMessageDialog(this, "Horário adicionado com sucesso!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duração deve ser um número inteiro.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        Collections.sort(horariosAtendimento, Comparator.comparing(HorarioAtendimento::getDia)
                .thenComparing(HorarioAtendimento::getInicio));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        for (HorarioAtendimento h : horariosAtendimento) {
            modeloTabela.addRow(new Object[]{
                    h.getDia(),
                    h.getInicio().format(fmt),
                    h.getFim().format(fmt),
                    h.getDuracaoAtendimento()
            });
        }
    }
}