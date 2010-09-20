package org.jobjects.mvc.event.eventest;

import org.jobjects.mvc.event.JspBean;

public class MonBean1 implements JspBean {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String champs1;
  private Integer champs2;

  public MonBean1() {
  }

  public String getChamps1() {
    return champs1;
  }

  public void setChamps1(String champs1) {
    this.champs1 = champs1;
  }

  public Integer getChamps2() {
    return champs2;
  }

  public void setChamps2(Integer champs2) {
    this.champs2 = champs2;
  }

}
