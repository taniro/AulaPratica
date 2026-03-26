package ufrn.br.aulapratica.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufrn.br.aulapratica.model.Tarefa;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NegociacaoController {

    private static final List<Tarefa> tarefas = new ArrayList<>();

    static {
        tarefas.add(new Tarefa("Configurar Banco de Dados"));
        tarefas.add(new Tarefa("Implementar Login"));
    }

    @RequestMapping("/api/tarefas")
    public void getTarefas(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var userAgent = request.getHeader("User-Agent");

        if (userAgent.contains("Mozilla") || userAgent == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("Acesso bloqueado. Use o  Insomnia.");
            return;
        }

        var accept = request.getHeader("Accept");
        if (accept == null) {
            accept = "*/*";
        }

        if (accept.contains("application/json")) {
            enviarJson(response);
        } else if (accept.contains("text/html")) {
            enviarHtml(response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().print("Suportamos apenas HTML e JSON");
        }
    }

    private void enviarJson(HttpServletResponse res) throws IOException {
        // A REGRA DE OURO: Avisar qual é o formato!
        res.setContentType("application/json");

        PrintWriter out = res.getWriter();
        out.print("["); // Abre o Array

        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa t = tarefas.get(i);
            // Atenção absoluta com as aspas duplas escapadas (\")
            out.print("{\"id\": " + t.id() + ", \"titulo\": \"" + t.texto() + "\"}");

            if (i < tarefas.size() - 1) {
                out.print(", ");
            } // Vírgula, exceto no último
        }

        out.print("]"); // Fecha o Array
    }

    private void enviarHtml(HttpServletResponse res) throws IOException {
        res.setContentType("text/html");

        PrintWriter out = res.getWriter();
        out.println("<html><body>");
        out.print("<ul>"); // Abre o Array

        for (Tarefa t : tarefas) {
            out.print("<li>" + t.id() + ", titulo: " + t.texto() + "</li>");
        }

        out.print("</ul>"); // Fecha o Array
        out.println("</body></html>");
    }

}
