/**
 * 
 */
package org.jobjects.mvc.event;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 * @author patronmi
 *
 */
public class ConstantsTest {

	@Test
	public void testConstantsNombreElement() {
		assertEquals("Nombre d'�l�ment en constante.", 3, Constants.values().length);
	}

	@Test
	public void testConstantsNomACTION() {
		assertEquals("action doit �tre pr�sent.", "action", Constants.action.name());
	}

	@Test
	public void testConstantsNomERRORS() {
		assertEquals("errors doit �tre pr�sent.", "errors", Constants.errors.name());
	}

	@Test
	public void testConstantsNomJSPBEAN() {
		assertEquals("jspBean doit �tre pr�sent.", "jspBean", Constants.jspBean.name());
	}
}
