package br.ufjf.dcc025.view.TelasConsulta;

import br.ufjf.dcc025.controller.AgendamentoController;
import br.ufjf.dcc025.controller.PacienteController;
import br.ufjf.dcc025.exceptions.ConsultaInvalidaException;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;
import br.ufjf.dcc025.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TelaAgendamento extends JFrame
{
        private Usuario usuarioLogado;


        private JComboBox<Paciente> cbPacientes;
        private JComboBox<String> cbEspecialidade;
        private JComboBox<Medico> cbMedicos;
        private JComboBox<LocalDate> cbDatas;
        private JComboBox<LocalTime> cbHorarios;

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


            JLabel lblTitulo = new JLabel("Nova Consulta", SwingConstants.CENTER);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
            lblTitulo.setForeground(new Color(0, 102, 204));
            lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            add(lblTitulo, BorderLayout.NORTH);


            JPanel painelForm = new JPanel(new GridBagLayout());
            painelForm.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 5, 10, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            // Paciente
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

            // Especialidade
            gbc.gridx = 0; gbc.gridy = 1;
            painelForm.add(criarLabel("Especialidade:"), gbc);

            gbc.gridx = 1;
            cbEspecialidade = new JComboBox<>();
            painelForm.add(cbEspecialidade, gbc);

            // 3. Médico
            gbc.gridx = 0; gbc.gridy = 2;
            painelForm.add(criarLabel("Médico:"), gbc);

            gbc.gridx = 1;
            cbMedicos = new JComboBox<>();
            painelForm.add(cbMedicos, gbc);

            // Data
            gbc.gridx = 0; gbc.gridy = 3;
            painelForm.add(criarLabel("Data Disponível:"), gbc);

            gbc.gridx = 1;
            cbDatas = new JComboBox<>();
            renderizarDatas();

            painelForm.add(cbDatas, gbc);

            //Horários Disponíveis
            gbc.gridx = 0; gbc.gridy = 4;
            painelForm.add(criarLabel("Horário:"), gbc);

            gbc.gridx = 1;
            cbHorarios = new JComboBox<>();
            painelForm.add(cbHorarios, gbc);

            add(painelForm, BorderLayout.CENTER);

            JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            JButton btnCancelar = new JButton("Cancelar");
            btnConfirmar = new JButton("Confirmar Agendamento");

            estilizarBotao(btnConfirmar, new Color(0, 153, 76));
            estilizarBotao(btnCancelar, new Color(204, 0, 0));

            painelBotoes.add(btnCancelar);
            painelBotoes.add(btnConfirmar);
            add(painelBotoes, BorderLayout.SOUTH);

            // --- EVENTOS
            cbEspecialidade.addActionListener(e -> filtrarMedicosPorEspecialidade());

            cbMedicos.addActionListener(e -> carregarDatasDisponiveis());

            cbDatas.addActionListener(e -> carregarHorariosDisponiveis());

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
            if (cbEspecialidade.getItemCount() > 0) {
                cbEspecialidade.setSelectedIndex(-1);
            }
        }

        private void filtrarMedicosPorEspecialidade() {
            cbMedicos.removeAllItems();
            cbDatas.removeAllItems();
            cbHorarios.removeAllItems();

            String especialidadeSelecionada = (String) cbEspecialidade.getSelectedItem();
            if (especialidadeSelecionada == null) return;

            List<Medico> medicosFiltrados = agendamentoController.buscarMedicosPorEspecialidade(especialidadeSelecionada);
            for (Medico m : medicosFiltrados) {
                cbMedicos.addItem(m);
            }
            cbMedicos.setSelectedIndex(-1);
        }

        private void carregarDatasDisponiveis() {
            cbDatas.removeAllItems();
            cbHorarios.removeAllItems();

            Medico medicoSelecionado = (Medico) cbMedicos.getSelectedItem();
            if (medicoSelecionado == null) return;

            List<LocalDate> dias = agendamentoController.listarDiasTrabalhadosProximos30Dias(medicoSelecionado);

            for (LocalDate data : dias) {
                cbDatas.addItem(data);
            }
            cbDatas.setSelectedIndex(-1);
        }

        private void renderizarDatas() {
            cbDatas.setRenderer(new DefaultListCellRenderer() {
                private final DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM");
                private final DateTimeFormatter fmtDia = DateTimeFormatter.ofPattern("EEE", new Locale("pt", "BR"));

                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                    if (value instanceof LocalDate) {
                        LocalDate data = (LocalDate) value;

                        String parteData = data.format(fmtData);

                        String parteDia = data.format(fmtDia).replace(".", "");

                        parteDia = parteDia.substring(0, 1).toUpperCase() + parteDia.substring(1);

                        setText(parteData + " (" + parteDia + ")");
                    }
                    return this;
                }
            });
        }

        private void carregarHorariosDisponiveis() {
            cbHorarios.removeAllItems();

            Medico medico = (Medico) cbMedicos.getSelectedItem();
            LocalDate data = (LocalDate) cbDatas.getSelectedItem();

            if (medico == null || data == null) return;

            List<LocalTime> horarios = agendamentoController.disponibilidadeDeHorarioConsultas(medico, data);

            for (LocalTime h : horarios) {
                cbHorarios.addItem(h);
            }
        }

        private void realizarAgendamento() {
            Paciente paciente = (Paciente) cbPacientes.getSelectedItem();
            Medico medico = (Medico) cbMedicos.getSelectedItem();
            LocalDate data = (LocalDate) cbDatas.getSelectedItem();
            LocalTime horario = (LocalTime) cbHorarios.getSelectedItem();

            if (paciente == null || medico == null || data == null || horario == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione todas as opções.");
                return;
            }

            try {
                agendamentoController.agendarConsulta(medico, paciente, data, horario);
                JOptionPane.showMessageDialog(this, "Consulta agendada com sucesso!");
                dispose();
            } catch (ConsultaInvalidaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception exception){
                JOptionPane.showMessageDialog(this, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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