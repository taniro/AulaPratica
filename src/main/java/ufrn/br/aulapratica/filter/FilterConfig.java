package ufrn.br.aulapratica.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AutenticacaoFilter> registrarFiltroAutenticacao() {
        FilterRegistrationBean<AutenticacaoFilter> registrationBean = new FilterRegistrationBean<>();

        // Instancia nosso filtro
        registrationBean.setFilter(new AutenticacaoFilter());

        // Define o padrão de interceptação (Neste caso, TODAS as rotas)
        registrationBean.addUrlPatterns("/*");

        // Define a ordem de execução (útil quando se tem dezenas de filtros)
        registrationBean.setOrder(1);

        return registrationBean;
    }
}