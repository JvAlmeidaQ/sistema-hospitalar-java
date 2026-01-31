

package br.ufjf.dcc025.view.TelasConsulta;

import br.ufjf.dcc025.controller.AgendamentoController;
import br.ufjf.dcc025.controller.PacienteController;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;
import br.ufjf.dcc025.model.Usuario;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaAgendamento extends JFrame {

    private Usuario usuarioLogado;

    private JComboBox<Paciente> cbPacientes;
    private JComboBox<String> cbEspecialidade;
    private JComboBox<Medico> cbMedicos;
    private JFormattedTextField txtData;
    private JComboBox<String> cbHorarios;
    private JButton btnBuscarHorarios;
    private JButton btnConfirmar;

    private final AgendamentoController agendamentoController;

    public TelaAgendamento(Usuario usuario) {
        this.usuarioLogado = usuario;
        this.agendamentoController = new AgendamentoController();

        setTitle("Agendamento de Consulta");
        setSize(500, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //titulo
        JLabel lblTitulo = new JLabel("Nova Consulta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        //formulario
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        //paciente
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(criarLabel("Paciente:"), gbc);

        gbc.gridx = 1;
        cbPacientes = new JComboBox<>();
        carregarPacientes();
        painelForm.add(cbPacientes, gbc);

        if (usuarioLogado instanceof Paciente) {
            cbPacientes.setSelectedItem((Paciente) usuarioLogado);
            cbPacientes.setEnabled(false);
        }

        //especialidade
        gbc.gridx = 0; gbc.gridy = 1;
        painelForm.add(criarLabel("Especialidade:"), gbc);

        gbc.gridx = 1;
        cbEspecialidade = new JComboBox<>();
        cbEspecialidade.addActionListener(e -> filtrarMedicosPorEspecialidade());
        painelForm.add(cbEspecialidade, gbc);

        //medico
        gbc.gridx = 0; gbc.gridy = 2;
        painelForm.add(criarLabel("Médico:"), gbc);

        gbc.gridx = 1;
        cbMedicos = new JComboBox<>();
        painelForm.add(cbMedicos, gbc);

        //date
        gbc.gridx = 0; gbc.gridy = 3;
        painelForm.add(criarLabel("Data (dd/MM/yyyy):"), gbc);

        gbc.gridx = 1;
        JPanel painelData = new JPanel(new BorderLayout(5, 0));
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(dateMask);
        } catch (Exception e) {
            txtData = new JFormattedTextField();
        }
        painelData.add(txtData, BorderLayout.CENTER);

        btnBuscarHorarios = new JButton("Verificar");
        btnBuscarHorarios.setFont(new Font("Arial", Font.BOLD, 10));
        painelData.add(btnBuscarHorarios, BorderLayout.EAST);

        painelForm.add(painelData, gbc);

        //horario disponivel
        gbc.gridx = 0; gbc.gridy = 4;
        painelForm.add(criarLabel("Horários Disponíveis:"), gbc);

        gbc.gridx = 1;
        cbHorarios = new JComboBox<>();
        cbHorarios.setEnabled(false); // Começa travado até verificar
        painelForm.add(cbHorarios, gbc);

        add(painelForm, BorderLayout.CENTER);

        //botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnCancelar = new JButton("Cancelar");
        btnConfirmar = new JButton("Confirmar Agendamento");

        estilizarBotao(btnConfirmar, new Color(0, 153, 76));
        estilizarBotao(btnCancelar, new Color(204, 0, 0));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnConfirmar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnBuscarHorarios.addActionListener(e -> buscarHorariosLivres());
        btnConfirmar.addActionListener(e -> realizarAgendamento());
        btnCancelar.addActionListener(e -> dispose());

        carregarEspecialidades();
    }

    private void carregarEspecialidades() {
        cbEspecialidade.removeAllItems();
        List<String> especialidades = agendamentoController.listarEspecialidadesDisponiveis();
        for (String esp : especialidades) {
            cbEspecialidade.addItem(esp);
        }

        if(cbEspecialidade.getItemCount() > 0) {
            cbEspecialidade.setSelectedIndex(0);
        }
    }

    private void filtrarMedicosPorEspecialidade() {
        cbMedicos.removeAllItems();
        String especialidadeSelecionada = (String) cbEspecialidade.getSelectedItem();
        if (especialidadeSelecionada == null)
            return;
        List<Medico> medicosFiltrados = agendamentoController.buscarMedicosPorEspecialidade(especialidadeSelecionada);
        for (Medico m : medicosFiltrados) {
            cbMedicos.addItem(m);
        }
    }

    private void buscarHorariosLivres() {
        Medico medico = (Medico) cbMedicos.getSelectedItem();
        String dataStr = txtData.getText();

        if(medico == null || dataStr.contains("_")) {
            JOptionPane.showMessageDialog(this, "Selecione um médico e informe a data completa.");
            return;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataStr, formatter);
            if(data.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Não é possível agendar em datas passadas.");
                return;
            }
            List<LocalTime> horarios = agendamentoController.disponibilidadeDeHorarioConsultas(medico, data);

            cbHorarios.removeAllItems();

            if (horarios.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Agenda cheia ou médico não atende neste dia/horário.");
                cbHorarios.setEnabled(false);
            } else {
                for(LocalTime horario : horarios) {
                    cbHorarios.addItem(horario.toString());
                }
                cbHorarios.setEnabled(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Data inválida ou erro ao buscar: " + ex.getMessage());
        }
    }

    private void realizarAgendamento() {
        Paciente paciente = (Paciente) cbPacientes.getSelectedItem();
        Medico medico = (Medico) cbMedicos.getSelectedItem();
        if (paciente == null || medico == null || !cbHorarios.isEnabled() || cbHorarios.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os dados e verifique a disponibilidade.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(txtData.getText(), formatter);
            LocalTime horario = LocalTime.parse((String) cbHorarios.getSelectedItem());
            agendamentoController.agendarConsulta(medico, paciente, data, horario);
            JOptionPane.showMessageDialog(this, "Consulta agendada com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao agendar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarPacientes() {
        cbPacientes.removeAllItems();

        PacienteController pacienteController = new PacienteController();

        for (Paciente p : pacienteController.listarTodosPacientes()) {
            cbPacientes.addItem(p);
        }
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
    }
}