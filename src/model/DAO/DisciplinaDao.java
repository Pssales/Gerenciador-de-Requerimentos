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
import model.bean.Professor;

/**
 *
 * @author Camila
 */
public class DisciplinaDao {

    //Cria uma variavel da classe Connection
    Connection con;

    //Instancia a conexão
    public DisciplinaDao() {
        con = ConnectionFactory.getConnection();
    }
    
    //Método responsável por inserir um registro no banco
    public void create(Disciplina dis) {
        PreparedStatement stmt = null;

        try {
            //Query responsavel pela inserção dos registros
            stmt = con.prepareStatement("INSERT INTO disciplina "
                    + "(nomeDisciplina, descricao,idProfessor)VALUES(?,?,?)");

            stmt.setString(1, dis.getNomeDisciplina());
            stmt.setString(2, dis.getDescricao());
            stmt.setInt(3, dis.getProfessor().getIdProfessor());

            //executa o comando
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex + "merda");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    //Método responsável por ler os registros gravados no banco que atendem a condição.
    public List<Disciplina> read() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Lista responsável por armazenar os registros
        List<Disciplina> disciplinas = new ArrayList<>();

        try {
            //Query responsavel por fazer a busca no banco.
            stmt = con.prepareStatement("SELECT idDisciplina,nomeDisciplina,descricao,idProfessor,nomeProfessor "
                    + "FROM disciplina NATURAL JOIN professor");
           
            //Executa a query
            rs = stmt.executeQuery();

            //Executa uma sequencia de comando enquanto a condição for verdadeira
            while (rs.next()) {

                Disciplina dis = new Disciplina();
                
                //Instancia o objeto com o resultado da busca
                dis.setIdDisciplina(rs.getInt("idDisciplina"));
                dis.setNomeDisciplina(rs.getString("nomeDisciplina"));
                dis.setDescricao(rs.getString("descricao"));

                Professor professor = new Professor();
                                //Instancia o objeto com o resultado da busca

                professor.setIdProfessor(rs.getInt("idProfessor"));
                professor.setNome(rs.getString("nomeProfessor"));
                dis.setProfessor(professor);
                
                //Insere o objeto na lista
                disciplinas.add(dis);
            }

        } catch (SQLException ex) {

            Logger.getLogger(DisciplinaDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return disciplinas;

    }

    //Método responsável por ler os registros gravados no banco.
    public List<Disciplina> readName(String name) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Lista responsável por armazenar as disciplinas.
        List<Disciplina> disciplinas = new ArrayList<>();

        try {
            //Query responsável por seleciona os registos que atendem a condição.
            stmt = con.prepareStatement("SELECT idDisciplina,nomeDisciplina,descricao,nomeProfessor FROM disciplina NATURAL JOIN professor WHERE nomeDisciplina LIKE ?");

            //Passa a  condição para a query.
            stmt.setString(1, "%" + name + "%");

            //Executa a query
            rs = stmt.executeQuery();//Armazena os rezultados na variavel rs

            //executa a sequência de comandos enquento a condição for verdadeira.
            while (rs.next()) {

                Disciplina dis = new Disciplina();

                //Preenche o objeto com os dados da busca
                dis.setIdDisciplina(rs.getInt("idDisciplina"));
                dis.setNomeDisciplina(rs.getString("nomeDisciplina"));
                dis.setDescricao(rs.getString("descricao"));

                Professor professor = new Professor();

                //Preenche o objeto com os dados da busca
                professor.setIdProfessor(rs.getInt("idProfessor"));
                professor.setNome(rs.getString("nomeProfessor"));
                dis.setProfessor(professor);

                //Armazena os dados na lista
                disciplinas.add(dis);
            }

        } catch (SQLException ex) {

            Logger.getLogger(DisciplinaDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return disciplinas;

    }

    //Métpdo responsável por atualizar um registro
    public void update(Disciplina dis) {

        PreparedStatement stmt = null;

        try {
            //Query responsável por atualizar um registro
            stmt = con.prepareStatement("UPDATE disciplina SET "
                    + "nomeDisciplina = ?, descricao = ?, idProfessor = ? WHERE idDisciplina = ?");

            //Preenche a query
            stmt.setString(1, dis.getNomeDisciplina());
            stmt.setString(2, dis.getDescricao());
            stmt.setInt(3, dis.getProfessor().getIdProfessor());
            stmt.setInt(4, dis.getIdDisciplina());

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

    //Método responsavel por apagar um registro
    public void delete(Disciplina dis) {

        PreparedStatement stmt = null;

        try {
            //Query resposavel por apagar um registro da tabela aluno-disciplina
            stmt = con.prepareStatement("DELETE FROM aluno_disciplina WHERE idDisciplina=?");

            //Preenche a query
            stmt.setInt(1, dis.getIdDisciplina());

            //Executa a query
            stmt.executeUpdate();

            //Query resposavel por apagar um registro da tabela disciplina
            stmt = con.prepareStatement("DELETE FROM disciplina  WHERE idDisciplina=?");
            
            //Preenche a query
            stmt.setInt(1, dis.getIdDisciplina());

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
}
