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
		assertEquals("Nombre d'élément en constante.", 3, Constants.values().length);
	}

	@Test
	public void testConstantsNomACTION() {
		assertEquals("ACTION doit être présent.", "ACTION", Constants.ACTION.name());
	}

	@Test
	public void testConstantsNomERRORS() {
		assertEquals("ACTION doit être présent.", "ERRORS", Constants.ERRORS.name());
	}

	@Test
	public void testConstantsNomJSPBEAN() {
		assertEquals("ACTION doit être présent.", "JSPBEAN", Constants.JSPBEAN.name());
	}
}
