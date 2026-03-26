package ufrn.br.aulapratica.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufrn.br.aulapratica.service.TarefaService;
import ufrn.br.aulapratica.view.TarefaHtmlView;

import java.io.IOException;

@RestController
public class TarefaController {

    // O Controller depende do Service, nunca do Repository diretamente
    private final TarefaService tarefaService = new TarefaService();

    @RequestMapping("/tarefas")
    public void gerenciar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String metodo = request.getMethod();

        try {
            if ("GET".equalsIgnoreCase(metodo)) {
                responderComHtml(response, null);
            } else if ("POST".equalsIgnoreCase(metodo)) {
                processarCriacao(request, response);
            }
        } catch (IllegalArgumentException e) {
            // Captura o erro da regra de negócio e envia para a View exibir
            responderComHtml(response, e.getMessage());
        }
    }

    private void processarCriacao(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String texto = req.getParameter("texto");

        // DELEGAÇÃO: O Controller não valida, ele passa a bola para o Service
        tarefaService.criarNovaTarefa(texto);

        // Padrão PRG (Post-Redirect-Get) após sucesso
        res.setStatus(HttpServletResponse.SC_SEE_OTHER);
        res.setHeader("Location", "/tarefas");
    }

    private void responderComHtml(HttpServletResponse res, String mensagemErro) throws IOException {
        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");

        // 1. Pega os dados com o Service
        var tarefas = tarefaService.listarTarefas();

        // 2. Entrega os dados para a View renderizar a String HTML
        String htmlGerado = TarefaHtmlView.renderizarLista(tarefas, mensagemErro);

        // 3. Escreve a String pronta na rede
        res.getWriter().print(htmlGerado);
        res.getWriter().flush();
    }
}