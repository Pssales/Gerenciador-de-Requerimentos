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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.bean.Aluno;
import model.bean.Disciplina;

/**
 *
 * @author a1600222
 */
public class AlunoDisciplinaDao {
    Connection con;

    public AlunoDisciplinaDao() {
        con = ConnectionFactory.getConnection();
    }
    
    public void create(Aluno a, Disciplina d) {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM aluno WHERE nomeAluno = ?");
            rs = stmt.executeQuery();
            
            stmt = con.prepareStatement("INSERT INTO aluno_disciplina (idAluno, idDisciplina)VALUES(?,?)");
            stmt.setInt(1, rs.getInt("idAluno"));
            stmt.setInt(2, d.getIdDisciplina());
            
            stmt.executeUpdate();
                        

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex + "erro");
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
            stmt = con.prepareStatement("UPDATE aluno_disciplina SET idAluno = ?, idDisciplina = ? WHERE idAluno = ?");
            
            stmt.setInt(1, a.getIdAluno());
            stmt.setInt(2, a.getDisciplina().getIdDisciplina());
          
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
            stmt = con.prepareStatement("DELETE FROM aluno_disciplina  WHERE idAluno=?");
           
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
}
