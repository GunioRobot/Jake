package com.doublesignal.sepm.jake.core.domain;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>NoteObject</code>
 * @author Simon
 *
 */
public class NoteObjectTest {
	
	private NoteObject n1, n2, nNull;

	@Before
	public void setUp() throws Exception {
		n1 = new NoteObject("foo", "foobar");
		n2 = new NoteObject("bar", "barfoo");
		nNull = new NoteObject(null, null);
	}
	
	@Test
	public void n1Equalsn1() {
		Assert.assertTrue(n1.equals(n1));
	}
	
	@Test
	public void n1Equalsn2() {
		Assert.assertFalse(n1.equals(n2));
	}
	
	@Test
	public void nNullEqualsn1() {
		Assert.assertFalse(nNull.equals(n1));
	}

}
