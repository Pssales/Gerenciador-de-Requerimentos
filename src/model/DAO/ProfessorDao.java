/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.DAO;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.bean.Professor;

/**
 *
 * @author Camila
 */
public class ProfessorDao {

    //Cria variavel da classe Connection
    Connection con;

    public ProfessorDao() {
        //Instancia a conexão
        con = ConnectionFactory.getConnection();
    }

    //Método responsável por inserir um aluno no banco
    public void create(Professor a) {

        PreparedStatement stmt = null;

        try {
            //Query responsável pela inserção do registro.
            stmt = con.prepareStatement("INSERT INTO professor "
                    + "(nomeProfessor, dataNascimento, telefone, rg, email)VALUES(?,?,?,?,?)");
            //Prenche a query
            stmt.setString(1, a.getNome());
            try {
                stmt.setDate(2, formataData(a.getDataNascimento()));
            } catch (Exception ex) {
                Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getRg());
            stmt.setString(5, a.getEmail());
            
            //Executa a query
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex +"merda");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    //Método responsável por buscar registros na tabela
    public List<Professor> read() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Lista para armazenar os registros
        List<Professor> prof = new ArrayList<>();

        try {
            //Query responsavel pela busca
            stmt = con.prepareStatement("SELECT * FROM professor;");
            
            //Executa a query
            rs = stmt.executeQuery();

            //Executa uma sequencia de comandos enquanto a condição é atendida
            while (rs.next()) {
                
                Professor professor = new Professor();
                
                //Popula o objeto
                professor.setIdProfessor(rs.getInt("idProfessor"));
                professor.setNome(rs.getString("nomeProfessor"));
                professor.setDataNascimento(rs.getString("dataNascimento"));
                professor.setTelefone(rs.getString("telefone"));
                professor.setRg(rs.getString("rg"));
                professor.setEmail(rs.getString("Email"));
                
                //Adiciona o objeto na lista
                prof.add(professor);
            }

        } catch (SQLException ex) {

            Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return prof;

    }

    //Método responsável por buscar registros na tabela que atendem a condição
    public List<Professor> readName(String name) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Lista reponsável por armazenar os registros
        List<Professor> prof = new ArrayList<>();

        try {
            
            //Query responsavel por selecionar os registros que atendem a condição
            stmt = con.prepareStatement("SELECT * FROM professor WHERE nomeProfessor LIKE ?;");
            
            //Popula a query
            stmt.setString(1, "%" + name + "%");
            
            //Executa a query
            rs = stmt.executeQuery();

            //Executa uma sequenciade comandos enquanto a condição for verdadeira
            while (rs.next()) {

                Professor professor = new Professor();
                
                //Instância o objeto com os dados da pesquisa
                professor.setNome(rs.getString("nomeProfessor"));
                professor.setDataNascimento(rs.getString("dataNascimento"));
                professor.setTelefone(rs.getString("telefone"));
                professor.setRg(rs.getString("rg"));
                professor.setEmail(rs.getString("Email"));
                
                //Adiciona o registro na lista
                prof.add(professor);
            }

        } catch (SQLException ex) {

            Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return prof;

    }

    //Método responsável por atualizar um registro
    public void update(Professor p) {

        PreparedStatement stmt = null;

        try {
            //Query responsável por atualizar um registro no banco
            stmt = con.prepareStatement("UPDATE professor "
                    + "SET nomeProfessor = ?, dataNascimento = ?, telefone =?, rg= ?, email= ? WHERE idProfessor=?");
            
            //preenche a query
            stmt.setString(1, p.getNome());
            try {
                stmt.setDate(2, formataData(p.getDataNascimento()));
            } catch (Exception ex) {
                Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            stmt.setString(3, p.getTelefone());
            stmt.setString(4, p.getRg());
            stmt.setString(5, p.getEmail());
            stmt.setInt(6, p.getIdProfessor());
            
            //Executa a query
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Atualizado!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    //Método responsável por deletar um registro
    public void delete(Professor p) {

        PreparedStatement stmt = null;

        try {
            //Query responsavel por deletar um registro
            stmt = con.prepareStatement("DELETE FROM professor  WHERE idProfessor=?");
            
            //Preenche a query
            stmt.setInt(1, p.getIdProfessor());
            
            //Executa a query
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não excluido!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    //Converte uma String em Date
    public static java.sql.Date formataData(String data) throws Exception {
        if (data == null || data.equals("")) {
            return null;
        }
        java.sql.Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = new java.sql.Date(((java.util.Date) formatter.parse(data)).getTime());
        } catch (ParseException e) {
            throw e;
        }
        return date;
    }
}
