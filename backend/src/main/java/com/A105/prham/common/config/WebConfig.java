package com.A105.prham.common.config;

import com.A105.prham.auth.filter.TokenRefreshFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig {

    final private TokenRefreshFilter tokenRefreshFilter;

    @Bean
    public FilterRegistrationBean<TokenRefreshFilter> tokenFilter() {
        FilterRegistrationBean<TokenRefreshFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenRefreshFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}