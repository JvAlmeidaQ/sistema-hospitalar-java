package br.ufjf.dcc025.view;

import br.ufjf.dcc025.controller.Autenticar;
import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;
import br.ufjf.dcc025.model.Secretaria;
import br.ufjf.dcc025.model.Usuario;
import br.ufjf.dcc025.view.MedicoView.TelaPrincipalMedico;
import br.ufjf.dcc025.view.PacienteView.TelaPrincipalPaciente;
import br.ufjf.dcc025.view.SecretariaView.TelaPrincipalSecretaria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;

    public TelaLogin() {
        setTitle("Sistema da Clínica - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(6, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("Clínica Prisma Saúde", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 102, 204));

        JLabel lblEmail = new JLabel("E-mail:");
        this.txtEmail = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        this.txtSenha = new JPasswordField();

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(new Color(0, 102, 204));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 12));

        painel.add(lblTitulo);
        painel.add(lblEmail);
        painel.add(txtEmail);
        painel.add(lblSenha);
        painel.add(txtSenha);
        painel.add(btnEntrar);

        add(painel);

        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }

    private void realizarLogin() {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        Autenticar controller = new Autenticar();
        Usuario usuarioLogado = controller.login(email, senha);

        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuário ou senha inválidos!",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Bem vindo(a), " + usuarioLogado.getNome() + "!");

            if (usuarioLogado instanceof Medico) {
                new TelaPrincipalMedico((Medico) usuarioLogado).setVisible(true);
            } else if (usuarioLogado instanceof Paciente) {
                new TelaPrincipalPaciente((Paciente) usuarioLogado).setVisible(true);
            } else if (usuarioLogado instanceof Secretaria) {
                new TelaPrincipalSecretaria((Secretaria) usuarioLogado).setVisible(true);
            }
            this.dispose();
        }
    }
}