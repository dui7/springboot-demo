package com.fentiaozi.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/8
 */
@Configuration
public class CASAutoConfig {

    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;
    @Value("${cas.server-login-url}")
    private String casServerLoginUrl;
    @Value("${cas.client-host-url}")
    private String casClientHostUrl;

    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>();
        listener.setEnabled(true);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    @Bean
    public FilterRegistrationBean<SingleSignOutFilter> logOutFilter() {
        FilterRegistrationBean<SingleSignOutFilter> authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new SingleSignOutFilter());
        authenticationFilter.setEnabled(true);

        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", casServerUrlPrefix);
        initParameters.put("casServerLoginUrl", casServerLoginUrl);
        initParameters.put("serverName", casClientHostUrl);
        //忽略的url，"|"分隔多个url
        initParameters.put("ignorePattern", "/admin/*");
        authenticationFilter.setInitParameters(initParameters);
        authenticationFilter.addUrlPatterns("/*");
        authenticationFilter.setOrder(1);
        return authenticationFilter;
    }
}