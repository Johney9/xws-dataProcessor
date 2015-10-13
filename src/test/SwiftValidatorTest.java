package test;

import static org.junit.Assert.*;

import org.junit.Test;

import validation.SwiftValidator;

public class SwiftValidatorTest {

	@Test
	public void testValidate() {
		assertTrue(SwiftValidator.validate("SBBERS01"));
	}

}
