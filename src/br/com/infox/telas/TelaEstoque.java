/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;


public class TelaEstoque extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCliente
     */
    public TelaEstoque() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisar_produto();
    }

        private void adicionarProd() {
        String sql = "insert into tbproduto(descricao,tipo,valor) values(?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txt_desc_prod.getText());
            pst.setString(2, cbx_tipo_prod.getSelectedItem().toString());
            pst.setString(3, txt_valor_prod.getText());

            //validação dos campos obrigatórios   
            if ((txt_desc_prod.getText().isEmpty()) || (cbx_tipo_prod.getSelectedItem().toString().isEmpty()) || (txt_valor_prod.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                //a linha abaixo atualiza a tabela usuario com os dados do formulário
                //a estrutura abaixo é usada para confirmar a inserção dos dados na tabela
                int adicionado = pst.executeUpdate();
                //a linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso");
                    limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //método para pesquisar Produtos pelo nome com filtro
    private void pesquisar_produto() {
        
        String sql = "select idproduto, tipo, descricao, valor  from tbproduto where descricao like ?";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" - continuação da String sql
            pst.setString(1, txt_pesq_prod.getText() + "%");
          //  pst.setString(2, cbx_tipo_prod.getSelectedItem().toString()); 'and tipo= ?'
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblProdutos.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        int setar = tblProdutos.getSelectedRow();
       
        txt_id_prod.setText(tblProdutos.getModel().getValueAt(setar, 0).toString());
        cbx_tipo_prod.setSelectedItem(tblProdutos.getModel().getValueAt(setar, 1).toString());
        txt_desc_prod.setText(tblProdutos.getModel().getValueAt(setar, 2).toString());
        txt_valor_prod.setText(tblProdutos.getModel().getValueAt(setar, 3).toString());
        //a linha abaixo desabilita o botão adicionar
        btnAdicionar.setEnabled(false);
        
    }

    // método para alterar dados do cliente
    private void alterar_produto() {
        String sql = "update tbproduto set descricao=?,tipo=?,valor=? where idproduto=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txt_desc_prod.getText());
            pst.setString(2, cbx_tipo_prod.getSelectedItem().toString());
            pst.setString(3, txt_valor_prod.getText());
            pst.setString(4, txt_id_prod.getText());

            if ((txt_desc_prod.getText().isEmpty()) || (cbx_tipo_prod.getSelectedItem().toString().isEmpty()) || (txt_valor_prod.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                //a linha abaixo atualiza a tabela cliente com os dados do formulário
                //a etrutura abaixo é usada para confirmar a alteração dos dados na tabela
                int adicionado = pst.executeUpdate();
                //a linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do produto alterados com sucesso");
                    limpar();
                    btnAdicionar.setEnabled(true);
                    
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método responsável pela remoção de clientes
    private void remover_produto() {
        //a estrutura abaixo confirma a remoção do cliente
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este cliente ?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbproduto where idproduto=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txt_id_prod.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente Removido com sucesso");
                    limpar();
                    btnAdicionar.setEnabled(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }

    // metodo de limpeza de campos do formulario
    private void limpar() {
        txt_id_prod.setText(null);
        txt_desc_prod.setText(null);
        txt_valor_prod.setText(null);
        cbx_tipo_prod.setSelectedItem(null);
        txt_pesq_prod.setText(null);
        ((DefaultTableModel) tblProdutos.getModel()).setRowCount(0);

    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_pesq_prod = new javax.swing.JTextField();
        cbx_tipo_prod = new javax.swing.JComboBox<>();
        txt_desc_prod = new javax.swing.JTextField();
        txt_valor_prod = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblProdutos = new javax.swing.JTable();
        txt_id_prod = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Produtos");
        setPreferredSize(new java.awt.Dimension(640, 480));
        getContentPane().setLayout(null);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(456, 230, 153, 150);

        txt_pesq_prod.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        txt_pesq_prod.setBorder(null);
        txt_pesq_prod.setOpaque(false);
        txt_pesq_prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pesq_prodActionPerformed(evt);
            }
        });
        txt_pesq_prod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_pesq_prodKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_pesq_prodKeyReleased(evt);
            }
        });
        getContentPane().add(txt_pesq_prod);
        txt_pesq_prod.setBounds(60, 52, 266, 17);

        cbx_tipo_prod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Indefinido", "Mouse", "Teclado", "Processador", "Placa de Video", "Placa Mae" }));
        getContentPane().add(cbx_tipo_prod);
        cbx_tipo_prod.setBounds(160, 340, 120, 20);

        txt_desc_prod.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        txt_desc_prod.setBorder(null);
        txt_desc_prod.setOpaque(false);
        getContentPane().add(txt_desc_prod);
        txt_desc_prod.setBounds(162, 282, 230, 17);

        txt_valor_prod.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        txt_valor_prod.setBorder(null);
        txt_valor_prod.setOpaque(false);
        getContentPane().add(txt_valor_prod);
        txt_valor_prod.setBounds(162, 310, 110, 17);

        btnAdicionar.setBackground(new java.awt.Color(34, 33, 33));
        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/add (3).png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdicionar);
        btnAdicionar.setBounds(40, 370, 80, 70);

        btnAlterar.setBackground(new java.awt.Color(34, 33, 33));
        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pencil.png"))); // NOI18N
        btnAlterar.setToolTipText("Alterar");
        btnAlterar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(34, 33, 33)));
        btnAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAlterar);
        btnAlterar.setBounds(200, 370, 80, 70);

        btnRemover.setBackground(new java.awt.Color(34, 33, 33));
        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/cancel.png"))); // NOI18N
        btnRemover.setToolTipText("Remover");
        btnRemover.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(34, 33, 33)));
        btnRemover.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRemover.setPreferredSize(new java.awt.Dimension(80, 80));
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });
        getContentPane().add(btnRemover);
        btnRemover.setBounds(350, 370, 80, 70);

        jScrollPane4.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tblProdutos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tblProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Tipo", "Descrição", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProdutosMouseClicked(evt);
            }
        });
        tblProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblProdutosKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tblProdutos);
        if (tblProdutos.getColumnModel().getColumnCount() > 0) {
            tblProdutos.getColumnModel().getColumn(0).setResizable(false);
            tblProdutos.getColumnModel().getColumn(1).setResizable(false);
            tblProdutos.getColumnModel().getColumn(2).setResizable(false);
            tblProdutos.getColumnModel().getColumn(3).setResizable(false);
        }

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(10, 90, 616, 130);

        txt_id_prod.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_id_prod.setForeground(new java.awt.Color(217, 217, 217));
        txt_id_prod.setBorder(null);
        txt_id_prod.setEnabled(false);
        txt_id_prod.setOpaque(false);
        getContentPane().add(txt_id_prod);
        txt_id_prod.setBounds(160, 252, 68, 17);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/background/TelaEstoque.png"))); // NOI18N
        getContentPane().add(jLabel9);
        jLabel9.setBounds(0, 0, 640, 460);

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // método para adicionar clientes
        adicionarProd();
        pesquisar_produto();
    }//GEN-LAST:event_btnAdicionarActionPerformed
// o evento abaixo é do tipo "enquanto for digitando"
    private void txt_pesq_prodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pesq_prodKeyReleased
        // chamar o método pesquisar clientes
        pesquisar_produto();
    }//GEN-LAST:event_txt_pesq_prodKeyReleased
// evento que será usado para setar os campos da tabela (clicando com o mouse)
    private void tblProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdutosMouseClicked
        // chamando o método para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblProdutosMouseClicked

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // chamando o método para alterar clientes
        alterar_produto();
        pesquisar_produto();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        // chamando o método para remover um cliente
        remover_produto();
        pesquisar_produto();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void tblProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProdutosKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblProdutosKeyReleased

    private void txt_pesq_prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pesq_prodActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txt_pesq_prodActionPerformed

    private void txt_pesq_prodKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pesq_prodKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txt_pesq_prodKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JComboBox<String> cbx_tipo_prod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable tblProdutos;
    private javax.swing.JTextField txt_desc_prod;
    private javax.swing.JTextField txt_id_prod;
    private javax.swing.JTextField txt_pesq_prod;
    private javax.swing.JTextField txt_valor_prod;
    // End of variables declaration//GEN-END:variables
}
