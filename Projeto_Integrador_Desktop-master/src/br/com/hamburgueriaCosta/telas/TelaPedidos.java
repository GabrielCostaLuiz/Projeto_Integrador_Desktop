/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hamburgueriaCosta.telas;

import br.com.hamburgueriaCosta.dal.ModuloConexao;
import java.sql.*;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author gabriel
 */
public class TelaPedidos extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaPedidos
     */
    public TelaPedidos() {
        initComponents();
        conexao = ModuloConexao.conector();
        //btnexcluir.setEnabled(false);
    }
    
    private void pesquisar() {
        String sql = "select cl.idcliente as IdCliente,cl.nomecliente as Nome,pe.idpedido as IdPedido,pe.datapedido as Data from cliente cl join pedido pe on cl.idcliente = pe.idcliente where nomecliente like ?";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" - continuação da string sql
            pst.setString(1, txtpesquisa1.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml para preencher a tabela
            tabela1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
     public void setar_campos() {
        int setar = tabela1.getSelectedRow();
        txtidcliente.setText(tabela1.getModel().getValueAt(setar, 0).toString());
        txtnomecliente.setText(tabela1.getModel().getValueAt(setar, 1).toString());
        txtidpedido.setText(tabela1.getModel().getValueAt(setar, 2).toString());
        txtdatapedido.setText(tabela1.getModel().getValueAt(setar, 3).toString());
    }

    private void consultar() {
        String num_id = JOptionPane.showInputDialog("Número do Cliente");
        String sql = "select cl.idcliente"
                + ",cl.nomecliente,"
                + "pe.idpedido,"
                + "pe.datapedido,"
                + "pr.nomeproduto,"
                + "pr.preco,"
                + "it.iditens,"
                + "it.quantidade,"
                + "pa.idpagamento,"
                + "pa.tipo,"
                + "pa.descricao,"
                + "pa.valor,"
                + "pa.parcelas,"
                + "pa.valorparcela "
                + "from cliente cl inner join pedido pe on cl.idcliente = pe.idcliente "
                + "inner join itenspedido it on pe.idpedido= it.idpedido "
                + "inner join produto pr on pr.idproduto = it.idproduto "
                + "inner join pagamento pa on pa.idpedido = pe.idpedido where cl.idcliente =" + num_id;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtidcliente.setText(rs.getString(1));
                txtnomecliente.setText(rs.getString(2));
                txtidpedido.setText(rs.getString(3));
                txtdatapedido.setText(rs.getString(4));
                txtiditens.setText(rs.getString(7));
                txtidpagamento.setText(rs.getString(9));
                btntipo.setSelectedItem(rs.getString(10));
                txtdescricao.setText(rs.getString(11));
                txtvalor.setText(rs.getString(12));
                btnparcelas.setSelectedItem(rs.getString(13));
                txtvalorparcela.setText(rs.getString(14));

            } else {
                JOptionPane.showMessageDialog(null, "Cliente não cadastrado");
                //as linhas abaixo "limpam" os campos;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    
     private void mostrar(){
        String sql = "select pr.nomeproduto as Hamburguers,"
                +"pr.preco as Preço," 
                +"it.quantidade as Quantidade " 
                + "from itenspedido it inner join pedido pe on it.idpedido = pe.idpedido "
                + "inner join produto pr on pr.idproduto = it.idproduto where pe.idpedido = ?";
           try {
        pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidpedido.getText());
            rs = pst.executeQuery(); 
            jtb.setModel(DbUtils.resultSetToTableModel(rs));  
            } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
     }
    
    private void consultar2() {
        String sql = "select pe.idpedido,"
                + "pr.nomeproduto,"
                + "pr.preco,"
                + "it.iditens,"
                + "it.quantidade,"
                + "pa.idpagamento,"
                + "pa.tipo,"
                + "pa.descricao,"
                + "pa.valor,"
                + "pa.parcelas,"
                + "pa.valorparcela "
                + "from itenspedido it inner join pedido pe on it.idpedido = pe.idpedido "
                + "inner join produto pr on pr.idproduto = it.idproduto "
                + "inner join pagamento pa on pa.idpedido = pe.idpedido where pe.idpedido = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidpedido.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtidpedido.setText(rs.getString(1));
                txtiditens.setText(rs.getString(4));
                txtidpagamento.setText(rs.getString(6));
                btntipo.setSelectedItem(rs.getString(7));
                txtdescricao.setText(rs.getString(8));
                txtvalor.setText(rs.getString(9));
                btnparcelas.setSelectedItem(rs.getString(10));
                txtvalorparcela.setText(rs.getString(11));
                //btnexcluir.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(null, "Pedido não cadastrado ou incompleto");
                //as linhas abaixo "limpam" os campos;
                txtidpedido.setText(null);
                txtiditens.setText(null);
                txtidpagamento.setText(null);
                txtdescricao.setText(null);
                txtvalor.setText(null);
                txtvalorparcela.setText(null);
                txtidcliente.setText(null);
                txtidpedido.setText(null);
                txtdatapedido.setText(null);
                txtnomecliente.setText(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
     //private void remover() {
         //a estrutura abaixo confirma a remoção do usuario
        //String sql = "delete from pedido where idpedido=?";
        //try {
            //pst = conexao.prepareStatement(sql);
            //pst.setString(1, txtidpedido.getText());
            //int apagar = pst.executeUpdate();
            //if (apagar > 0) {
                //JOptionPane.showMessageDialog(null, "Pedido removido com sucesso");
                //txtdatapedido.setText(null);
                //txtidpedido.setText(null);
                //txtidcliente.setText(null);
                //txtnomecliente.setText(null);
               
            //}
        //} catch (Exception e) {
           //JOptionPane.showMessageDialog(null, e);
        //}
    //}
     
     // private void remover2() {
        // a estrutura abaixo confirma a remoção do usuario
         //String sql = "delete from itenspedido where iditens=?";
         //try {
             //pst = conexao.prepareStatement(sql);
             //pst.setString(1, txtiditens.getText());
             //int apagar = pst.executeUpdate();
             //if (apagar > 0) {
                 //JOptionPane.showMessageDialog(null, "Itens removido com sucesso");
                 //txtiditens.setText(null);

            // }
        // } catch (Exception e) {
             //JOptionPane.showMessageDialog(null, e);
         //}
     //}
      
        //private void remover3() {
        // a estrutura abaixo confirma a remoção do usuario
         //String sql = "delete from pagamento where idpagamento=?";
         //try {
             //pst = conexao.prepareStatement(sql);
            // pst.setString(1, txtidpagamento.getText());
             //int apagar = pst.executeUpdate();
             //if (apagar > 0) {
                 //JOptionPane.showMessageDialog(null, "Pagamento removido com sucesso");
                 //txtidpagamento.setText(null);
                 //txtdescricao.setText(null);
                 //txtvalor.setText(null);
                 //txtvalorparcela.setText(null);
             //}
         //} catch (Exception e) {
             //JOptionPane.showMessageDialog(null, e);
         //}
     //}
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtidpagamento = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtvalor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtvalorparcela = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtdescricao = new javax.swing.JTextArea();
        btnparcelas = new javax.swing.JComboBox<>();
        btntipo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtidcliente = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtdatapedido = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtidpedido = new javax.swing.JTextField();
        txtnomecliente = new javax.swing.JTextField();
        txtiditens = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtpesquisa1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtb = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Consultar Pedido");
        setPreferredSize(new java.awt.Dimension(769, 535));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMinimumSize(new java.awt.Dimension(230, 360));
        jPanel1.setPreferredSize(new java.awt.Dimension(230, 360));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtidpagamento.setEnabled(false);
        jPanel1.add(txtidpagamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel15.setText("Idpagamento");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel4.setText("Tipo de pagamento");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel5.setText("Descrição");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        txtvalor.setEnabled(false);
        txtvalor.setMinimumSize(new java.awt.Dimension(16, 20));
        txtvalor.setName(""); // NOI18N
        jPanel1.add(txtvalor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 210, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel7.setText("Parcelas");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel8.setText("Valor parcelado");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, -1, -1));

        txtvalorparcela.setEnabled(false);
        txtvalorparcela.setMinimumSize(new java.awt.Dimension(16, 20));
        txtvalorparcela.setName(""); // NOI18N
        jPanel1.add(txtvalorparcela, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 210, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel13.setText("Valor");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel19.setText("Pagamento");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 230, 10));

        txtdescricao.setColumns(20);
        txtdescricao.setRows(5);
        txtdescricao.setEnabled(false);
        jScrollPane3.setViewportView(txtdescricao);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 210, 50));

        btnparcelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));
        btnparcelas.setEnabled(false);
        jPanel1.add(btnparcelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        btntipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Boleto", "Crédito", "Débito" }));
        btntipo.setEnabled(false);
        jPanel1.add(btntipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 230, 360));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setMinimumSize(new java.awt.Dimension(230, 360));
        jPanel2.setPreferredSize(new java.awt.Dimension(230, 360));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel9.setText("Idcliente");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, -1, -1));

        txtidcliente.setEnabled(false);
        jPanel2.add(txtidcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 50, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel1.setText("Data do pedido");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        txtdatapedido.setEnabled(false);
        txtdatapedido.setMinimumSize(new java.awt.Dimension(16, 20));
        txtdatapedido.setName(""); // NOI18N
        jPanel2.add(txtdatapedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 210, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel10.setText("Nome");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel3.setText("Idpedido");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, -1, -1));

        txtidpedido.setEnabled(false);
        txtidpedido.setMinimumSize(new java.awt.Dimension(16, 20));
        txtidpedido.setName(""); // NOI18N
        jPanel2.add(txtidpedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 50, -1));

        txtnomecliente.setEnabled(false);
        jPanel2.add(txtnomecliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 210, -1));

        txtiditens.setEnabled(false);
        jPanel2.add(txtiditens, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 50, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel12.setText("Iditens");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel18.setText("Informações do pedido");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 230, 10));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 230, 360));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabela1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "IdCliente", "Nome", "IdPedido", "Data"
            }
        ));
        tabela1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabela1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela1);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 450, 100));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_magnifyingglass_1055031.png"))); // NOI18N
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 50, -1));

        txtpesquisa1.setText("Insira o nome do cliente aqui");
        txtpesquisa1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtpesquisa1MouseClicked(evt);
            }
        });
        txtpesquisa1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtpesquisa1KeyReleased(evt);
            }
        });
        jPanel4.add(txtpesquisa1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 190, 30));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 730, 120));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setMinimumSize(new java.awt.Dimension(250, 360));
        jPanel3.setPreferredSize(new java.awt.Dimension(250, 360));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setText("Produtos");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 270, 10));

        jtb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Hamburguers", "Preço", "Quantidade"
            }
        ));
        jScrollPane4.setViewportView(jtb);

        jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 250, 310));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 270, 360));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/hamb.jpg"))); // NOI18N
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabela1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela1MouseClicked
        // TODO add your handling code here:
        setar_campos();
        consultar2();
        mostrar();
    }//GEN-LAST:event_tabela1MouseClicked

    private void txtpesquisa1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpesquisa1KeyReleased
        // TODO add your handling code here:
        pesquisar();
    }//GEN-LAST:event_txtpesquisa1KeyReleased

    private void txtpesquisa1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtpesquisa1MouseClicked
        // TODO add your handling code here:
        txtpesquisa1.setText(null);
    }//GEN-LAST:event_txtpesquisa1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> btnparcelas;
    private javax.swing.JComboBox<String> btntipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jtb;
    private javax.swing.JTable tabela1;
    private javax.swing.JTextField txtdatapedido;
    private javax.swing.JTextArea txtdescricao;
    private javax.swing.JTextField txtidcliente;
    private javax.swing.JTextField txtiditens;
    private javax.swing.JTextField txtidpagamento;
    private javax.swing.JTextField txtidpedido;
    private javax.swing.JTextField txtnomecliente;
    private javax.swing.JTextField txtpesquisa1;
    private javax.swing.JTextField txtvalor;
    private javax.swing.JTextField txtvalorparcela;
    // End of variables declaration//GEN-END:variables
}
