package ufrn.br.aulapratica.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FundamentosHttpController {

    List<String> tarefas = new ArrayList<>();

    @RequestMapping("/fundamentos")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Extraindo dados da Start Line HTTP
        System.out.println("Método: " + request.getMethod()); // GET, POST...
        System.out.println("URI: " + request.getRequestURI()); // /caminho

        // ... continuação do método ...
        // Definindo Cabeçalhos (Headers) de resposta
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        // Abrindo o duto de rede para escrever o Corpo (Body)
        PrintWriter writer = response.getWriter();
        writer.println("Requisição processada com sucesso no servidor!");

        // Empurra os bytes e fecha a conexão
        writer.flush();
        writer.close();

    }

    @RequestMapping("/tarefas")
    public void gerenciarRotas(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String metodo = request.getMethod();

        if (metodo.equals("GET")){
            processarGet(request, response);
        } else if (metodo.equals("POST")) {
            processarPost(request, response);
        }else{
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

    }

    private void processarGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var writer = response.getWriter();
        writer.println("<html><body><ul>");
        for ( String s : tarefas){
            writer.println("<li>" + s + "</li>");
        }
        writer.println("</ul> </body> </html>");

    }

    private void processarPost(HttpServletRequest req, HttpServletResponse res) {
        // O parâmetro "titulo" vem da view HTML
        String titulo = req.getParameter("titulo");

        if (titulo == null || titulo.trim().isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            return; // Aborta o fluxo
        }
        // Lógica Omitida: salvar a tarefa na memória
        tarefas.add(titulo);

        // Define status 303 (Redirecionamento)
        res.setStatus(HttpServletResponse.SC_SEE_OTHER);

        // Define a nova rota para onde o cliente deve ir
        res.setHeader("Location", "/tarefas");
    }
}
