package org.jobjects.mvc.event.eventest;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jobjects.mvc.event.ErrorMessages;
import org.jobjects.mvc.event.AbstractEvent;
import org.jobjects.mvc.event.JspBean;

public class MonEvent1 extends AbstractEvent {

  @Override
  public String getFromUrl() {
    return "/index1.jsp";
  }

  @Override
  public Class<? extends JspBean> getJspBean() {
    return MonBean1.class;
  }

  @Override
  public void process(ServletContext sc, HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    sc.getRequestDispatcher(getFromUrl()).forward(request, response);
  }

  @Override
  public ErrorMessages validation(HttpServletRequest request) {
    ErrorMessages errorMessages = new ErrorMessages();
    return errorMessages;
  }

}
