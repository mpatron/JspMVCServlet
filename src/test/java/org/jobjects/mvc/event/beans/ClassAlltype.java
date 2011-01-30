package org.jobjects.mvc.event.beans;

import java.util.List;
import java.util.Map;

public class ClassAlltype {
  private String monChampString;

  public String getMonChampString() {
    return monChampString;
  }

  public void setMonChampString(String monChampString) {
    this.monChampString = monChampString;
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

  private List<String> maList;

  public List<String> getMaList() {
    return maList;
  }

  public void setMaList(List<String> maList) {
    this.maList = maList;
  }

  /*
   * private Integer monChampInteger;
   * 
   * public Integer getMonchampInteger() { return monChampInteger; }
   * 
   * public void setMonchampInteger(Integer monChampInteger) {
   * this.monChampInteger = monChampInteger; }
   * 
   * private Integer[] monTableauInteger;
   * 
   * public Integer[] getMonTableauInteger() { return monTableauInteger; }
   * 
   * public void setMonTableauInteger(Integer[] monTableauInteger) {
   * this.monTableauInteger = monTableauInteger; }
   */
  // ---------------------------------------------------------------------------
  /*
   * private int monChampInt;
   * 
   * public int getMonchampInt() { return monChampInt; }
   * 
   * public void setMonchampInt(int monChampInt) { this.monChampInt =
   * monChampInt; }
   * 
   * private int[] monTableauInt;
   * 
   * public int[] getMonTableauInt() { return monTableauInt; }
   * 
   * public void setMonTableauInt(int[] monTableauInt) { this.monTableauInt =
   * monTableauInt; }
   */
  // ---------------------------------------------------------------------------

  private Map<String, String> maMap;

  public Map<String, String> getMaMap() {
    return maMap;
  }

  public void setMaMap(Map<String, String> maMap) {
    this.maMap = maMap;
  }

}
