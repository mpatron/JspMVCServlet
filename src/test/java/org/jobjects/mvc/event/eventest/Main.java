package org.jobjects.mvc.event.eventest;

public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String ActeSqlList = "'APA' , 'APM' , 'APQ'";
    String TrsSqlList  =
        ActeSqlList.replace(" ","").replace("'","C'").replace("C'A","'A") + "," //Mise en place
      + ActeSqlList.replace(" ","").replace("'","M'").replace("M'A","'A")+ "," //Modification
      + ActeSqlList.replace(" ","").replace("'","S'").replace("S'A","'A")      //Arrêt
      ;
    
    String ActeSqlComboList = ActeSqlList.replace(" ","").replace("'","FOR'").replace("FOR'A","'A");

    
    System.out.println("ActeSqlList="+ActeSqlList);
    System.out.println("TrsSqlList="+TrsSqlList);
    System.out.println("ActeSqlComboList="+ActeSqlComboList);
  }

}
