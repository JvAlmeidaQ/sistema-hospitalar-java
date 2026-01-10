//ERRO SETSENHA
//Não foi testado se realmente esta mudando os valores
package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.PacienteController;
import br.ufjf.dcc025.model.Endereco; // Essa Importação não deve exisitr
import br.ufjf.dcc025.model.Paciente; // Essa Importação não deve exisitr
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TelaEdicaoPaciente extends JFrame {

    private Paciente pacienteLogado;

    // Dados Pessoais
    private JTextField txtNome, txtCpf, txtEmail, txtTelefone, txtConvenio;
    private JPasswordField txtSenha;

    // Endereço
    private JTextField txtCep, txtEstado, txtCidade, txtBairro, txtRua, txtNumero, txtComplemento;

    public TelaEdicaoPaciente(Paciente paciente) {
        this.pacienteLogado = paciente;

        setTitle("Editar Perfil - Paciente");
        setSize(400, 550);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. DADOS PESSOAIS ---
        JPanel painelPessoal = new JPanel(new GridLayout(0, 1, 5, 5));
        painelPessoal.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204)),
                "Dados Pessoais",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(0, 102, 204)
        ));

        painelPessoal.add(criarLabel("Nome Completo:"));
        txtNome = new JTextField(paciente.getNome());
        painelPessoal.add(txtNome);

        painelPessoal.add(criarLabel("CPF (Não editável):"));
        txtCpf = new JTextField(paciente.getCpf());
        txtCpf.setEditable(false);
        txtCpf.setBackground(new Color(230, 230, 230));
        painelPessoal.add(txtCpf);

        painelPessoal.add(criarLabel("Convênio:"));
        txtConvenio = new JTextField(paciente.getConvenio());
        painelPessoal.add(txtConvenio);

        painelPessoal.add(criarLabel("Telefone:"));
        txtTelefone = new JTextField(paciente.getTelefone());
        painelPessoal.add(txtTelefone);

        painelPessoal.add(criarLabel("E-mail:"));
        txtEmail = new JTextField(paciente.getEmail());
        painelPessoal.add(txtEmail);

        painelPessoal.add(criarLabel("Senha:"));
        txtSenha = new JPasswordField(paciente.getSenha());
        painelPessoal.add(txtSenha);

        painelPrincipal.add(painelPessoal);
        painelPrincipal.add(Box.createVerticalStrut(20));

        // --- 2. ENDEREÇO ---
        JPanel painelEndereco = new JPanel(new GridLayout(0, 1, 5, 5));
        painelEndereco.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204)),
                "Endereço",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(0, 102, 204)
        ));

        // Recupera o objeto endereço atual para preencher os campos
        Endereco enderecoAtual = paciente.getEndereco();

        painelEndereco.add(criarLabel("CEP:"));
        txtCep = new JTextField(enderecoAtual.getCep());
        painelEndereco.add(txtCep);

        painelEndereco.add(criarLabel("Estado:"));
        txtEstado = new JTextField(enderecoAtual.getEstado());
        painelEndereco.add(txtEstado);

        painelEndereco.add(criarLabel("Cidade:"));
        txtCidade = new JTextField(enderecoAtual.getCidade());
        painelEndereco.add(txtCidade);

        painelEndereco.add(criarLabel("Bairro:"));
        txtBairro = new JTextField(enderecoAtual.getBairro());
        painelEndereco.add(txtBairro);

        painelEndereco.add(criarLabel("Rua:"));
        txtRua = new JTextField(enderecoAtual.getRua());
        painelEndereco.add(txtRua);

        painelEndereco.add(criarLabel("Número:"));
        txtNumero = new JTextField(enderecoAtual.getNumero());
        painelEndereco.add(txtNumero);

        painelEndereco.add(criarLabel("Complemento:"));
        txtComplemento = new JTextField(enderecoAtual.getComplemento());
        painelEndereco.add(txtComplemento);

        painelPrincipal.add(painelEndereco);

        // --- SCROLL PANE ---
        JScrollPane scrollPane = new JScrollPane(painelPrincipal);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

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
        String senhaAtual = showPasswordDialog(this, "Confirme sua senha atual para salvar as Alterações: ");
        if(senhaAtual == null)
            return;

        String nome = this.txtNome.getText();
        String email = this.txtEmail.getText();
        String telefone = this.txtTelefone.getText();
        String convenio = this.txtConvenio.getText();
        String novaSenha = new String(txtSenha.getPassword());

        String cep = this.txtCep.getText();
        String estado = this.txtEstado.getText();
        String cidade = this.txtCidade.getText();
        String bairro = this.txtBairro.getText();
        String rua = this.txtRua.getText();
        String numero = this.txtNumero.getText();
        String complemento = this.txtComplemento.getText();

        PacienteController pacienteController = new PacienteController();

        try {
            pacienteController.atualizarPaciente(pacienteLogado, senhaAtual, novaSenha, nome, email, telefone);

            pacienteController.atualizarEndereco(pacienteLogado, cep, estado, cidade, bairro, rua, numero, complemento);

            JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar:\n" + e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
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