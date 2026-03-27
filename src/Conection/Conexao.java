package Conection;
import java.sql.Connection
import java.sql.DriverManager

public class Conexao {
    public static void main (String[] args){

        public url = "jdbc:postgresql://IP:5432/NomeBanco";
        String Usuario = "postgre";
        String Senha: "root";

        try{
            connection conn = DriverManager.getConnectio(url, Usuario,Senha);
            System.out.printnl("Conectado");
        }
        catch (exeptione){
            System.out.println("Erro"+e.get.menssage());
        }
    }
}
