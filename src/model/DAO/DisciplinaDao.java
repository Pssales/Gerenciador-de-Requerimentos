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
import model.bean.Disciplina;

/**
 *
 * @author Camila
 */
public class DisciplinaDao {
    Connection con;
    
    public DisciplinaDao() {
        con = ConnectionFactory.getConnection();
    }
    public void create(Disciplina dis){
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO disciplina (nomeDisciplina, descricao)VALUES(?,?)");

            stmt.setString(1, dis.getNomeDisciplina());
            stmt.setString(2, dis.getDescricao());
           
            //executa o comando
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex + "merda");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public List<Disciplina> read() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Disciplina> disciplinas = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM disciplina");
            rs = stmt.executeQuery();

            while (rs.next()) {

                Disciplina dis = new Disciplina();

                dis.setIdDisciplina(rs.getInt("idDisciplina"));
                dis.setNomeDisciplina(rs.getString("nomeDisciplina"));
                dis.setDescricao(rs.getString("descricao"));
                disciplinas.add(dis);
            }

        } catch (SQLException ex) {

            Logger.getLogger(DisciplinaDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return disciplinas;

    }
    public List<Disciplina> readName(String name) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Disciplina> disciplinas = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM disciplina WHERE nomeDisciplina LIKE ?");
            stmt.setString(1, "%"+name+"%");
            rs = stmt.executeQuery();

            while (rs.next()) {

                Disciplina dis = new Disciplina();

                dis.setIdDisciplina(rs.getInt("idDisciplina"));
                dis.setNomeDisciplina(rs.getString("nomeDisciplina"));
                dis.setDescricao(rs.getString("descricao"));
                disciplinas.add(dis);
            }

        } catch (SQLException ex) {

            Logger.getLogger(DisciplinaDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return disciplinas;

    }
    
    public void update(Disciplina dis) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE disciplina SET nomeDisciplina = ?, descricao = ? WHERE idDisciplina = ?");

            stmt.setString(1, dis.getNomeDisciplina());
            stmt.setString(2, dis.getDescricao());
            stmt.setInt(3, dis.getIdDisciplina());
            
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não Atualizado!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public void delete(Disciplina dis) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM disciplina  WHERE idDisciplina=?");
           
            stmt.setInt(1, dis.getIdDisciplina());
            
            stmt.executeUpdate();
            
            stmt = con.prepareStatement("DELETE FROM Aluno WHERE idDisciplina=?");
            
            JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não excluido!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
}
