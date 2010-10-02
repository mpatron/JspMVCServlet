package org.jobjects.mvc.event;

public class MyJspBean implements JspBean {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public MyJspBean() {
  }

  private String myValue;

  public String getMyValue() {
    return myValue;
  }

  public void setMyValue(String myValue) {
    this.myValue = myValue;
  }

  private String[] myTableau;

  public String[] getMyTableau() {
    return myTableau;
  }

  public void setMyTableau(String[] myTableau) {
    this.myTableau = myTableau;
  }

  private String[] myTableau2;

  public String[] getMyTableau2() {
    return myTableau2;
  }

  public void setMyTableau2(String[] myTableau2) {
    this.myTableau2 = myTableau2;
  }

  private String mONchamps;

  public String getMONchamps() {
    return mONchamps;
  }

  public void setMONchamps(String mONchamps) {
    this.mONchamps = mONchamps;
  }
  
  /*attribut privé sans getter nin setter pour vérifier qu'il ne sera pas pris dans le marshaling.*/
  @SuppressWarnings("unused")
  private String pDTransacCodeToto;
}
