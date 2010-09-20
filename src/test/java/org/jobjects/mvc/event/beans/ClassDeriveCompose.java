package org.jobjects.mvc.event.beans;

public class ClassDeriveCompose extends ClassParent {
  private String composeName;

  public String getComposeName() {
    return composeName;
  }

  public void setComposeName(String composeName) {
    this.composeName = composeName;
  }
  // ---------------------------------------------------------------------------

  private ClassPojo composition;

  public ClassPojo getComposition() {
    return composition;
  }

  public void setComposition(ClassPojo composition) {
    this.composition = composition;
  }
  // ---------------------------------------------------------------------------

  private String[] monTableauString;

  public String[] getMonTableauString() {
    return monTableauString;
  }

  public void setMonTableauString(String[] monTableauString) {
    this.monTableauString = monTableauString;
  }
  // ---------------------------------------------------------------------------

}
