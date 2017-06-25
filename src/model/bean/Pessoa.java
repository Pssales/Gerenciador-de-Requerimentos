package model.bean;

import java.sql.Date;

public class Pessoa {

    protected String nome;
    protected String sobrenome;
    protected String dataNascimento;
    protected String telefone;
    protected String rg;
    protected String email;

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getRg() {
        return rg;
    }

    public String getEmail() {
        return email;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

}
