//ERRO SETSENHA
//Não foi testado se realmente esta mudando os valores
package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.SecretariaController;
import br.ufjf.dcc025.model.Secretaria; // Essa Importação não deve exisitr
import javax.swing.*;
import java.awt.*;

public class TelaEdicaoSecretaria extends JFrame {

    private Secretaria secretariaLogada; // Objeto que será editado
    private JTextField txtNome, txtEmail, txtCpf;
    private JPasswordField txtSenha;

    // Construtor recebe a secretária que está logada/sendo editada
    public TelaEdicaoSecretaria(Secretaria secretaria) {
        this.secretariaLogada = secretaria;

        setTitle("Editar Perfil - Secretária");
        setSize(400, 550);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só essa janela
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. TÍTULO ---
        JLabel lblTitulo = new JLabel("Editar Meus Dados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // --- 2. FORMULÁRIO ---
        JPanel painelForm = new JPanel(new GridLayout(4, 1, 5, 5));
        painelForm.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        // Nome
        painelForm.add(criarLabel("Nome Completo:"));
        txtNome = new JTextField(secretaria.getNome()); // Já inicia preenchido
        painelForm.add(txtNome);

        // Email
        painelForm.add(criarLabel("E-mail:"));
        txtEmail = new JTextField(secretaria.getEmail());
        painelForm.add(txtEmail);

        // Senha
        painelForm.add(criarLabel("Nova Senha (Opcional):"));
        txtSenha = new JPasswordField();
        painelForm.add(txtSenha);

        // CPF (Apenas visualização - não editável)
        painelForm.add(criarLabel("CPF (Não editável):"));
        txtCpf = new JTextField(secretaria.getCpf());
        txtCpf.setEditable(false); // Bloqueia edição
        txtCpf.setBackground(new Color(230, 230, 230)); // Cor cinza para indicar inativo
        painelForm.add(txtCpf);

        add(painelForm, BorderLayout.CENTER);

        // --- 3. BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnSalvar = new JButton("Salvar Alterações");

        // Estilizando botão Salvar
        btnSalvar.setBackground(new Color(0, 102, 204));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES ---
        btnCancelar.addActionListener(e -> dispose()); // Fecha a janela

        btnSalvar.addActionListener(e -> salvarAlteracoes());
    }
    private void salvarAlteracoes() {

        String novaSenha = new String(txtSenha.getPassword());

        String senhaAtual = null;

        if (!novaSenha.isBlank()) {
            senhaAtual = showPasswordDialog(this,
                    "Digite sua senha atual para confirmar a alteração:");



            if (senhaAtual == null || senhaAtual.isBlank()) {
                JOptionPane.showMessageDialog(this, "Alteração Cancelada! A senha atual é Obrigatoria.",
                        "Segurança", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        // 2. Coleta dados
        String novoNome = txtNome.getText();
        String novoEmail = txtEmail.getText();


        SecretariaController controller = new SecretariaController();

        try {
            controller.atualizarSecretaria(secretariaLogada, senhaAtual, novaSenha, novoNome, novoEmail);

            JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            dispose(); // Fecha

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao Salvar", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static String showPasswordDialog(Component pai, String mensagem) {
        JPasswordField txtSenha = new JPasswordField();
        Object[] message = {mensagem, txtSenha};
        int option = JOptionPane.showConfirmDialog(pai, message, "Segurança", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return new String(txtSenha.getPassword());
        }
        return null;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
}