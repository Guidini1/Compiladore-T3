package compiladores.t3;

import java.util.LinkedList;

public class Escopo {
    // A pilha que contém todas as tabelas de símbolos (escopos)
    private LinkedList<Tabela> pilhaDeTabelas;

    // Construtor que inicializa a pilha e cria o primeiro escopo
    public Escopo() {
        pilhaDeTabelas = new LinkedList<Tabela>();
        criarNovoEscopo();
    }

    // Cria um novo escopo (tabela de símbolos) e adiciona à pilha
    public void criarNovoEscopo() {
        pilhaDeTabelas.push(new Tabela());
    }

    // Remove o escopo atual (o topo da pilha)
    public void removerEscopo() {
        pilhaDeTabelas.pop();
    }

    // Retorna o escopo atual (o topo da pilha) sem removê-lo
    public Tabela escopoAtual() {
        return pilhaDeTabelas.peek();
    }

    // Retorna a pilha de todos os escopos
    public LinkedList<Tabela> recuperarTodosEscopos() {
        return pilhaDeTabelas;
    }
}
