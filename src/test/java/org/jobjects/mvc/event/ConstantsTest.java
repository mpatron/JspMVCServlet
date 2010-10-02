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
		assertEquals("action doit être présent.", "action", Constants.action.name());
	}

	@Test
	public void testConstantsNomERRORS() {
		assertEquals("errors doit être présent.", "errors", Constants.errors.name());
	}

	@Test
	public void testConstantsNomJSPBEAN() {
		assertEquals("jspBean doit être présent.", "jspBean", Constants.jspBean.name());
	}
}
