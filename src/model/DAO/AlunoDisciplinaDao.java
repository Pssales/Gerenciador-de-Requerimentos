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
import model.bean.AlunoDisciplina;
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

            stmt = con.prepareStatement("INSERT INTO aluno_disciplina (idAluno, idDisciplina)VALUES(?,?)");
            stmt.setInt(1, a.getIdAluno());
            stmt.setInt(2, d.getIdDisciplina());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex + "erro");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public List<AlunoDisciplina> read() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<AlunoDisciplina> alunosDisciplinas = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT idAluno,nomeAluno,idDisciplina,nomeDisciplina FROM aluno NATURAL JOIN  aluno_disciplina NATURAL JOIN disciplina;");
            rs = stmt.executeQuery();

            while (rs.next()) {

                AlunoDisciplina ad = new AlunoDisciplina();
                
                Aluno a = new Aluno();
                a.setIdAluno(rs.getInt("idAluno"));
                a.setNome(rs.getString("nomeAluno"));
                ad.setAluno(a);
                
                Disciplina d= new Disciplina();
                d.setIdDisciplina(rs.getInt("idDisciplina"));
                d.setNomeDisciplina(rs.getString("nomeDisciplina"));
                ad.setDisciplina(d);

                alunosDisciplinas.add(ad);
            }

        } catch (SQLException ex) {

            Logger.getLogger(AlunoDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return alunosDisciplinas;

    }

    public void update(AlunoDisciplina ad) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE aluno_disciplina SET idAluno = ?, idDisciplina = ? WHERE idAluno = ? ");

            stmt.setInt(1, ad.getAluno().getIdAluno());
            stmt.setInt(2, ad.getDisciplina().getIdDisciplina());
            stmt.setInt(3, ad.getAluno().getIdAluno());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Atualizado!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public void delete(AlunoDisciplina ad) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM aluno_disciplina  WHERE idAluno=? AND idDisciplina=? ");

            stmt.setInt(1, ad.getAluno().getIdAluno());
            stmt.setInt(2, ad.getDisciplina().getIdDisciplina());
            
            System.out.println(stmt);
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
