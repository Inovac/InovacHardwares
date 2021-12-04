package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

public class TelaCaixa extends javax.swing.JInternalFrame {
//  usando a variavel conexao do DAL

    Connection conexao = null;
    // criando variáveis especiais para conexão com o banco
    //Prepared Statement e ResultSet são frameworks do pacote java.sql
    // e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;

    // String sql = "select * from valorTotal";
    public TelaCaixa() {

        initComponents();
        conexao = ModuloConexao.conector();
        setar_venda();    
    }

    private void pesquisar_produto() {
        String sql = "select descricao, valor  from tbproduto where descricao like ? and tipo= ?";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" - continuação da String sql           
            pst.setString(1, txtpesquisa.getText() + "%");
            pst.setString(2, cbotipo.getSelectedItem().toString());
            //'and tipo= ?'
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblProdutos.setModel(DbUtils.resultSetToTableModel(rs));
            setar_venda();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setar_venda() {
        String sql = "select tipoProduto, descricaoProduto, qtdeProduto, valorProduto from tbvenda";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" - continuação da String sql
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblVendas.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    // método para setar os campos do formulário com o conteúdo da tabela
    /*
    public void setar_valor() {
        String sql = "insert into valor_total (total) select SUM(valorProduto*qtdeProduto) from tbvenda;";
        try {

            pst = conexao.prepareStatement(sql);
            System.out.println("esta funcinando!1");
            rs = pst.executeQuery();
            System.out.println("esta funcinando!2");
            rs.next();
            int valor = (rs.getInt("Total"));
            //txtvalorTotal.setText("" + valor);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "" + e);
        }
    }

    String resultado = rs.getDouble(1);
            System.out.println("esta funcinando!3");
            txtvalorTotal.setText(resultado);
            System.out.println("esta funcinando!4");
            System.out.println(resultado);
            System.out.println("esta funcinando!5");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println("nao esta funcionando");
        }
    }
   public void mostrarId(){
     try{
      sql = "SELECT MAX(idcliente) FROM cadastrocliente";
     PreparedStatement stmt = bd.connection.prepareStatement(sql);
     ResultSet rs = stmt.executeQuery();
    rs.next();
     int valor = (rs.getInt("idcliente"));
     txtCodigoCliente.setText(""+valor);    
     }catch(SQLException e){
         JOptionPane.showMessageDialog(this, ""+e);
     }
  }
     */
    // método para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        int setar = tblProdutos.getSelectedRow();

        txtdesc.setText(tblProdutos.getModel().getValueAt(setar, 0).toString());
        txtvalor.setText(tblProdutos.getModel().getValueAt(setar, 1).toString());
        //a linha abaixo desabilita o botão adicionar
        //btnAdicionar.setEnabled(false);
    }

    private void adicionarProd() {
        String sql = "insert into tbvenda (descricaoProduto,valorProduto,tipoProduto,qtdeProduto) values(?,?,?,?)";
        String sql2 = "insert into valor_total (total) select SUM(valorProduto*qtdeProduto) from tbvenda;";
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtdesc.getText());
            pst.setString(2, txtvalor.getText());
            pst.setString(4, cboqtd.getSelectedItem().toString());
            pst.setString(3, cbotipo.getSelectedItem().toString());

            //validação dos campos obrigatórios   
            if ((txtdesc.getText().isEmpty()) || (cboqtd.getSelectedItem().toString().isEmpty()) || (txtvalor.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            } else {
                //a linha abaixo atualiza a tabela usuario com os dados do formulário
                //a estrutura abaixo é usada para confirmar a inserção dos dados na tabela
                int adicionado = pst.executeUpdate();
                //a linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto adicionado ao Carrinho");
                    // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
                    tblVendas.setModel(DbUtils.resultSetToTableModel(rs));
                    limpar();
                    setar_venda();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo de limpeza de campos do formulario
    private void limpar() {
        txtdesc.setText(null);
        txtvalor.setText(null);
        cboqtd.setSelectedItem(null);
        cbotipo.setSelectedItem(null);
        txtpesquisa.setText(null);
        ((DefaultTableModel) tblProdutos.getModel()).setRowCount(0);
    }
     private void pix() {
        lblpix.setText("Pix: 234.785.234-0");
    }
    // método responsável pela remoção de clientes
    private void comprar() {
        //a estrutura abaixo confirma a remoção do cliente
        int confirma = JOptionPane.showConfirmDialog(null, "Deseja Finalizar a compra?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbvenda";
            try {
                pst = conexao.prepareStatement(sql);
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Compra Feita com Sucesso!");
                    tblVendas.setModel(DbUtils.resultSetToTableModel(rs));
                    //btnAdicionar.setEnabled(true);
                    limpar();
                    setar_venda();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    // método responsável pela remoção de clientes
    private void excluir() {
        //a estrutura abaixo confirma a remoção do cliente
        int confirma = JOptionPane.showConfirmDialog(null, "Deseja Cancelar a compra?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbvenda";
            try {
                pst = conexao.prepareStatement(sql);
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Compra Foi Deletada!");
                    tblVendas.setModel(DbUtils.resultSetToTableModel(rs));
                    //btnAdicionar.setEnabled(true);
                    limpar();
                    setar_venda();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVendas = new javax.swing.JTable();
        lblpix = new javax.swing.JLabel();
        btnPagar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnFinalizar = new javax.swing.JButton();
        txtdesc = new javax.swing.JTextField();
        txtvalor = new javax.swing.JTextField();
        cbotipo = new javax.swing.JComboBox<>();
        cboqtd = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProdutos = new javax.swing.JTable();
        txtpesquisa = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        jToolBar1.setRollover(true);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setToolTipText("Vendas");
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(640, 480));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        getContentPane().setLayout(null);

        tblVendas.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Tipo", "Produto", "Quantidade", "Valor Unitario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblVendas);
        if (tblVendas.getColumnModel().getColumnCount() > 0) {
            tblVendas.getColumnModel().getColumn(0).setResizable(false);
            tblVendas.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblVendas.getColumnModel().getColumn(1).setResizable(false);
            tblVendas.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblVendas.getColumnModel().getColumn(2).setResizable(false);
            tblVendas.getColumnModel().getColumn(2).setPreferredWidth(10);
            tblVendas.getColumnModel().getColumn(3).setResizable(false);
            tblVendas.getColumnModel().getColumn(3).setPreferredWidth(40);
        }

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 330, 600, 100);

        lblpix.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        lblpix.setForeground(new java.awt.Color(255, 255, 255));
        lblpix.setText("Forma de Pagamento");
        getContentPane().add(lblpix);
        lblpix.setBounds(360, 30, 270, 50);

        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/background/icons8-sacar-dinheiro-60.png"))); // NOI18N
        btnPagar.setBorder(null);
        btnPagar.setOpaque(false);
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });
        getContentPane().add(btnPagar);
        btnPagar.setBounds(380, 110, 60, 60);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/background/icons8-comprar-50.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.setOpaque(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(450, 110, 60, 60);

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/background/icons8-cancelar-40.png"))); // NOI18N
        btnRemover.setOpaque(false);
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });
        getContentPane().add(btnRemover);
        btnRemover.setBounds(520, 110, 60, 60);

        btnFinalizar.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        btnFinalizar.setForeground(new java.awt.Color(51, 51, 51));
        btnFinalizar.setText("Finalizar");
        btnFinalizar.setEnabled(false);
        btnFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnFinalizar);
        btnFinalizar.setBounds(380, 180, 200, 40);

        txtdesc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtdesc.setForeground(new java.awt.Color(51, 51, 51));
        txtdesc.setBorder(null);
        txtdesc.setEnabled(false);
        txtdesc.setOpaque(false);
        getContentPane().add(txtdesc);
        txtdesc.setBounds(123, 279, 200, 17);

        txtvalor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtvalor.setForeground(new java.awt.Color(51, 51, 51));
        txtvalor.setBorder(null);
        txtvalor.setEnabled(false);
        txtvalor.setOpaque(false);
        getContentPane().add(txtvalor);
        txtvalor.setBounds(123, 302, 90, 16);

        cbotipo.setForeground(new java.awt.Color(51, 51, 51));
        cbotipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Mouse", "Teclado", "Processador", "Placa de Video", "Placa Mae" }));
        cbotipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbotipoActionPerformed(evt);
            }
        });
        getContentPane().add(cbotipo);
        cbotipo.setBounds(120, 232, 120, 20);

        cboqtd.setForeground(new java.awt.Color(51, 51, 51));
        cboqtd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "1", "2", "3", "4" }));
        cboqtd.setBorder(null);
        cboqtd.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(cboqtd);
        cboqtd.setBounds(413, 277, 120, 20);

        tblProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Descricao", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
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
        jScrollPane2.setViewportView(tblProdutos);
        if (tblProdutos.getColumnModel().getColumnCount() > 0) {
            tblProdutos.getColumnModel().getColumn(1).setResizable(false);
        }

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(20, 50, 220, 170);

        txtpesquisa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtpesquisa.setForeground(new java.awt.Color(51, 51, 51));
        txtpesquisa.setBorder(null);
        txtpesquisa.setOpaque(false);
        txtpesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpesquisaActionPerformed(evt);
            }
        });
        txtpesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpesquisaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtpesquisaKeyReleased(evt);
            }
        });
        getContentPane().add(txtpesquisa);
        txtpesquisa.setBounds(26, 24, 210, 14);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/background/TELACAIXA_1.png"))); // NOI18N
        jLabel7.setText("jLabel7");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(0, 0, 640, 450);

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameActivated

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        adicionarProd();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        // TODO add your handling code here:
        excluir();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void tblProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdutosMouseClicked
        // TODO add your handling code here:
        setar_campos();
        
    }//GEN-LAST:event_tblProdutosMouseClicked

    private void txtpesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpesquisaKeyReleased

        pesquisar_produto();
    }//GEN-LAST:event_txtpesquisaKeyReleased

    private void txtpesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpesquisaActionPerformed

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        // TODO add your handling code here:
        lblpix.setText("Forma de Pagamento");
        btnFinalizar.setEnabled(false);
        comprar();

    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void txtpesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpesquisaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpesquisaKeyPressed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed

        pix();
        btnFinalizar.setEnabled(true);
    }//GEN-LAST:event_btnPagarActionPerformed

    private void cbotipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbotipoActionPerformed
        
        pesquisar_produto();
        
    }//GEN-LAST:event_cbotipoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JComboBox<String> cboqtd;
    private javax.swing.JComboBox<String> cbotipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblpix;
    private javax.swing.JTable tblProdutos;
    private javax.swing.JTable tblVendas;
    private javax.swing.JTextField txtdesc;
    private javax.swing.JTextField txtpesquisa;
    private javax.swing.JTextField txtvalor;
    // End of variables declaration//GEN-END:variables
}
