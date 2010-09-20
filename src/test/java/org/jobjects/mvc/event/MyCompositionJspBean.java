package org.jobjects.mvc.event;

public class MyCompositionJspBean extends MyJspBean {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public MyCompositionJspBean() {
  }

  private MyComposite composite;

  public MyComposite getComposite() {
    return composite;
  }

  public void setComposite(MyComposite composite) {
    this.composite = composite;
  }
}
