package org.jobjects.mvc.event;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class AbstractEventTest {

  AbstractEvent abstractEvent = null;
  HttpServletRequest requestMock = null;

  @Before
  public void setUp() throws Exception {
    /* minimun de l'instance abstract � tester */
    abstractEvent = new AbstractEvent() {
      @Override
      public ErrorMessages validation(HttpServletRequest request) {
        return null;
      }

      @Override
      public void process(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws ServletException,
          IOException {
      }

      @Override
      public Class<? extends JspBean> getJspBean() {
        return null;
      }

      @Override
      public String getFromUrl() {
        return null;
      }
    };
    requestMock = createMock(HttpServletRequest.class);
  }

  @Test
  public void testGetFromUrl() {
    /* abstract test non n�cessaire */
    assertTrue(true);
  }

  @Test
  public void testGetJspBean() {
    /* abstract test non n�cessaire */
    assertTrue(true);
  }

  @Test
  public void testLoadJspBean() {
    /*
     * la methode de l'abstract a test prend en parametre un HttpServletRequest
     * on le mock en specifiant le comportement qu'on d�sire.
     */
    Vector<String> v = new Vector<String>();
    v.add("myValue");
    v.add("myTableau");
    v.add("myTableau2");
    v.add("MONchamps");
    expect(requestMock.getParameterNames()).andReturn(v.elements());
    expect(requestMock.getParameterValues("myValue")).andReturn((String[]) new String[] { "maChaine" });
    expect(requestMock.getParameterValues("myTableau")).andReturn((String[]) new String[] { "maChaine-a", "maChaine-b", "maChaine-c" });
    expect(requestMock.getParameterValues("myTableau2")).andReturn((String[]) null);
    expect(requestMock.getParameterValues("MONchamps")).andReturn((String[]) new String[] { "la valeur de mon champs" });
    replay(requestMock);
    try {
      MyJspBean myJspBean = abstractEvent.loadJspBean(requestMock, MyJspBean.class);
      assertEquals("maChaine", myJspBean.getMyValue());
      assertArrayEquals(new String[] { "maChaine-a", "maChaine-b", "maChaine-c" }, myJspBean.getMyTableau());
      assertNotNull(myJspBean.getMyTableau2());
      assertEquals("la valeur de mon champs", myJspBean.getMONchamps());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testLoadsurDerivationJspBean() {
    /*
     * la methode de l'abstract a test prend en parametre un HttpServletRequest
     * on le mock en specifiant le comportement qu'on d�sire.
     */
    Vector<String> v = new Vector<String>();
    v.add("myValue");
    v.add("myTableau");
    v.add("myTableau2");
    v.add("MONchamps");
    v.add("derivate");
    expect(requestMock.getParameterNames()).andReturn(v.elements());
    expect(requestMock.getParameterValues("myValue")).andReturn((String[]) new String[] { "maChaine" });
    expect(requestMock.getParameterValues("myTableau")).andReturn((String[]) new String[] { "maChaine-a", "maChaine-b", "maChaine-c" });
    expect(requestMock.getParameterValues("myTableau2")).andReturn((String[]) null);
    expect(requestMock.getParameterValues("MONchamps")).andReturn((String[]) new String[] { "la valeur de mon champs" });
    expect(requestMock.getParameterValues("derivate")).andReturn((String[]) new String[] { "la valeur de mon champs deriv�..." });
    replay(requestMock);
    try {
      MyDerJspBean myJspBean = abstractEvent.loadJspBean(requestMock, MyDerJspBean.class);
      assertEquals("maChaine", myJspBean.getMyValue());
      assertArrayEquals(new String[] { "maChaine-a", "maChaine-b", "maChaine-c" }, myJspBean.getMyTableau());
      assertNotNull("Le tableau ne doit pas �tre nul.", myJspBean.getMyTableau2());
      if (myJspBean.getMyTableau2() != null) {
        assertTrue("Le tableau doit �tre de taille vide.", myJspBean.getMyTableau2().length == 0);
      }

      assertEquals("la valeur de mon champs deriv�...", myJspBean.getDerivate());
      assertEquals("la valeur de mon champs", myJspBean.getMONchamps());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testLoadsurCompositionJspBean() {
    /*
     * la methode de l'abstract a test prend en parametre un HttpServletRequest
     * on le mock en specifiant le comportement qu'on d�sire.
     */
    Vector<String> v = new Vector<String>();
    v.add("myValue");
    v.add("myTableau");
    v.add("myTableau2");
    v.add("MONchamps");
    v.add("composite.monchamp");
    v.add("composite.myTableauEnPlus");
    expect(requestMock.getParameterNames()).andReturn(v.elements());
    expect(requestMock.getParameterValues("myValue")).andReturn((String[]) new String[] { "maChaine" });
    expect(requestMock.getParameterValues("myTableau")).andReturn((String[]) new String[] { "maChaine-a", "maChaine-b", "maChaine-c" });
    expect(requestMock.getParameterValues("myTableau2")).andReturn((String[]) null);
    expect(requestMock.getParameterValues("MONchamps")).andReturn((String[]) new String[] { "la valeur de mon champs" });
    expect(requestMock.getParameterValues("composite.monchamp")).andReturn((String[]) new String[] { "la composition de mon champ^^" });
    expect(requestMock.getParameterValues("composite.myTableauEnPlus")).andReturn(
        (String[]) new String[] { "uneChaine-a", "uneChaine-b", "uneChaine-c" });

    replay(requestMock);
    try {
      MyCompositionJspBean myJspBean = abstractEvent.loadJspBean(requestMock, MyCompositionJspBean.class);
      assertEquals("maChaine", myJspBean.getMyValue());
      assertArrayEquals(new String[] { "maChaine-a", "maChaine-b", "maChaine-c" }, myJspBean.getMyTableau());
      assertNotNull("Le tableau ne doit pas �tre nul.", myJspBean.getMyTableau2());
      if (myJspBean.getMyTableau2() != null) {
        assertTrue("Le tableau doit �tre de taille vide.", myJspBean.getMyTableau2().length == 0);
      }
      assertEquals("la valeur de mon champs", myJspBean.getMONchamps());
      assertEquals("la composition de mon champ^^", myJspBean.getComposite().getMonchamp());
      assertArrayEquals(new String[] { "uneChaine-a", "uneChaine-b", "uneChaine-c" }, myJspBean.getComposite().getMyTableauEnPlus());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testValidation() {
    /* abstract test non n�cessaire */
    assertTrue(true);
  }

  @Test
  public void testProcess() {
    /* abstract test non n�cessaire */
    assertTrue(true);
  }

}
