package com.mape.controller;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    req.getSession().removeAttribute("userLogin");
    req.getSession().removeAttribute("userId");
    req.getSession().removeAttribute("userRole");
    req.getSession().removeAttribute("userFirstName");
    req.getSession().removeAttribute("userLastName");
    resp.sendRedirect("./");
  }
}
