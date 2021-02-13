/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hamburgueriaCosta.telas;

import br.com.hamburgueriaCosta.dal.ModuloConexao;
import java.security.MessageDigest;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author gabriel
 */
public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCliente
     */
    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
        btnexcluir.setEnabled(false);
        btneditar.setEnabled(false);
    }

    private void adicionarcliente() {
        String sql = "insert into cliente (nomecliente,cpf,telefone,sexo,idendereco,idusuario) values(?,?,?,?,?,?)";
        //caso de certo try, caso de erro catch
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnome.getText());
            pst.setString(2, txtcpf.getText());
            pst.setString(3, txttelefone.getText());
            pst.setString(4, boxsexo.getSelectedItem().toString());
            pst.setString(5, txtidend.getText());
            pst.setString(6, txtidusu.getText());
            //validação dos campos obrigatorios
            if ((txtnome.getText().isEmpty()) || (txtcpf.getText().isEmpty()) || (txttelefone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Dados Pessoais");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a inserção dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso");
                    txtnome.setText(null);
                    txtcpf.setText(null);
                    txttelefone.setText(null);
                    txtidend.setText(null);
                    txtidusu.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void adicionarendereco() {
        String sql = "insert into endereco(tipo,logradouro,numero,complemento,bairro,cep) values(?,?,?,?,?,?)";
        //caso de certo try, caso de erro catch
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, boxtipo.getSelectedItem().toString());
            pst.setString(2, txtlogradouro.getText());
            pst.setString(3, txtnumero.getText());
            pst.setString(4, txtcomplemento.getText());
            pst.setString(5, txtbairro.getText());
            pst.setString(6, txtcep.getText());
            //validação dos campos obrigatorios
            if ((txtlogradouro.getText().isEmpty()) || (txtnumero.getText().isEmpty()) || (txtcomplemento.getText().isEmpty()) || (txtbairro.getText().isEmpty()) || (txtcep.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos do Endereço");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a inserção dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Endereço adicionado com sucesso");
                    txtlogradouro.setText(null);
                    txtnumero.setText(null);
                    txtcomplemento.setText(null);
                    txtbairro.setText(null);
                    txtcep.setText(null);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void adicionarusuario() {
        String sql = "insert into usuario(nomeusuario,senha,perfil) values(?,?,?)";
        String senha = txtsenha.getText();
        //caso de certo try, caso de erro catch

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String s = senha;
            md.update(s.getBytes());
            byte[] hash = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }

            String criptografado = hexString.toString();

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtlogin.getText());
            pst.setString(2, criptografado);
            pst.setString(3, boxperfil.getSelectedItem().toString());

            //validação dos campos obrigatorios
            if ((txtlogin.getText().isEmpty()) || (txtsenha.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos do Usuário");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a inserção dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso");
                    txtlogin.setText(null);
                    txtsenha.setText(null);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void pegaridusuario() {
        String sql = "SELECT MAX(idusuario) FROM usuario";
        //caso de certo try, caso de erro catch
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtidusu.setText(rs.getString(1));
            } else {
                JOptionPane.showMessageDialog(null, "ID do ultimo Usuário não encontrado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void pegaridendereco() {
        String sql = "SELECT MAX(idendereco) FROM endereco";
        //caso de certo try, caso de erro catch
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtidend.setText(rs.getString(1));
            } else {
                JOptionPane.showMessageDialog(null, "ID do ultimo Endereço não encontrado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void consultar() {
        String num_id = JOptionPane.showInputDialog("Número do ID, se não souber procure na aba Consultar/Consultar Geral");
        String sql = "select "
                + "        cl.idcliente,"
                + "        cl.nomecliente,"
                + "        cl.cpf,"
                + "        cl.telefone,"
                + "        cl.sexo,"
                + "        en.idendereco,"
                + "        en.tipo,"
                + "        en.logradouro,"
                + "        en.numero,"
                + "        en.complemento,"
                + "        en.bairro,"
                + "        en.cep,"
                + "        us.idusuario,"
                + "        us.nomeusuario,"
                + "        us.senha,"
                + "        us.perfil"
                + "        from usuario us inner join cliente cl on us.idusuario=cl.idusuario"
                + "        inner join endereco en on en.idendereco = cl.idendereco"
                + "         where idcliente=" + num_id;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtidcli.setText(rs.getString(1));
                txtnome.setText(rs.getString(2));
                txtcpf.setText(rs.getString(3));
                txttelefone.setText(rs.getString(4));
                boxsexo.setSelectedItem(rs.getString(5));
                txtidend.setText(rs.getString(6));
                boxtipo.setSelectedItem(rs.getString(7));
                txtlogradouro.setText(rs.getString(8));
                txtnumero.setText(rs.getString(9));
                txtcomplemento.setText(rs.getString(10));
                txtbairro.setText(rs.getString(11));
                txtcep.setText(rs.getString(12));
                txtidusu.setText(rs.getString(13));
                txtlogin.setText(rs.getString(14));
                txtsenha.setText(rs.getString(15));
                boxperfil.setSelectedItem(rs.getString(16));
                //evitando problemas
                btncriar.setEnabled(false);
                txtsenha.setEnabled(false);
                boxsexo.setEnabled(false);
                btnexcluir.setEnabled(true);
                btneditar.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente não cadastrado");
                //as linhas abaixo "limpam" os campos
                txtnome.setText(null);
                txtcpf.setText(null);
                txttelefone.setText(null);
                txtidusu.setText(null);
                txtidend.setText(null);
                txtlogradouro.setText(null);
                txtnumero.setText(null);
                txtcomplemento.setText(null);
                txtbairro.setText(null);
                txtcep.setText(null);
                txtlogin.setText(null);
                txtsenha.setText(null);
                btnexcluir.setEnabled(false);
                btnexcluir.setEnabled(false);
                btneditar.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /*private void consultarend() {
        String sql = "select * from endereco where idendereco = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidend.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                boxtipo.setSelectedItem(rs.getString(2));
                txtlogradouro.setText(rs.getString(3));
                txtnumero.setText(rs.getString(4));
                txtcomplemento.setText(rs.getString(5));
                txtbairro.setText(rs.getString(6));
                txtcep.setText(rs.getString(7));
                btnexcluir.setEnabled(true);
                btneditar.setEnabled(true);
            } else {
                //as linhas abaixo "limpam" os campos
                txtlogradouro.setText(null);
                txtnumero.setText(null);
                txtcomplemento.setText(null);
                txtbairro.setText(null);
                txtcep.setText(null);
                btnexcluir.setEnabled(false);
                btneditar.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }*/

   /* private void consultarusu() {
        String sql = "select * from usuario where idusuario = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidusu.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtlogin.setText(rs.getString(2));
                txtsenha.setText(rs.getString(3));
                boxperfil.setSelectedItem(rs.getString(4));
                btnexcluir.setEnabled(true);
                btneditar.setEnabled(true);
            } else {
                //as linhas abaixo "limpam" os campos
                txtlogin.setText(null);
                txtsenha.setText(null);
                btnexcluir.setEnabled(false);
                btneditar.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }*/

    private void alterar() {
        String sql = "update cliente set nomecliente=?,cpf=?,telefone=?,sexo=? where idcliente = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnome.getText());
            pst.setString(2, txtcpf.getText());
            pst.setString(3, txttelefone.getText());
            pst.setString(4, boxsexo.getSelectedItem().toString());
            pst.setString(5, txtidcli.getText());
            if ((txtidcli.getText().isEmpty()) || (txtnome.getText().isEmpty()) || (txtcpf.getText().isEmpty()) || (txttelefone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Dados Pessoais");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a alteração dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do Cliente alterados com sucesso");
                    txtidcli.setText(null);
                    txtnome.setText(null);
                    txtcpf.setText(null);
                    txttelefone.setText(null);
                    //habilitar os objetos
                    btncriar.setEnabled(true);
                    txtsenha.setEnabled(true);
                    boxsexo.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void alterarend() {
        String sql = "update endereco set tipo=?,logradouro=?,numero=?,complemento=?,bairro=?,cep=? where idendereco=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, boxtipo.getSelectedItem().toString());
            pst.setString(2, txtlogradouro.getText());
            pst.setString(3, txtnumero.getText());
            pst.setString(4, txtcomplemento.getText());
            pst.setString(5, txtbairro.getText());
            pst.setString(6, txtcep.getText());
            pst.setString(7, txtidend.getText());
            if ((txtlogradouro.getText().isEmpty()) || (txtnumero.getText().isEmpty()) || (txtcomplemento.getText().isEmpty()) || (txtbairro.getText().isEmpty()) || (txtcep.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos do seu Endereço");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a alteração dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    txtlogradouro.setText(null);
                    txtnumero.setText(null);
                    txtcomplemento.setText(null);
                    txtbairro.setText(null);
                    txtcep.setText(null);
                    txtidend.setText(null);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void alterarusu() {
        String sql = "update usuario set nomeusuario=?,senha=?,perfil=? where idusuario=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtlogin.getText());
            pst.setString(2, txtsenha.getText());
            pst.setString(3, boxperfil.getSelectedItem().toString());
            pst.setString(4, txtidusu.getText());

            if ((txtlogin.getText().isEmpty()) || (txtsenha.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos do Usuário");
            } else {
// a linha abaixo atualiza a tabela usuarios com os dados do formulario
                // a estrutura abaixo é usada para confirmar a inserção dos dados da tabela
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    txtlogin.setText(null);
                    txtsenha.setText(null);
                    txtidusu.setText(null);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void remover() {
        // a estrutura abaixo confirma a remoção do usuario
        String sql = "delete from cliente where idcliente=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidcli.getText());
            int apagar = pst.executeUpdate();
            if (apagar > 0) {
                JOptionPane.showMessageDialog(null, "Usuário removido com sucesso");
                txtidcli.setText(null);
                txtnome.setText(null);
                txtcpf.setText(null);
                txttelefone.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void removerend() {
        String sql = "delete from endereco where idendereco=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidend.getText());
            int apagar = pst.executeUpdate();
            if (apagar > 0) {
                txtlogradouro.setText(null);
                txtnumero.setText(null);
                txtcomplemento.setText(null);
                txtbairro.setText(null);
                txtcep.setText(null);
                txtidend.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void removerusu() {
        String sql = "delete from usuario where idusuario=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtidusu.getText());
            int apagar = pst.executeUpdate();
            if (apagar > 0) {
                txtidusu.setText(null);
                txtlogin.setText(null);
                txtsenha.setText(null);

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

        jPanel3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtidusu = new javax.swing.JTextField();
        txtlogin = new javax.swing.JTextField();
        txtsenha = new javax.swing.JPasswordField();
        boxperfil = new javax.swing.JComboBox<>();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtidend = new javax.swing.JTextField();
        txtlogradouro = new javax.swing.JTextField();
        txtnumero = new javax.swing.JTextField();
        txtcomplemento = new javax.swing.JTextField();
        txtbairro = new javax.swing.JTextField();
        txtcep = new javax.swing.JTextField();
        boxtipo = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        btneditar = new javax.swing.JButton();
        btncriar = new javax.swing.JButton();
        btnexcluir = new javax.swing.JButton();
        btnprocurar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtcpf = new javax.swing.JTextField();
        txtnome = new javax.swing.JTextField();
        txtidcli = new javax.swing.JTextField();
        txttelefone = new javax.swing.JTextField();
        boxsexo = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Cliente");
        setPreferredSize(new java.awt.Dimension(769, 535));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setPreferredSize(new java.awt.Dimension(230, 300));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setText("Usuário");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel3.setText("Perfil");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel2.setText("Senha");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel23.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel23.setText("Idusuário");
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel14.setText("Login");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        txtidusu.setEnabled(false);
        jPanel3.add(txtidusu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 50, -1));
        jPanel3.add(txtlogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 210, -1));
        jPanel3.add(txtsenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 210, -1));

        boxperfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "funcionario", "admin", "cliente" }));
        boxperfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxperfilActionPerformed(evt);
            }
        });
        jPanel3.add(boxperfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 250, 10));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, -1, 400));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(230, 400));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel5.setText("Logradouro");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setText("Endereço");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel8.setText("Bairro");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel6.setText("Numero");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel4.setText("Tipo");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel9.setText("CEP");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel7.setText("Complemento");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        jLabel22.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel22.setText("Idendereco");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        txtidend.setEnabled(false);
        jPanel2.add(txtidend, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 60, -1));
        jPanel2.add(txtlogradouro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 230, -1));
        jPanel2.add(txtnumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 230, -1));
        jPanel2.add(txtcomplemento, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 230, -1));
        jPanel2.add(txtbairro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 230, -1));
        jPanel2.add(txtcep, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 230, -1));

        boxtipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Av", "Rua", "Al", "Praça" }));
        jPanel2.add(boxtipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 250, 10));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 250, 400));

        btneditar.setBackground(new java.awt.Color(255, 255, 255));
        btneditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_file_edit_48763.png"))); // NOI18N
        btneditar.setPreferredSize(new java.awt.Dimension(80, 80));
        btneditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditarActionPerformed(evt);
            }
        });
        getContentPane().add(btneditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 420, -1, -1));

        btncriar.setBackground(new java.awt.Color(255, 255, 255));
        btncriar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_file_add_48761.png"))); // NOI18N
        btncriar.setPreferredSize(new java.awt.Dimension(80, 80));
        btncriar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncriarActionPerformed(evt);
            }
        });
        getContentPane().add(btncriar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 420, -1, -1));

        btnexcluir.setBackground(new java.awt.Color(255, 255, 255));
        btnexcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_file_delete_48762.png"))); // NOI18N
        btnexcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnexcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexcluirActionPerformed(evt);
            }
        });
        getContentPane().add(btnexcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 420, -1, -1));

        btnprocurar.setBackground(new java.awt.Color(255, 255, 255));
        btnprocurar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/iconfinder_file_search_48764.png"))); // NOI18N
        btnprocurar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnprocurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprocurarActionPerformed(evt);
            }
        });
        getContentPane().add(btnprocurar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 420, -1, -1));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setText("Dados Pessoais");
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel11.setText("Idcliente");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel12.setText("Nome");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel13.setText("Telefone");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jLabel17.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel17.setText("CPF");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel18.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel18.setText("Sexo");
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));
        jPanel4.add(txtcpf, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 210, -1));
        jPanel4.add(txtnome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 210, -1));

        txtidcli.setEnabled(false);
        jPanel4.add(txtidcli, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 50, -1));
        jPanel4.add(txttelefone, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 210, -1));

        boxsexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino\t", "Feminino" }));
        boxsexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxsexoActionPerformed(evt);
            }
        });
        jPanel4.add(boxsexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 230, 10));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 230, 400));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hamburgueriaCosta/icones/hamb.jpg"))); // NOI18N
        jLabel20.setText("jLabel20");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 510));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boxsexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxsexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxsexoActionPerformed

    private void boxperfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxperfilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxperfilActionPerformed

    private void btncriarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncriarActionPerformed
        // TODO add your handling code here:
        adicionarusuario();
        pegaridusuario();
        adicionarendereco();
        pegaridendereco();
        adicionarcliente();
        btnexcluir.setEnabled(false);
    }//GEN-LAST:event_btncriarActionPerformed

    private void btnprocurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprocurarActionPerformed
        // TODO add your handling code here:

        consultar();
    }//GEN-LAST:event_btnprocurarActionPerformed

    private void btneditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditarActionPerformed
        // TODO add your handling code here:
        alterarend();
        alterarusu();
        alterar();
        btnexcluir.setEnabled(false);
        btneditar.setEnabled(false);
    }//GEN-LAST:event_btneditarActionPerformed

    private void btnexcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexcluirActionPerformed
        // TODO add your handling code here:
        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este cliente ?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            remover();
            removerend();
            removerusu();
            btnexcluir.setEnabled(false);
            btneditar.setEnabled(false);
            btncriar.setEnabled(true);
        }
    }//GEN-LAST:event_btnexcluirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxperfil;
    private javax.swing.JComboBox<String> boxsexo;
    private javax.swing.JComboBox<String> boxtipo;
    private javax.swing.JButton btncriar;
    private javax.swing.JButton btneditar;
    private javax.swing.JButton btnexcluir;
    private javax.swing.JButton btnprocurar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField txtbairro;
    private javax.swing.JTextField txtcep;
    private javax.swing.JTextField txtcomplemento;
    private javax.swing.JTextField txtcpf;
    private javax.swing.JTextField txtidcli;
    private javax.swing.JTextField txtidend;
    private javax.swing.JTextField txtidusu;
    private javax.swing.JTextField txtlogin;
    private javax.swing.JTextField txtlogradouro;
    private javax.swing.JTextField txtnome;
    private javax.swing.JTextField txtnumero;
    private javax.swing.JPasswordField txtsenha;
    private javax.swing.JTextField txttelefone;
    // End of variables declaration//GEN-END:variables
}
