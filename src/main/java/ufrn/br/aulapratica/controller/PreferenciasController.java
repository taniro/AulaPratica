package ufrn.br.aulapratica.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PreferenciasController {

    @RequestMapping("/preferencias/tema")
    public void alternarTema(HttpServletRequest request, HttpServletResponse response) throws IOException, IOException {

        // 1. LEITURA DO ESTADO ATUAL
        // O servidor HTTP não possui a chave direta, ele recebe um Array bruto
        Cookie[] cookies = request.getCookies();
        String temaAtual = "light"; // Estado padrão (Fallback)

        if (cookies != null) {
            // Busca linear O(n) pelo cookie desejado
            for (Cookie c : cookies) {
                if ("app_tema".equals(c.getName())) {
                    temaAtual = c.getValue();
                }
            }
        }

        // 2. REGRA DE NEGÓCIO
        // Inverte o tema atual
        String novoTema = temaAtual.equals("light") ? "dark" : "light";

        // 3. ESCRITA DO NOVO ESTADO
        Cookie cookieTema = new Cookie("app_tema", novoTema);

        // O cookie não deve morrer ao fechar o navegador. Vai durar 7 dias.
        // 60 seg * 60 min * 24 horas * 7 dias
        cookieTema.setMaxAge(60 * 60 * 24 * 7);

        // Boas práticas absolutas de segurança e escopo
        cookieTema.setHttpOnly(true); // Bloqueia leitura via JavaScript (XSS Mitigation)
        cookieTema.setPath("/");      // O cookie será enviado para qualquer URI do nosso sistema

        // Empurra o comando Set-Cookie para os cabeçalhos de resposta
        response.addCookie(cookieTema);

        // 4. RETORNO VISUAL (Mock de SSR)
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String corFundo = novoTema.equals("dark") ? "#1e1e1e" : "#ffffff";
        String corTexto = novoTema.equals("dark") ? "#ffffff" : "#000000";

        response.getWriter().println("<html style='background-color: " + corFundo + "; color: " + corTexto + ";'>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>Sistema de Gestão de Demandas</h1>");
        response.getWriter().println("<p>O tema foi gravado com sucesso no seu navegador.</p>");
        response.getWriter().println("<p>Tema atual: <strong>" + novoTema.toUpperCase() + "</strong></p>");

        // O botão é apenas um link GET para a mesma rota, que executará a inversão novamente
        response.getWriter().println("<a href='/preferencias/tema'><button>Alternar Tema</button></a>");

        response.getWriter().println("</body></html>");
        response.getWriter().flush();
    }
}