/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

//importando a biblioteca sql
import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import java.awt.Color;
import javax.swing.JOptionPane;



public class TelaLogin extends javax.swing.JFrame {

    //  usando a variavel conexao do DAL
    Connection conexao = null;
    // criando variáveis especiais para conexão com o banco
    //Prepared Statement e ResultSet são frameworks do pacote java.sql
    // e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;

    //criando o metodo logar
    public void logar() {

        //logica principal para pesquisar no banco de dados
        String sql = "select * from tbusuario where login = ? and senha = ?";
        try {
        //as linhas abaixo preparam a consulta em função do que foi 
        //digitado nas caixas de texto. O ? é substituído pelo conteúdo
        //das variáveis que são armazenadas em pst.setString
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUsuario.getText());
            pst.setString(2, txtSenha.getText());
            //a linha abaixo executa a query(consulta)
            rs = pst.executeQuery();
            //se existir um usuário e senha correspondente
            if (rs.next()) {
                //a linha abaixo obtem o conteúdo do campo perfil da tabela tbusuario
                String perfil = rs.getString(6);
                //System.out.println(perfil);
                //a estrutura abaixo faz o tratamento do perfil do usuário
                if (perfil.equals("admin")) {
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                    //TelaPrincipal.menRel.setEnabled(true);
                    //TelaPrincipal.menCadUsu.setEnabled(true);
                    TelaPrincipal.lblUsuario.setText(rs.getString(2));
                    TelaPrincipal.lblUsuario.setForeground(Color.green);
                    this.dispose();
                    
                }else{
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                    TelaPrincipal.lblUsuario.setText(rs.getString(2));
                    this.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, "usuário e/ou senha inválido(s)");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public TelaLogin() {
        initComponents();
        //estabelecendo a conexao com o banco sempre neste
        //ponto
        conexao = ModuloConexao.conector();
        // a estrutura abaixo muda o ícone de acordo com o status da conexao
        if (conexao != null) {
            lblConexao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/dbok.png")));
        } else {
            lblConexao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/dberror.png")));
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

        lblConexao = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);
        getContentPane().setLayout(null);

        lblConexao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/dberror.png"))); // NOI18N
        getContentPane().add(lblConexao);
        lblConexao.setBounds(180, 260, 32, 32);

        txtUsuario.setBackground(new java.awt.Color(217, 217, 217));
        txtUsuario.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        txtUsuario.setForeground(new java.awt.Color(102, 102, 102));
        txtUsuario.setBorder(null);
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        getContentPane().add(txtUsuario);
        txtUsuario.setBounds(283, 172, 134, 16);
        txtUsuario.getAccessibleContext().setAccessibleName("");

        txtSenha.setBackground(new java.awt.Color(217, 217, 217));
        txtSenha.setForeground(new java.awt.Color(102, 102, 102));
        txtSenha.setBorder(null);
        getContentPane().add(txtSenha);
        txtSenha.setBounds(283, 212, 134, 16);

        btnLogin.setBorder(null);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogin);
        btnLogin.setBounds(283, 242, 59, 15);

        btnSair.setBorder(null);
        btnSair.setContentAreaFilled(false);
        btnSair.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
        getContentPane().add(btnSair);
        btnSair.setBounds(358, 242, 59, 15);

        jLabel3.setBackground(new java.awt.Color(217, 217, 217));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/background/Entrar.png"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(0, 0, 700, 350);

        setSize(new java.awt.Dimension(712, 358));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        //chamando o método logar
        logar();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        // Aciona o botao para sair do programa
        int confirma = JOptionPane.showConfirmDialog(null, "Deseja Sair do Programa?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
        System.exit(0);
        }
    }//GEN-LAST:event_btnSairActionPerformed

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnSair;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblConexao;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
