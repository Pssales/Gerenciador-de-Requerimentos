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

    //Cria variavel da classe Connection
    Connection con;

    //Instancia a conexão
    public AlunoDao() {
        con = ConnectionFactory.getConnection();
    }

    //Método responsável por gravar no banco de dados o objeto aluno passado por parameto. 
    public void create(Aluno a) {

        PreparedStatement stmt = null;

        try {
            //Query sql responsável pela inserção no banco.
            stmt = con.prepareStatement("INSERT INTO Aluno "
                    + "(nomeAluno, dataNascimento, telefone, rg,ra, email)VALUES(?,?,?,?,?,?)");

            //Preenche a query com os valores do aluno.
            stmt.setString(1, a.getNome());
            //Transforma variavel do tipo String em Date.
            try {
                stmt.setDate(2, formataData(a.getDataNascimento()));
            } catch (Exception ex) {
                Logger.getLogger(AlunoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getRg());
            stmt.setString(5, a.getRa());
            stmt.setString(6, a.getEmail());

            //Executa a query.
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            System.out.println(ex + "Erro");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    //Método responsável por ler os registros gravados no banco.
    public List<Aluno> read() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Lista responsável por armazenar os alunos 
        List<Aluno> alunos = new ArrayList<>();

        try {
            //Query sql responsável pela seleção dos dados do banco.
            stmt = con.prepareStatement("SELECT * FROM aluno");

            //Executa a query
            rs = stmt.executeQuery();//Recebe os resultados.

            //executa a sequência de comandos enquento a condição for verdadeira.
            while (rs.next()) {

                Aluno aluno = new Aluno();
                //Instancia o objeto aluno com os dados contidos na variavel rs
                aluno.setIdAluno(rs.getInt("idAluno"));
                aluno.setNome(rs.getString("nomeAluno"));
                aluno.setDataNascimento(rs.getString("dataNascimento"));
                aluno.setTelefone(rs.getString("telefone"));
                aluno.setRg(rs.getString("rg"));
                aluno.setRa(rs.getString("ra"));
                aluno.setEmail(rs.getString("Email"));

                //Adiciona os registros na lista
                alunos.add(aluno);
            }

        } catch (SQLException ex) {

            Logger.getLogger(AlunoDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return alunos;

    }

    //Método responsável por ler os registros gravados no banco que atendem a condição.
    public List<Aluno> readName(String name) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Lista resonsável por armazenar os resultados da pesquisa
        List<Aluno> alunos = new ArrayList<>();

        try {
            //Query responsável por seleciona os registos que atendem a condição.
            stmt = con.prepareStatement("SELECT * FROM aluno WHERE nomeAluno LIKE ?");

            //Passa a  condição para a query.
            stmt.setString(1, "%" + name + "%");

            //Executa a query.
            rs = stmt.executeQuery();//Recebe os resultados

            //executa a sequência de comandos enquento a condição for verdadeira.
            while (rs.next()) {

                Aluno aluno = new Aluno();

                //Instancia o objeto aluno com os dados contidos na variavel rs.
                aluno.setIdAluno(rs.getInt("idAluno"));
                aluno.setNome(rs.getString("nomeAluno"));
                aluno.setDataNascimento(rs.getString("dataNascimento"));
                aluno.setTelefone(rs.getString("telefone"));
                aluno.setRg(rs.getString("rg"));
                aluno.setRa(rs.getString("ra"));
                aluno.setEmail(rs.getString("Email"));

                //Adiciona os registros na lista
                alunos.add(aluno);
            }

        } catch (SQLException ex) {

            Logger.getLogger(AlunoDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return alunos;

    }

    //Método responsável por atualizar um registro no banco
    public void update(Aluno a) {

        PreparedStatement stmt = null;

        try {
            //Query responsável por fazer a atualização.
            stmt = con.prepareStatement("UPDATE aluno SET nomeAluno = ?, dataNascimento = ?, telefone =?, rg = ?,ra =?, email = ? WHERE idAluno = ?");

            //Preenche a query
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

    //Método responsável por deletar um registo do banco de dados
    public void delete(Aluno a) {

        PreparedStatement stmt = null;

        try {
            //Query responsável por fazer a remoção d rejistro na tabela aluno_disciplina.
            stmt = con.prepareStatement("DELETE FROM aluno_disciplina  WHERE idAluno=?");
            //Preenche a query.
            stmt.setInt(1, a.getIdAluno());
            //executa a querry.
            stmt.executeUpdate();

            //Query responsável por fazer a remoção d rejistro na tabela aluno.
            stmt = con.prepareStatement("DELETE FROM Aluno WHERE idAluno =?");

            //Preenche a query.
            stmt.setInt(1, a.getIdAluno());

            //executa a querry.
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não excluido!");
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    //Converte uma variavel em formato String em Date
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
