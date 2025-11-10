package com.A105.prham.common.config;

import com.A105.prham.auth.filter.TokenRefreshFilter;
import com.A105.prham.common.annotation.UserIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    final private TokenRefreshFilter tokenRefreshFilter;
    private final UserIdArgumentResolver userIdArgumentResolver;

    @Bean
    public FilterRegistrationBean<TokenRefreshFilter> tokenFilter() {
        FilterRegistrationBean<TokenRefreshFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenRefreshFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userIdArgumentResolver);
    }

}