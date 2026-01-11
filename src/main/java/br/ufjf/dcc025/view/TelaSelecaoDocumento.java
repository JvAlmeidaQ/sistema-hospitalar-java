package br.ufjf.dcc025.view;

import br.ufjf.dcc025.model.Consulta;
import br.ufjf.dcc025.model.DocumentoMedico;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class TelaSelecaoDocumento extends JFrame {

    private Consulta consulta;
    private JList<DocumentoMedico> listaDocumentos;
    private DefaultListModel<DocumentoMedico> listModel;

    public TelaSelecaoDocumento(Consulta consulta) {
        this.consulta = consulta;

        setTitle("Documentos da Consulta");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // cabeçario
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTopo.setBackground(new Color(245, 245, 245));
        JLabel lblTitulo = new JLabel("Documentos Anexados");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0, 102, 204));
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaDocumentos = new JList<>(listModel);
        listaDocumentos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DocumentoMedico) {
                    DocumentoMedico doc = (DocumentoMedico) value;
                    setText(doc.getTipoRegistroClinico() + " - Emitido em: " + doc.getDataExpedicao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaDocumentos);
        add(scrollPane, BorderLayout.CENTER);

        //botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar");
        JButton btnVisualizar = new JButton("Visualizar Documento");

        btnVisualizar.setBackground(new Color(0, 102, 204));
        btnVisualizar.setForeground(Color.WHITE);

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnVisualizar);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarDocumentos();

        btnVoltar.addActionListener(e -> dispose());

        btnVisualizar.addActionListener(e -> {
            DocumentoMedico docSelecionado = listaDocumentos.getSelectedValue();
            if (docSelecionado != null) {
                exibirConteudoDocumento(docSelecionado);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um documento para visualizar.");
            }
        });
    }

    private void carregarDocumentos() {
        listModel.clear();
        java.util.List<DocumentoMedico> docs = consulta.getDocumentoMedico();
        if (docs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Esta consulta não possui documentos anexados.");
        } else {
            for (DocumentoMedico d : docs) {
                listModel.addElement(d);
            }
        }
    }

    private void exibirConteudoDocumento(DocumentoMedico doc) {
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setText(doc.imprimeDocumento()); //
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fonte monoespaçada para alinhar
        textArea.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scroll, "Visualização - " + doc.getTipoRegistroClinico(), JOptionPane.PLAIN_MESSAGE);
    }
}