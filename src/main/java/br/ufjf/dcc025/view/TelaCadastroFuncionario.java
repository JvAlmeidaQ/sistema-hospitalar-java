package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.FuncionarioController;
import br.ufjf.dcc025.model.DadosHospital; // Essa Importação não deve exisitr
import br.ufjf.dcc025.model.Medico; // Essa Importação não deve exisitr
import br.ufjf.dcc025.model.Secretaria; // Essa Importação não deve exisitr

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCadastroFuncionario extends JFrame {

    // Componentes globais
    private JTextField txtNome, txtCpf, txtEmail, txtEspecialidade;
    private JPasswordField txtSenha;
    private JLabel lblEspecialidade;
    private JCheckBox chkMedico, chkSecretaria;
    private ButtonGroup grupoTipoFuncionario;

    public TelaCadastroFuncionario() {
        setTitle("Sistema da Clínica - Cadastro de Funcionário");
        setSize(400, 650);
        //setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. PAINEL SUPERIOR (Título + Checkboxes) ---
        JPanel painelSuperior = new JPanel(new GridLayout(2, 1));

        // Título
        JLabel lblTitulo = new JLabel("Cadastro de Funcionário", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        painelSuperior.add(lblTitulo);

        // Checkboxes (Tipo de Funcionário)
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        chkMedico = new JCheckBox("Médico");
        chkSecretaria = new JCheckBox("Secretária");

        // Agrupando para garantir seleção única
        grupoTipoFuncionario = new ButtonGroup();
        grupoTipoFuncionario.add(chkMedico);
        grupoTipoFuncionario.add(chkSecretaria);

        painelTipo.add(chkMedico);
        painelTipo.add(chkSecretaria);
        painelSuperior.add(painelTipo);

        add(painelSuperior, BorderLayout.NORTH);

        // --- 2. FORMULÁRIO ---
        JPanel painelFormulario = new JPanel();
        // GridLayout com 0 linhas (indefinido) e 1 coluna, para adaptar dinamicamente
        painelFormulario.setLayout(new GridLayout(0, 1, 5, 5));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Nome
        painelFormulario.add(criarLabel("Nome Completo:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);

        // CPF
        painelFormulario.add(criarLabel("CPF:"));
        txtCpf = new JTextField();
        painelFormulario.add(txtCpf);

        // Email
        painelFormulario.add(criarLabel("E-mail:"));
        txtEmail = new JTextField();
        painelFormulario.add(txtEmail);

        // Senha
        painelFormulario.add(criarLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelFormulario.add(txtSenha);

        // Especialidade (Inicialmente Oculto)
        lblEspecialidade = criarLabel("Especialidade:");
        txtEspecialidade = new JTextField();

        // Adiciona ao painel
        painelFormulario.add(lblEspecialidade);
        painelFormulario.add(txtEspecialidade);

        // Define invisível inicialmente
        lblEspecialidade.setVisible(false);
        txtEspecialidade.setVisible(false);

        add(painelFormulario, BorderLayout.CENTER);

        // --- 3. BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton btnVoltar = new JButton("VOLTAR");
        JButton btnSalvar = new JButton("SALVAR");

        // Estilo do botão Salvar
        btnSalvar.setBackground(new Color(0, 102, 204));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES / LISTENERS ---

        // Lógica para mostrar/esconder Especialidade
        ActionListener acaoCheckbox = e -> {
            boolean isMedico = chkMedico.isSelected();
            lblEspecialidade.setVisible(isMedico);
            txtEspecialidade.setVisible(isMedico);

            // Se ocultar, limpa o texto para evitar salvar lixo
            if (!isMedico) {
                txtEspecialidade.setText("");
            }

            // Revalida o painel para ajustar o layout visualmente
            painelFormulario.revalidate();
            painelFormulario.repaint();
        };

        chkMedico.addActionListener(acaoCheckbox);
        chkSecretaria.addActionListener(acaoCheckbox);

        btnVoltar.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose();
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarFuncionario();
            }
        });
    }

    // Método auxiliar para criar labels padronizadas
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private void salvarFuncionario() {
        // 1. Coleta dados da tela
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());
        String especialidade = txtEspecialidade.getText();
        boolean isMedico = chkMedico.isSelected();
        boolean isSecretaria = chkSecretaria.isSelected();

        if (!isMedico && !isSecretaria) {
            JOptionPane.showMessageDialog(this, "Selecione o tipo de funcionário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FuncionarioController controller = new FuncionarioController();

        try {
            controller.cadastrarFuncionario(nome, cpf, email, senha, isMedico, especialidade);

            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            // Mostra o erro exato (ex: "CPF já existe")
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
        txtEspecialidade.setText("");
        grupoTipoFuncionario.clearSelection();

        lblEspecialidade.setVisible(false);
        txtEspecialidade.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCadastroFuncionario().setVisible(true));
    }
}