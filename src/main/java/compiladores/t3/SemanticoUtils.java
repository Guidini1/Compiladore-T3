package compiladores.t3;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import compiladores.t3.AlgumaParser.Exp_aritmeticaContext;
import compiladores.t3.AlgumaParser.ExpressaoContext;
import compiladores.t3.AlgumaParser.FatorContext;
import compiladores.t3.AlgumaParser.Fator_logicoContext;
import compiladores.t3.AlgumaParser.ParcelaContext;
import compiladores.t3.AlgumaParser.TermoContext;
import compiladores.t3.AlgumaParser.Termo_logicoContext;
import java.util.Iterator;

public class SemanticoUtils {
    // Lista que guarda os erros semânticos
    public static List<String> errosSemanticos = new ArrayList<>();

    // Adiciona um erro semântico à lista e indica a linha onde foi encontrado
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e na expressão
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.ExpressaoContext ctx) {
        Tabela.Tipos tipoResultado = null;
        Iterator<Termo_logicoContext> iterador = ctx.termo_logico().iterator();
        while (iterador.hasNext()) {
            Termo_logicoContext termoLogico = iterador.next();
            Tabela.Tipos tipoAux = checarTipo(escopos, termoLogico);
            if (tipoResultado == null) {
                tipoResultado = tipoAux;
            } else if (tipoResultado != tipoAux && tipoAux != Tabela.Tipos.INVALIDO) {
                tipoResultado = Tabela.Tipos.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e no termo lógico
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.Termo_logicoContext ctx) {
        Tabela.Tipos tipoResultado = null;
        Iterator<Fator_logicoContext> iterador = ctx.fator_logico().iterator();
        while (iterador.hasNext()) {
            Fator_logicoContext fatorLogico = iterador.next();
            Tabela.Tipos tipoAux = checarTipo(escopos, fatorLogico);
            if (tipoResultado == null) {
                tipoResultado = tipoAux;
            } else if (tipoResultado != tipoAux && tipoAux != Tabela.Tipos.INVALIDO) {
                tipoResultado = Tabela.Tipos.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e no fator lógico
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.Fator_logicoContext ctx) {
        return checarTipo(escopos, ctx.parcela_logica());
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e na parcela lógica
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.Parcela_logicaContext ctx) {
        Tabela.Tipos tipoResultado = null;
        if (ctx.exp_relacional() != null) {
            tipoResultado = checarTipo(escopos, ctx.exp_relacional());
        } else {
            tipoResultado = Tabela.Tipos.LOGICO;
        }
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e na expressão relacional
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.Exp_relacionalContext ctx) {
        Tabela.Tipos tipoResultado = null;
        if (ctx.op_relacional() != null) {
            Iterator<Exp_aritmeticaContext> iterador = ctx.exp_aritmetica().iterator();
            while (iterador.hasNext()) {
                Exp_aritmeticaContext expAritmetica = iterador.next();
                Tabela.Tipos tipoAux = checarTipo(escopos, expAritmetica);
                Boolean auxNumerico = tipoAux == Tabela.Tipos.REAL || tipoAux == Tabela.Tipos.INT;
                Boolean resultadoNumerico = tipoResultado == Tabela.Tipos.REAL || tipoResultado == Tabela.Tipos.INT;
                if (tipoResultado == null) {
                    tipoResultado = tipoAux;
                } else if (!(auxNumerico && resultadoNumerico) && tipoAux != tipoResultado) {
                    tipoResultado = Tabela.Tipos.INVALIDO;
                }
            }

            if (tipoResultado != Tabela.Tipos.INVALIDO) {
                tipoResultado = Tabela.Tipos.LOGICO;
            }
        } else {
            tipoResultado = checarTipo(escopos, ctx.exp_aritmetica(0));
        }
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e na expressão aritmética
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.Exp_aritmeticaContext ctx) {
        Tabela.Tipos tipoResultado = null;
        Iterator<TermoContext> iterador = ctx.termo().iterator();
        while (iterador.hasNext()) {
            TermoContext termo = iterador.next();
            Tabela.Tipos tipoAux = checarTipo(escopos, termo);
            if (tipoResultado == null) {
                tipoResultado = tipoAux;
            } else if (tipoResultado != tipoAux && tipoAux != Tabela.Tipos.INVALIDO) {
                tipoResultado = Tabela.Tipos.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e no termo
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.TermoContext ctx) {
        Tabela.Tipos tipoResultado = null;

        Iterator<FatorContext> iterador = ctx.fator().iterator();
        while (iterador.hasNext()) {
            FatorContext fator = iterador.next();
            Tabela.Tipos tipoAux = checarTipo(escopos, fator);
            Boolean auxNumerico = tipoAux == Tabela.Tipos.REAL || tipoAux == Tabela.Tipos.INT;
            Boolean resultadoNumerico = tipoResultado == Tabela.Tipos.REAL || tipoResultado == Tabela.Tipos.INT;
            if (tipoResultado == null) {
                tipoResultado = tipoAux;
            } else if (!(auxNumerico && resultadoNumerico) && tipoAux != tipoResultado) {
                tipoResultado = Tabela.Tipos.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e no fator
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.FatorContext ctx) {
        Tabela.Tipos tipoResultado = null;

        Iterator<ParcelaContext> iterador = ctx.parcela().iterator();
        while (iterador.hasNext()) {
            ParcelaContext parcela = iterador.next();
            Tabela.Tipos tipoAux = checarTipo(escopos, parcela);
            if (tipoResultado == null) {
                tipoResultado = tipoAux;
            } else if (tipoResultado != tipoAux && tipoAux != Tabela.Tipos.INVALIDO) {
                tipoResultado = Tabela.Tipos.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e na parcela
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.ParcelaContext ctx) {
        Tabela.Tipos tipoResultado = Tabela.Tipos.INVALIDO;

        if (ctx.parcela_nao_unario() != null) {
            tipoResultado = checarTipo(escopos, ctx.parcela_nao_unario());
        } else {
            tipoResultado = checarTipo(escopos, ctx.parcela_unario());
        }
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e na parcela não unária
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.Parcela_nao_unarioContext ctx) {
        if (ctx.identificador() != null) {
            return checarTipo(escopos, ctx.identificador());
        }
        return Tabela.Tipos.CADEIA;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e no identificador
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.IdentificadorContext ctx) {
        String nomeVar = "";
        Tabela.Tipos tipoResultado = Tabela.Tipos.INVALIDO;
        for (int i = 0; i < ctx.IDENT().size(); i++) {
            nomeVar += ctx.IDENT(i).getText();
            if (i != ctx.IDENT().size() - 1) {
                nomeVar += ".";
            }
        }
        Iterator<Tabela> iterador = escopos.recuperarTodosEscopos().iterator();
        while (iterador.hasNext()) {
            Tabela tabela = iterador.next();
            if (tabela.existe(nomeVar)) {
                tipoResultado = checarTipo(escopos, nomeVar);
            }
        }
        System.out.println(nomeVar);
        return tipoResultado;
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e na parcela unária
    public static Tabela.Tipos checarTipo(Escopo escopos, AlgumaParser.Parcela_unarioContext ctx) {
        if (ctx.NUM_INT() != null) {
            return Tabela.Tipos.INT;
        }
        if (ctx.NUM_REAL() != null) {
            return Tabela.Tipos.REAL;
        }
        if (ctx.identificador() != null) {
            return checarTipo(escopos, ctx.identificador());
        }
        if (ctx.IDENT() != null) {
            Tabela.Tipos tipoResultado = null;
            tipoResultado = checarTipo(escopos, ctx.IDENT().getText());
            Iterator<ExpressaoContext> iterador = ctx.expressao().iterator();
            while (iterador.hasNext()) {
                ExpressaoContext expressao = iterador.next();
                Tabela.Tipos tipoAux = checarTipo(escopos, expressao);
                if (tipoResultado == null) {
                    tipoResultado = tipoAux;
                } else if (tipoResultado != tipoAux && tipoAux != Tabela.Tipos.INVALIDO) {
                    tipoResultado = Tabela.Tipos.INVALIDO;
                }
            }
            return tipoResultado;
        } else {
            Tabela.Tipos tipoResultado = null;
            Iterator<ExpressaoContext> iterador = ctx.expressao().iterator();
            while (iterador.hasNext()) {
                ExpressaoContext expressao = iterador.next();
                Tabela.Tipos tipoAux = checarTipo(escopos, expressao);
                if (tipoResultado == null) {
                    tipoResultado = tipoAux;
                } else if (tipoResultado != tipoAux && tipoAux != Tabela.Tipos.INVALIDO) {
                    tipoResultado = Tabela.Tipos.INVALIDO;
                }
            }
            return tipoResultado;
        }
    }

    // Verifica o tipo de um símbolo na tabela de símbolos com base no escopo e no nome da variável
    public static Tabela.Tipos checarTipo(Escopo escopos, String nomeVar) {
        Tabela.Tipos tipo = null;
        Iterator<Tabela> iterador = escopos.recuperarTodosEscopos().iterator();
        while (iterador.hasNext()) {
            Tabela tabela = iterador.next();
            tipo = tabela.verifica(nomeVar);
        }
        return tipo;
    }
}
