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

    Connection con;

    public ProfessorDao() {
        con = ConnectionFactory.getConnection();
    }

    public void create(Professor a) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO professor (nomeProfessor, dataNascimento, telefone, rg, email)VALUES(?,?,?,?,?)");
            stmt.setString(1, a.getNome());
            try {
                stmt.setDate(2, formataData(a.getDataNascimento()));
            } catch (Exception ex) {
                Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getRg());
            stmt.setString(5, a.getEmail());
            System.out.println(stmt);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex +"merda");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public List<Professor> read() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Professor> prof = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM professor;");
            rs = stmt.executeQuery();

            while (rs.next()) {
                
                Professor professor = new Professor();
                professor.setIdProfessor(rs.getInt("idProfessor"));
                professor.setNome(rs.getString("nomeProfessor"));
                professor.setDataNascimento(rs.getString("dataNascimento"));
                professor.setTelefone(rs.getString("telefone"));
                professor.setRg(rs.getString("rg"));
                professor.setEmail(rs.getString("Email"));
                prof.add(professor);
            }

        } catch (SQLException ex) {

            Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return prof;

    }

    public List<Professor> readName(String name) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Professor> prof = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM professor WHERE nomeProfessor LIKE ?;");
            stmt.setString(1, "%" + name + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {

                Professor professor = new Professor();
                professor.setNome(rs.getString("nomeProfessor"));
                professor.setDataNascimento(rs.getString("dataNascimento"));
                professor.setTelefone(rs.getString("telefone"));
                professor.setRg(rs.getString("rg"));
                professor.setEmail(rs.getString("Email"));
                prof.add(professor);
            }

        } catch (SQLException ex) {

            Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return prof;

    }

    public void update(Professor p) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE professor SET nomeProfessor = ?, dataNascimento = ?, telefone =?, rg= ?, email= ? WHERE idProfessor=?");

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
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Atualizado!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public void delete(Professor p) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM pessoa  WHERE idProfessor=?");
            stmt.setInt(1, p.getIdProfessor());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não excluido!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

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
