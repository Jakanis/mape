package com.mape.controller.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = {"/*"})
public class SessionLocaleFilter implements Filter {

  private FilterConfig config;

  @Override
  public void init(FilterConfig config) throws ServletException {
    this.config = config;
  }

  @Override
  public void destroy() {
    config = null;
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;

    if (req.getParameter("sessionLocale") != null) {
      req.getSession().setAttribute("lang", req.getParameter("sessionLocale"));
    }
    chain.doFilter(request, response);
  }
}