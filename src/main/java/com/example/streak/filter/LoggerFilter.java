package com.example.streak.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Slf4j
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var req = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        var res = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(req, res);

        var headerNames = req.getHeaderNames();
        var headerValues = new StringBuilder();

        headerNames.asIterator().forEachRemaining(headerKey -> {
            var headerValue = req.getHeader(headerKey);
            headerValues.append(headerKey).append(" : ").append(headerValue).append(" , ");
        });

        var requestBody = new String(req.getContentAsByteArray());
        var uri = req.getRequestURI();
        var method = req.getMethod();

        log.info("\n>>>> uri : {}, method : {}\nheader : {}\nbody : {}",uri, method, headerValues, requestBody);

        var responseHeaderNames = req.getHeaderNames();
        var responseHeaderValues = new StringBuilder();

        responseHeaderNames.asIterator().forEachRemaining(headerKey -> {
            var headerValue = res.getHeader(headerKey);
            responseHeaderValues.append(headerKey).append(" : ").append(headerValue).append(" , ");
        });

        var responseBody = new String(res.getContentAsByteArray());
        log.info("\n<<<< uri : {}, method : {}\nheader : {}\nbody : {}",uri, method, responseHeaderValues, responseBody);

        res.copyBodyToResponse();
    }
}
