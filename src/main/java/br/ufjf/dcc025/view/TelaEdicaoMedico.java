//ERRO SETSENHA
//Não foi testado se realmente esta mudando os valores
package br.ufjf.dcc025.view;

import br.ufjf.dcc025.model.Medico;
import javax.swing.*;
import java.awt.*;

public class TelaEdicaoMedico extends JFrame {

    private Medico medicoLogado; // Objeto Médico que será editado
    private JTextField txtNome, txtEmail, txtCpf, txtEspecialidade;
    private JPasswordField txtSenha;

    // Construtor recebe o médico que está logado
    public TelaEdicaoMedico(Medico medico) {
        this.medicoLogado = medico;

        setTitle("Editar Perfil - Médico");
        setSize(400, 550); // Um pouco maior por ter mais um campo
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. TÍTULO ---
        JLabel lblTitulo = new JLabel("Editar Dados do Médico", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // --- 2. FORMULÁRIO ---
        JPanel painelForm = new JPanel(new GridLayout(5, 1, 5, 5)); // 5 linhas agora
        painelForm.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        // Nome
        painelForm.add(criarLabel("Nome Completo:"));
        txtNome = new JTextField(medico.getNome());
        painelForm.add(txtNome);

        // Especialidade (Campo Extra)
        painelForm.add(criarLabel("Especialidade:"));
        // Atenção: Certifique-se que sua classe Medico tem o método getEspecialidade()
        txtEspecialidade = new JTextField(medico.getEspecialidade());
        painelForm.add(txtEspecialidade);

        // Email
        painelForm.add(criarLabel("E-mail:"));
        txtEmail = new JTextField(medico.getEmail());
        painelForm.add(txtEmail);

        // Senha
        painelForm.add(criarLabel("Nova Senha:"));
        txtSenha = new JPasswordField(medico.getSenha());
        painelForm.add(txtSenha);

        // CPF (Bloqueado)
        painelForm.add(criarLabel("CPF (Não editável):"));
        txtCpf = new JTextField(medico.getCpf());
        txtCpf.setEditable(false);
        txtCpf.setBackground(new Color(230, 230, 230));
        painelForm.add(txtCpf);

        add(painelForm, BorderLayout.CENTER);

        // --- 3. BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnSalvar = new JButton("Salvar Alterações");

        btnSalvar.setBackground(new Color(0, 102, 204));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES ---
        btnCancelar.addActionListener(e -> dispose());

        btnSalvar.addActionListener(e -> salvarAlteracoes());
    }

    private void salvarAlteracoes() {
        String novoNome = txtNome.getText();
        String novaEspecialidade = txtEspecialidade.getText();
        String novoEmail = txtEmail.getText();
        String novaSenha = new String(txtSenha.getPassword());

        // Validação
        if (novoNome.trim().isEmpty() || novaEspecialidade.trim().isEmpty() ||
                novoEmail.trim().isEmpty() || novaSenha.trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Todos os campos (incluindo Especialidade) são obrigatórios!",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Atualiza o objeto original
        medicoLogado.setNome(novoNome);
        medicoLogado.setEmail(novoEmail);
        //SETSENHA NÃO FUNCIONA !!!
        //medicoLogado.setSenha(novaSenha);

        // Importante: Você precisa ter criado o setEspecialidade na classe Medico
        medicoLogado.setEspecialidade(novaEspecialidade);

        JOptionPane.showMessageDialog(this, "Dados do médico atualizados com sucesso!");
        dispose();
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
}