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
import model.bean.Aluno;

/**
 *
 * @author Camila
 */
public class AlunoDao {

    Connection con;

    public AlunoDao() {
        con = ConnectionFactory.getConnection();
    }

    public void create(Aluno a) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO Aluno (nomeAluno, dataNascimento, telefone, rg,ra, email)VALUES(?,?,?,?,?,?)");

            stmt.setString(1, a.getNome());
            try {
                stmt.setDate(2, formataData(a.getDataNascimento()));
            } catch (Exception ex) {
                Logger.getLogger(AlunoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getRg());
            stmt.setString(5, a.getRa());
            stmt.setString(6, a.getEmail());
            
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex + "merda");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public List<Aluno> read() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Aluno> alunos = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM aluno");
            rs = stmt.executeQuery();

            while (rs.next()) {

                Aluno aluno = new Aluno();

                aluno.setIdAluno(rs.getInt("idAluno"));
                aluno.setNome(rs.getString("nomeAluno"));
                aluno.setDataNascimento(rs.getString("dataNascimento"));
                aluno.setTelefone(rs.getString("telefone"));
                aluno.setRg(rs.getString("rg"));
                aluno.setRa(rs.getString("ra"));
                aluno.setEmail(rs.getString("Email"));
                alunos.add(aluno);
            }

        } catch (SQLException ex) {

            Logger.getLogger(AlunoDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return alunos;

    }

    public void update(Aluno a) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE aluno SET nomeAluno = ?, dataNascimento = ?, telefone =?, rg = ?,ra =?, email = ? WHERE idAluno = ?");

            stmt.setString(1, a.getNome());
            try {
                stmt.setDate(2, formataData(a.getDataNascimento()));
            } catch (Exception ex) {
                Logger.getLogger(AlunoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getRg());
            stmt.setString(5, a.getRa());
            stmt.setString(6, a.getEmail());
            stmt.setInt(7, a.getIdAluno());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Atualizado!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public void delete(Aluno a) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM aluno  WHERE idAluno=?");
           
            stmt.setInt(1, a.getIdAluno());
            
            stmt.executeUpdate();
            
            stmt = con.prepareStatement("DELETE FROM Aluno WHERE idAluno =?");
            
            stmt.setInt(1, a.getIdAluno());
            
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
