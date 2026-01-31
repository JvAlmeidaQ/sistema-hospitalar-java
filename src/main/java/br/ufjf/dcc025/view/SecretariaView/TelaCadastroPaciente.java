

package br.ufjf.dcc025.view.SecretariaView;

import br.ufjf.dcc025.controller.PacienteController;
import br.ufjf.dcc025.exceptions.DadosInvalidosException;
import br.ufjf.dcc025.view.TelaLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCadastroPaciente extends JFrame {

    private JTextField txtNome, txtCpf, txtConvenio, txtTelefone, txtEmail;
    private JPasswordField txtSenha;
    private JTextField txtCep, txtEstado, txtCidade, txtBairro, txtRua, txtComplemento, txtNumero;

    public TelaCadastroPaciente() {
        setTitle("Sistema da Clínica - Cadastro");
        setSize(400, 650);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // Layout principal para fixar título e botões

        // título
        JLabel lblTituloPrincipal = new JLabel("Cadastro de Paciente", SwingConstants.CENTER);
        lblTituloPrincipal.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloPrincipal.setForeground(new Color(0, 102, 204));
        lblTituloPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Espaçamento
        add(lblTituloPrincipal, BorderLayout.NORTH);

        // Formulário
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridLayout(0, 2, 10, 10)); // 2 colunas
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        adicionarSubTitulo(painelFormulario, "DADOS PESSOAIS");

        painelFormulario.add(new JLabel("Nome Completo:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelFormulario.add(txtCpf);

        painelFormulario.add(new JLabel("Convênio:"));
        txtConvenio = new JTextField();
        painelFormulario.add(txtConvenio);

        painelFormulario.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelFormulario.add(txtTelefone);

        painelFormulario.add(new JLabel("E-mail:"));
        txtEmail = new JTextField();
        painelFormulario.add(txtEmail);

        painelFormulario.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelFormulario.add(txtSenha);

        painelFormulario.add(new JLabel(""));
        painelFormulario.add(new JLabel(""));
        adicionarSubTitulo(painelFormulario, "ENDEREÇO");

        painelFormulario.add(new JLabel("CEP:"));
        txtCep = new JTextField();
        painelFormulario.add(txtCep);

        painelFormulario.add(new JLabel("Estado (UF):"));
        txtEstado = new JTextField();
        painelFormulario.add(txtEstado);

        painelFormulario.add(new JLabel("Cidade:"));
        txtCidade = new JTextField();
        painelFormulario.add(txtCidade);

        painelFormulario.add(new JLabel("Bairro:"));
        txtBairro = new JTextField();
        painelFormulario.add(txtBairro);

        painelFormulario.add(new JLabel("Rua:"));
        txtRua = new JTextField();
        painelFormulario.add(txtRua);

        painelFormulario.add(new JLabel("Número:"));
        txtNumero = new JTextField();
        painelFormulario.add(txtNumero);

        painelFormulario.add(new JLabel("Complemento:"));
        txtComplemento = new JTextField();
        painelFormulario.add(txtComplemento);

        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(null); // Remove borda extra
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnVoltar = new JButton("VOLTAR");
        JButton btnSalvar = new JButton("SALVAR CADASTRO");

        btnSalvar.setBackground(new Color(0, 102, 204));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 12));

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnVoltar.addActionListener(e -> {
            dispose();
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarPaciente();
            }
        });
    }

    private void adicionarSubTitulo(JPanel painel, String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setForeground(Color.GRAY);
        painel.add(titulo);
        painel.add(new JLabel(""));
    }

    private void salvarPaciente() {

        if (txtNome.getText().trim().isEmpty() ||
                txtCpf.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() ||
                new String(txtSenha.getPassword()).trim().isEmpty() ||
                txtCep.getText().trim().isEmpty() ||
                txtRua.getText().trim().isEmpty() ||
                txtNumero.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha todos os campos obrigatórios.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());
        String telefone = txtTelefone.getText();
        String convenio = txtConvenio.getText();

        String cep = txtCep.getText();
        String rua = txtRua.getText();
        String numero = txtNumero.getText();
        String complemento = txtComplemento.getText();
        String bairro = txtBairro.getText();
        String cidade = txtCidade.getText();
        String estado = txtEstado.getText();

        PacienteController controller = new PacienteController();

        try {
            controller.cadastrarPaciente(
                    nome, cpf, email, senha, telefone, convenio,
                    cep, rua, numero, complemento, bairro, cidade, estado
            );

            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
            limparCampos();

        } catch (DadosInvalidosException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro: " + e,
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtConvenio.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtSenha.setText("");

        txtCep.setText("");
        txtEstado.setText("");
        txtCidade.setText("");
        txtBairro.setText("");
        txtRua.setText("");
        txtNumero.setText("");
        txtComplemento.setText("");
    }
}