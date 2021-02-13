/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hamburgueriaCosta.telas;

import br.com.hamburgueriaCosta.dal.ModuloConexao;
import java.sql.*;
import javax.swing.JOptionPane;
// a linha abaixo importa recursos da biblioteca rs2xml.jar
import net.proteanit.sql.DbUtils;

/**
 *
 * @author gabriel
 */
public class TelaProduto extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaProduto
     */
    public TelaProduto() {

        initComponents();
        conexao = ModuloConexao.conector();
        btnexcluir.setEnabled(false);
        btneditar.setEnabled(false);
    }

    private void adicionarproduto() {
        String sql = "insert into produto (nomeproduto,descricao,preco,idfoto) values(?,?,?,?)";
        //caso de certo try, caso de erro catch
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnomepro.getText());
            pst.setString(2, txtdescri.getText());
            pst.setString(3, txtpreco.getText());
            pst.setString(4, txtidfoto.getText());
            //validação dos campos obrigatorios
            if ((txtnomepro.getText().isEmpty()) || (txtdescri.getText().isEmpty()) || (txtpreco.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Dados do Produto");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a inserção dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso");
                    txtnomepro.setText(null);
                    txtdescri.setText(null);
                    txtpreco.setText(null);
                    txtidfoto.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void adicionarfoto() {
        String sql = "insert into foto(fotos,destaque) values(?,?)";
        //caso de certo try, caso de erro catch
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnomeimg.getText());
            pst.setString(2, boxdestaque.getSelectedItem().toString());

            //validação dos campos obrigatorios
            if ((txtnomeimg.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha o campo Nome Img");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a inserção dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Foto adicionado com sucesso");
                    txtnomeimg.setText(null);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void pegaridfoto() {
        String sql = "SELECT MAX(idfoto) FROM foto";
        //caso de certo try, caso de erro catch
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtidfoto.setText(rs.getString(1));
            } else {
                JOptionPane.showMessageDialog(null, "ID da ultima Foto não encontrada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para pesquisar clientes pelo nome com filtro
    private void pesquisar_produto() {
        String sql = "SELECT pr.idproduto as IdProduto, pr.nomeproduto as Produto, pr.descricao as Descrição, pr.preco as Preço, pr.idfoto as IdFoto, ft.fotos as Foto, ft.destaque as Destaque FROM produto pr join foto ft on pr.idfoto = ft.idfoto where nomeproduto like ?";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" - continuação da string sql
            pst.setString(1, txtpesquisa.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml para preencher a tabela
            tabela.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void pesquisar_foto() {
        String sql = "select * from foto where fotos like ?";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" - continuação da string sql
            pst.setString(1, txtpesquisa.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml para preencher a tabela
            tabela.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para setar os campos do formulario com o conteudo da tabela
    public void setar_campos() {
        int setar = tabela.getSelectedRow();
        txtidpro.setText(tabela.getModel().getValueAt(setar, 0).toString());
        txtnomepro.setText(tabela.getModel().getValueAt(setar, 1).toString());
        txtdescri.setText(tabela.getModel().getValueAt(setar, 2).toString());
        txtpreco.setText(tabela.getModel().getValueAt(setar, 3).toString());
        txtidfoto.setText(tabela.getModel().getValueAt(setar, 4).toString());
        txtnomeimg.setText(tabela.getModel().getValueAt(setar, 5).toString());
        boxdestaque.setSelectedItem(tabela.getModel().getValueAt(setar, 6).toString());

        // a linha abaixo desabilita o botão adicionar
        btncriar.setEnabled(false);
        btnexcluir.setEnabled(true);
        btneditar.setEnabled(true);
    }

    public void limpar_campos() {
        txtnomepro.setText(null);
        txtpreco.setText(null);
        txtdescri.setText(null);
        txtidfoto.setText(null);
        txtnomeimg.setText(null);
        txtidpro.setText(null);
    }

    private void alterar() {
        String sql = "update produto set nomeproduto=?,descricao=?,preco=? where idproduto = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnomepro.getText());
            pst.setString(2, txtdescri.getText());
            pst.setString(3, txtpreco.getText());
            pst.setString(4, txtidpro.getText());
            if ((txtnomepro.getText().isEmpty()) || (txtdescri.getText().isEmpty()) || (txtpreco.getText().isEmpty()) || (txtidpro.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Dados do Produto");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a alteração dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do Produto alterados com sucesso");
                    txtidpro.setText(null);
                    txtnomepro.setText(null);
                    txtdescri.setText(null);
                    txtpreco.setText(null);
                    //habilitar os objetos
                    btncriar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void alterarfoto() {
        String sql = "update foto set fotos=?,destaque=? where idfoto=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnomeimg.getText());
            pst.setString(2, boxdestaque.getSelectedItem().toString());
            pst.setString(3, txtidfoto.getText());

            if ((txtnomeimg.getText().isEmpty()) || (txtidfoto.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos da Foto");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a inserção dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    txtnomeimg.setText(null);
                    txtidfoto.setText(null);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void remover() {
        // a estrutura abaixo confirma a remoção do usuario
        String sql = "delete from produto where idproduto=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidpro.getText());
            int apagar = pst.executeUpdate();
            if (apagar > 0) {
                JOptionPane.showMessageDialog(null, "Produto removido com sucesso");
                txtidpro.setText(null);
                txtnomepro.setText(null);
                txtdescri.setText(null);
                txtpreco.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void removerfoto() {
        String sql = "delete from foto where idfoto=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidfoto.getText());
            int apagar = pst.executeUpdate();
            if (apagar > 0) {
                txtidfoto.setText(null);
                txtnomeimg.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtnomepro = new javax.swing.JTextField();
        txtpreco = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtdescri = new javax.swing.JTextArea();
        txtidpro = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        boxdestaque = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtnomeimg = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtidfoto = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        txtpesquisa = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        btncriar = new javax.swing.JButton();
        btnexcluir = new javax.swing.JButton();
        btneditar = new javax.swing.JButton();
        btnlimpar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Produtos");
        setPreferredSize(new java.awt.Dimension(769, 535));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel2.setText("Nome");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel3.setText("Descrição");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel4.setText("Preço");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Produto");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));
        jPanel1.add(txtnomepro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 220, -1));
        jPanel1.add(txtpreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 220, -1));

        txtdescri.setColumns(20);
        txtdescri.setRows(5);
        jScrollPane2.setViewportView(txtdescri);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 220, 60));

        txtidpro.setEnabled(false);
        jPanel1.add(txtidpro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 50, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel13.setText("Idproduto");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 240, 10));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 240, 310));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setText("Foto");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel7.setText("Nome Img");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        boxdestaque.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "nao", "sim" }));
        jPanel2.add(boxdestaque, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 60, 20));

        jLabel8.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel8.setText("Destaque");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));
        jPanel2.add(txtnomeimg, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 220, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel12.setText("Idfoto");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        txtidfoto.setEnabled(false);
        jPanel2.add(txtidfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 50, -1));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 240, 10));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 180, 240, 310));

        txtpesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtpesquisaKeyReleased(evt);
            }
        });
        getContentPane().add(txtpesquisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 250, 30));

        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "IdProduto", "Produto", "Descrição", "Preço", "IdFoto", "Foto", "Destaque"
            }
        ));
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 680, 100));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_magnifyingglass_1055031.png"))); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, -1, -1));

        btncriar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_file_add_48761.png"))); // NOI18N
        btncriar.setPreferredSize(new java.awt.Dimension(80, 80));
        btncriar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncriarActionPerformed(evt);
            }
        });
        getContentPane().add(btncriar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, -1, -1));

        btnexcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_file_delete_48762.png"))); // NOI18N
        btnexcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnexcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexcluirActionPerformed(evt);
            }
        });
        getContentPane().add(btnexcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 293, -1, -1));

        btneditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_file_edit_48763.png"))); // NOI18N
        btneditar.setPreferredSize(new java.awt.Dimension(80, 80));
        btneditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditarActionPerformed(evt);
            }
        });
        getContentPane().add(btneditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 410, -1, -1));

        btnlimpar.setText("Limpar Campos");
        btnlimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimparActionPerformed(evt);
            }
        });
        getContentPane().add(btnlimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 30, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/hamb.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 510));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btncriarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncriarActionPerformed
        // TODO add your handling code here:
        adicionarfoto();
        pegaridfoto();
        adicionarproduto();
        btnexcluir.setEnabled(false);
    }//GEN-LAST:event_btncriarActionPerformed

    private void txtpesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpesquisaKeyReleased
        // esse metodo é do tipo "enquanto for digitando
        /////////////
        pesquisar_produto();

    }//GEN-LAST:event_txtpesquisaKeyReleased

    private void tabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaMouseClicked
        // evento para setar os campos da tabela (clicando com o mouse)
        setar_campos();
    }//GEN-LAST:event_tabelaMouseClicked

    private void btnlimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimparActionPerformed
        // TODO add your handling code here:
        limpar_campos();
        btnexcluir.setEnabled(false);
        btneditar.setEnabled(false);
        btncriar.setEnabled(true);
    }//GEN-LAST:event_btnlimparActionPerformed

    private void btneditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditarActionPerformed
        // TODO add your handling code here:
        alterarfoto();
        alterar();
        btnexcluir.setEnabled(false);
        btneditar.setEnabled(false);
    }//GEN-LAST:event_btneditarActionPerformed

    private void btnexcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexcluirActionPerformed
        // TODO add your handling code here:
        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este produto ?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            remover();
            removerfoto();
            btnexcluir.setEnabled(false);
            btneditar.setEnabled(false);
            btncriar.setEnabled(true);
        }
    }//GEN-LAST:event_btnexcluirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxdestaque;
    private javax.swing.JButton btncriar;
    private javax.swing.JButton btneditar;
    private javax.swing.JButton btnexcluir;
    private javax.swing.JButton btnlimpar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tabela;
    private javax.swing.JTextArea txtdescri;
    private javax.swing.JTextField txtidfoto;
    private javax.swing.JTextField txtidpro;
    private javax.swing.JTextField txtnomeimg;
    private javax.swing.JTextField txtnomepro;
    private javax.swing.JTextField txtpesquisa;
    private javax.swing.JTextField txtpreco;
    // End of variables declaration//GEN-END:variables
}
