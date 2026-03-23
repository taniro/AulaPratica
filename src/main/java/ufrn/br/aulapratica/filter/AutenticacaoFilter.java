package ufrn.br.aulapratica.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtro nativo de Servlet para interceptar e validar Sessões HTTP.
 */
public class AutenticacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 1. DOWNCASTING
        // A API de Filtros é polimórfica e atende outros protocolos.
        // Fazemos o cast para focar apenas no HTTP.
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 2. EXTRAÇÃO DE METADADOS DA ROTA
        String uri = req.getRequestURI();

        // 3. WHITELIST (Rotas Públicas)
        // Ignora o bloqueio para a tela de login, processamento do login e recursos estáticos (CSS/JS)
        if (uri.startsWith("/login") || uri.startsWith("/assets") || uri.equals("/")) {
            // DELEGAÇÃO: Permite que a requisição siga adiante na cadeia
            chain.doFilter(request, response);
            return;
        }

        // 4. VERIFICAÇÃO DE ESTADO (Autenticação)
        // Tentamos recuperar a sessão sem forçar a criação (false)
        HttpSession session = req.getSession(false);

        boolean usuarioAutenticado = (session != null && session.getAttribute("usuarioLogado") != null);

        if (usuarioAutenticado) {
            // Usuário provou identidade. Pode prosseguir para o Controller.
            chain.doFilter(request, response);
        } else {
            // 5. BLOQUEIO ARQUITETURAL
            // Usuário anônimo tentando acessar área restrita.
            // Interrompemos a cadeia e disparamos um Redirecionamento 302 para o Login.
            res.setStatus(HttpServletResponse.SC_FOUND);
            res.setHeader("Location", "/login");

            // ATENÇÃO: Nunca chame chain.doFilter() aqui. O bloqueio exige a quebra da cadeia.
        }
    }
}