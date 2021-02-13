/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hamburgueriaCosta.dal;
import java.sql.*;

/**
 *
 * @author gabriel
 */
public class ModuloConexao {
    //método responsavel por estabelecer a conexão com o banco
    public static Connection conector(){
        java.sql.Connection conexao = null;
        // a linha abaixo "chama" o driver
        String driver = "com.mysql.jdbc.Driver";
        // Armazenando informações referente ao banco
        String url="jdbc:mysql://localhost:3306/dbhamburgueria";
        String user="root";
        String password = "";
        // Estabelecendo a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            // a linha abaixo serve para esclarecer o erro
            //System.out.println(e);
            return null;
        }
    }
}
