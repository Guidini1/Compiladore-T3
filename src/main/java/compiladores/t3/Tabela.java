package compiladores.t3;

import java.util.HashMap;

public class Tabela {

    // Enumeração dos tipos possíveis usados na tabela de símbolos
    public enum Tipos {
        INT, REAL, CADEIA, LOGICO, INVALIDO, TIPO, IDENT
    }

    // Classe interna para representar uma entrada na tabela de símbolos
    class EntradaTabelaDeSimbolos {
        String nome;   // Nome do símbolo
        Tipos tipo;    // Tipo do símbolo

        // Construtor privado para criar uma nova entrada na tabela
        private EntradaTabelaDeSimbolos(String nome, Tipos tipo) {
            this.nome = nome;
            this.tipo = tipo;
        }
    }

    private HashMap<String, EntradaTabelaDeSimbolos> myTable; // Tabela de símbolos armazenada em um HashMap

    // Construtor da classe Tabela, inicializa o HashMap
    public Tabela() {
        myTable = new HashMap<>();
    }

    // Verifica se um símbolo está presente na tabela
    public boolean existe(String nome) {
        return myTable.containsKey(nome);
    }

    // Retorna o tipo associado a um símbolo
    public Tipos verifica(String nome) {
        return myTable.get(nome).tipo;
    }

    // Adiciona um novo símbolo com seu tipo na tabela
    public void adiciona(String nome, Tipos tipo) {
        EntradaTabelaDeSimbolos entrada = new EntradaTabelaDeSimbolos(nome, tipo);
        myTable.put(nome, entrada);
    }
}
