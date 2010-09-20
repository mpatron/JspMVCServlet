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
		assertEquals("ACTION doit �tre pr�sent.", "ACTION", Constants.ACTION.name());
	}

	@Test
	public void testConstantsNomERRORS() {
		assertEquals("ACTION doit �tre pr�sent.", "ERRORS", Constants.ERRORS.name());
	}

	@Test
	public void testConstantsNomJSPBEAN() {
		assertEquals("ACTION doit �tre pr�sent.", "JSPBEAN", Constants.JSPBEAN.name());
	}
}
