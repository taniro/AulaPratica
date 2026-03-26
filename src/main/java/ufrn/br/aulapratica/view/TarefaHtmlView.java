package ufrn.br.aulapratica.view;

import ufrn.br.aulapratica.model.Tarefa;

import java.util.List;

public class TarefaHtmlView {

    public static String renderizarLista(List<Tarefa> tarefas, String erro) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Dashboard</title></head><body>");
        html.append("<h1>Sistema de Demandas</h1>");

        if (erro != null) {
            html.append("<p style='color: red;'>Erro: ").append(erro).append("</p>");
        }

        html.append("<form method='POST' action='/tarefas'>");
        html.append("Título: <input type='text' name='texto' required> ");
        html.append("<button type='submit'>Salvar</button>");
        html.append("</form><hr><ul>");

        for (Tarefa t : tarefas) {
            html.append("<li>[").append(t.id()).append("] ").append(t.texto()).append("</li>");
        }

        html.append("</ul></body></html>");
        return html.toString();
    }
}