package br.ufjf.dcc025.view;

import br.ufjf.dcc025.model.DadosHospital;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Secretaria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCadastroFuncionario extends JFrame {

    // Componentes globais
    private JTextField txtNome, txtCpf, txtEmail;
    private JPasswordField txtSenha;
    private JCheckBox chkMedico, chkSecretaria;
    private ButtonGroup grupoTipoFuncionario;

    public TelaCadastroFuncionario() {
        setTitle("Sistema da Clínica - Cadastro de Funcionário");
        setSize(450, 450); // Tamanho ajustado
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Título Principal
        JLabel lblTitulo = new JLabel("Cadastro de Funcionário", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 2. Formulário
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridLayout(6, 1, 5, 5)); // 6 linhas (Nome, CPF, Email, Senha, Checkboxes, Espaço)
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

        // --- ÁREA DE SELEÇÃO (CHECKBOXES) ---
        // Criamos um painel pequeno só para alinhar as checkboxes lado a lado
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        chkMedico = new JCheckBox("Médico");
        chkSecretaria = new JCheckBox("Secretária");

        // O ButtonGroup garante que apenas um seja selecionado por vez
        grupoTipoFuncionario = new ButtonGroup();
        grupoTipoFuncionario.add(chkMedico);
        grupoTipoFuncionario.add(chkSecretaria);

        painelTipo.add(new JLabel("Tipo de Usuário: "));
        painelTipo.add(chkMedico);
        painelTipo.add(chkSecretaria);

        painelFormulario.add(painelTipo);

        add(painelFormulario, BorderLayout.CENTER);

        // 3. Botões
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

        // --- AÇÕES ---
        btnVoltar.addActionListener(e -> {
            new TelaLogin().setVisible(true); // Ou voltar para um menu de admin, se houver
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
        try {
            String nome = txtNome.getText();
            String cpf = txtCpf.getText();
            String email = txtEmail.getText();
            String senha = new String(txtSenha.getPassword());

            if (!chkMedico.isSelected() && !chkSecretaria.isSelected()) {
                JOptionPane.showMessageDialog(this, "Selecione o tipo: Médico ou Secretária.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (chkMedico.isSelected()) {
                // Cria Médico
                Medico novoMedico = new Medico(nome, email, senha, cpf);
                DadosHospital.medicos.add(novoMedico);
                JOptionPane.showMessageDialog(this, "Médico cadastrado com sucesso!");
            } else {
                // Cria Secretária
                Secretaria novaSecretaria = new Secretaria(nome, email, senha, cpf);
                DadosHospital.secretarias.add(novaSecretaria);
                JOptionPane.showMessageDialog(this, "Secretária cadastrada com sucesso!");
            }

            // Volta para o Login (ou limpa a tela para cadastrar outro)
            new TelaLogin().setVisible(true);
            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCadastroFuncionario().setVisible(true));
    }
}