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

    //Cria variavel da classe Connection
    Connection con;

    //Instancia a conexão
    public AlunoDisciplinaDao() {
        con = ConnectionFactory.getConnection();
    }

    //Método responsável pela criação de um registro
    public void create(Aluno a, Disciplina d) {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            //Query responsável pela inserção 
            stmt = con.prepareStatement("INSERT INTO aluno_disciplina "
                    + "(idAluno, idDisciplina)VALUES(?,?)");
            //Preenche a query
            stmt.setInt(1, a.getIdAluno());
            stmt.setInt(2, d.getIdDisciplina());

            //Executa a query
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex + "erro");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    //Método responsável por ler os registros gravados no banco.
    public List<AlunoDisciplina> read() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        //Lista responsável por armazenar os realacionamento aluno_disciplina.
        List<AlunoDisciplina> alunosDisciplinas = new ArrayList<>();

        try {
            //Query responsavel por fazer a busca no banco.
            stmt = con.prepareStatement("SELECT idAluno,nomeAluno,idDisciplina,nomeDisciplina "
                    + "FROM aluno NATURAL JOIN  aluno_disciplina NATURAL JOIN disciplina;");
            //Executa a query
            rs = stmt.executeQuery();//armazena o resultado da busca na variável rs;

            //Executa a sequência de comandos enquento a condição for verdadeira.
            while (rs.next()) {

                AlunoDisciplina ad = new AlunoDisciplina();
                
                //Preenche o objeto com os dados da busca
                Aluno a = new Aluno();
                a.setIdAluno(rs.getInt("idAluno"));
                a.setNome(rs.getString("nomeAluno"));
                ad.setAluno(a);
                
                //Preenche o objeto com os dados da busca
                Disciplina d = new Disciplina();
                d.setIdDisciplina(rs.getInt("idDisciplina"));
                d.setNomeDisciplina(rs.getString("nomeDisciplina"));
                ad.setDisciplina(d);
                
                //Adiciona os resultados na lista
                alunosDisciplinas.add(ad);
            }

        } catch (SQLException ex) {

            Logger.getLogger(AlunoDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return alunosDisciplinas;

    }

    //Método responsável por ler os registros gravados no banco que atendem a condição.
    public void update(AlunoDisciplina ad) {

        PreparedStatement stmt = null;

        try {
            //Query responsável por atualizar o registro
            stmt = con.prepareStatement("UPDATE aluno_disciplina SET "
                    + "idAluno = ?, idDisciplina = ? WHERE idAluno = ? ");

            //Preenche os dados da query.
            stmt.setInt(1, ad.getAluno().getIdAluno());
            stmt.setInt(2, ad.getDisciplina().getIdDisciplina());
            stmt.setInt(3, ad.getAluno().getIdAluno());

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

    //Método resposável por apagar um registro do banco
    public void delete(AlunoDisciplina ad) {

        PreparedStatement stmt = null;

        try {
            //Query responsável por apagar um registro
            stmt = con.prepareStatement("DELETE FROM aluno_disciplina  "
                    + "WHERE idAluno=? AND idDisciplina=? ");

            //Preenche a query 
            stmt.setInt(1, ad.getAluno().getIdAluno());
            stmt.setInt(2, ad.getDisciplina().getIdDisciplina());

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
