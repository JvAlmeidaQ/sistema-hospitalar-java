package br.ufjf.dcc025.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame {

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
        JTextField txtEmail = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        JPasswordField txtSenha = new JPasswordField();

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
                String emailCapturado = txtEmail.getText();
                String senhaCapturada = new String(txtSenha.getPassword());

                // Apenas um teste visual por enquanto
                JOptionPane.showMessageDialog(null,
                        "Tentando entrar...\nEmail: " + emailCapturado);
            }
        });
    }

    // TIRAR!!!
    // Método main para testar a tela agora mesmo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}