//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.view.MedicoView;

import br.ufjf.dcc025.controller.PacienteController;
import br.ufjf.dcc025.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class TelaControleVisitas extends JFrame {

    private PacienteController controller;

    // Componentes
    private JTextField txtBusca;
    private JList<Paciente> listaPacientes;
    private DefaultListModel<Paciente> listModel;

    public TelaControleVisitas() {
        this.controller = new PacienteController();

        setTitle("Controle de Visitas - Hospital");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // cabeçario e busca
        JPanel painelTopo = new JPanel(new BorderLayout(5, 5));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        painelTopo.setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("Pacientes Internados");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel painelBusca = new JPanel(new BorderLayout(5, 0));
        painelBusca.setOpaque(false);
        painelBusca.add(new JLabel("Buscar Nome: "), BorderLayout.WEST);

        txtBusca = new JTextField();
        JButton btnBuscar = new JButton("Filtrar");

        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnBuscar, BorderLayout.EAST);

        painelTopo.add(lblTitulo, BorderLayout.NORTH);
        painelTopo.add(painelBusca, BorderLayout.SOUTH);

        add(painelTopo, BorderLayout.NORTH);

        //lista de pacientes
        listModel = new DefaultListModel<>();
        listaPacientes = new JList<>(listModel);
        listaPacientes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Paciente) {
                    Paciente p = (Paciente) value;
                    setText(p.getNome().toUpperCase());
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaPacientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Selecione um Paciente"));
        add(scrollPane, BorderLayout.CENTER);

        // Rodapé
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JButton btnVoltar = new JButton("Voltar");
        JButton btnVerificar = new JButton("Verificar Permissão");

        btnVerificar.setBackground(new Color(0, 153, 76)); // Verde
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.setFont(new Font("Arial", Font.BOLD, 12));
        btnVerificar.setPreferredSize(new Dimension(160, 35));

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnVerificar);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarListaInicial();

        btnVoltar.addActionListener(e -> dispose());
        btnBuscar.addActionListener(e -> filtrarLista());
        txtBusca.addActionListener(e -> filtrarLista());
        txtBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarLista();
            }
        });
        btnVerificar.addActionListener(e -> verificarVisita());
        listaPacientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    verificarVisita();
                }
            }
        });
    }

    private void carregarListaInicial() {
        listModel.clear();
        List<Paciente> internados = controller.listarPacientesInternados();

        if (internados.isEmpty()) {
            listModel.addElement(null);
            JOptionPane.showMessageDialog(this, "Não há pacientes internados no momento.");
        } else {
            for (Paciente p : internados) {
                listModel.addElement(p);
            }
        }
    }

    private void filtrarLista() {
        String termo = txtBusca.getText().trim();
        listModel.clear();

        List<Paciente> resultado;
        if (termo.isEmpty()) {
            resultado = controller.listarPacientesInternados();
        } else {
            resultado = controller.buscarInternadosPorNome(termo);
        }

        for (Paciente p : resultado) {
            listModel.addElement(p);
        }
    }

    private void verificarVisita() {
        Paciente selecionado = listaPacientes.getSelectedValue();

        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente na lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean podeReceber = selecionado.getPodeReceberVisitas() != null && selecionado.getPodeReceberVisitas();

        if (podeReceber) {
            JLabel label = new JLabel("VISITA LIBERADA");
            label.setFont(new Font("Arial", Font.BOLD, 18));
            label.setForeground(new Color(0, 102, 0));

            JOptionPane.showMessageDialog(this,
                    label,
                    "Status de Visita - " + selecionado.getNome(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JLabel label = new JLabel("VISITA NÃO PERMITIDA");
            label.setFont(new Font("Arial", Font.BOLD, 18));
            label.setForeground(Color.RED);

            JOptionPane.showMessageDialog(this,
                    label,
                    "Status de Visita - " + selecionado.getNome(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}