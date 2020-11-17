package com.mape.controller.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

  private FilterConfig config;

  @Override
  public void init(FilterConfig config) throws ServletException {
    this.config = config;
  }

  @Override
  public void destroy() {
    config = null;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    filterChain.doFilter(request, response);
  }
}
