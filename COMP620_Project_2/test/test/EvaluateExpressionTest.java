package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.ExpressionEvaluator;

class EvaluateExpressionTest {

	ExpressionEvaluator eval;

	@BeforeEach
	void setup() {
		eval = new ExpressionEvaluator();
	}

	@Test
	void TestSimpleFalseEvaluation() {
		eval.addExpression("false");

		assertEquals(Integer.MIN_VALUE, eval.evaluateExpressions());
		assertEquals(false, eval.evaluateExpressionRange(0, 0));
		
		eval.arraysToString();
	}
	

	@Test
	void TestSimpleTrueEvaluation() {
		eval.addExpression("true");

		assertEquals(0, eval.evaluateExpressions());
		assertEquals(true, eval.evaluateExpressionRange(0, 0));
	}
	
	
	
	@Test
	/**
	 * This should have no method of evaluating to a positive.
	 */
	void TestFalseOrFalseEvaluation() {
		eval.addExpression("false");
		eval.addExpression("or");
		eval.addExpression("false"); // false or false = false
		
		assertEquals(Integer.MIN_VALUE, eval.evaluateExpressions());
		assertEquals(false, eval.evaluateExpressionRange(0, 2));
		eval.arraysToString();

	}

	/**
	 * This should not need any modification to evaluate to true.
	 */
	@Test
	void TestTrueOrTrueEvaluation() {
		eval.addExpression("true");
		eval.addExpression("or");
		eval.addExpression("true"); // true or true = true

		assertEquals(0, eval.evaluateExpressions());
		
		eval.arraysToString();
		
		assertEquals(true, eval.evaluateExpressionRange(0, 2));
	}
	
	@Test
	void TestTrueAndFalseEvaluation() {
		eval.addExpression("true");
		eval.addExpression("and");
		eval.addExpression("false"); // true or true = true

		assertEquals(Integer.MIN_VALUE, eval.evaluateExpressions());
		
		eval.arraysToString();
		
		assertEquals(false, eval.evaluateExpressionRange(0, 2));
	}

	/**
	 * This should result in:
	 * 
	 * true or (true and false) true or false true
	 * 
	 */
	@Test
	void TestTrueOrTrueAndFalseEvaluation() {
		eval.addExpression("true");
		eval.addExpression("or");
		eval.addExpression("true");
		eval.addExpression("and");
		eval.addExpression("false"); // Results in true or true and false.

		assertEquals(3, eval.evaluateExpressions());

		
		assertEquals(true, eval.evaluateExpressionRange(0, 4));
		
	}

	/**
	 * This should result in:
	 * 
	 * true and true and true or false true and true and (true or false) true and
	 * true and true true and true true
	 * 
	 * 
	 */
	@Test
	void TestTrueAndTrueAndTrueOrFalseEvaluation() {
		eval.addExpression("true");
		eval.addExpression("and");
		eval.addExpression("true");
		eval.addExpression("and");
		eval.addExpression("true");
		eval.addExpression("or");
		eval.addExpression("false");
		// Results in true or true and false.
		eval.newEvaluateExpressions();

		assertEquals(true, eval.evaluateExpressionRange(0,6));
	}

	@Test
	void TestRidiculousTrueOrTrueLongEvaluation() {
		for (int i = 0; i < 3; i++) {
			eval.addExpression("true");
			eval.addExpression("or");
		}

		//assertEquals(true, eval.evaluateExpressionRange());
		assertEquals(0, eval.evaluateExpressions());
		eval.newEvaluateExpressions();
		eval.arraysToString();
	}

	/**
	 * There is no way to evaluate this other than false
	 */
	@Test
	void TestTrueXORTrueEvaluation() {
		eval.addExpression("true");
		eval.addExpression("xor");
		eval.addExpression("true"); // true xor true = false

		assertEquals(false, eval.evaluateExpressionRange(0, 2));
	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestTrueXORFalseEvaluation() {
		eval.addExpression("true");
		eval.addExpression("xor");
		eval.addExpression("false"); // true xor true = false

		assertEquals(true, eval.evaluateExpressionRange());
	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestFalseXORTrueEvaluation() {
		eval.addExpression("false");
		eval.addExpression("xor");
		eval.addExpression("true"); // true xor true = false

		assertEquals(true, eval.evaluateExpressionRange());
	}

	/**
	 * This should always evaluate as false
	 */
	@Test
	void TestFalseXORFalseEvaluation() {
		eval.addExpression("false");
		eval.addExpression("xor");
		eval.addExpression("false"); // true xor true = false

		assertEquals(false, eval.evaluateExpressionRange());
	}

	/**
	 * This should always evaluate as false
	 */
	@Test
	void TestTrueNANDTrueEvaluation() {
		eval.addExpression("true");
		eval.addExpression("nand");
		eval.addExpression("true"); // true nand true = false

		assertEquals(false, eval.evaluateExpressionRange());
	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestTrueNANDFalseEvaluation() {
		eval.addExpression("true");
		eval.addExpression("nand");
		eval.addExpression("false"); // true nand false = true

		assertEquals(true, eval.evaluateExpressionRange());
	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestFalseNANDTrueEvaluation() {

		eval.addExpression("false");
		eval.addExpression("nand");
		eval.addExpression("true"); // false nand true = true

		assertEquals(true, eval.evaluateExpressionRange());

	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestFalseNANDFalseEvaluation() {
		eval.addExpression("false");
		eval.addExpression("nand");
		eval.addExpression("false"); // false nand false = true

		assertEquals(true, eval.evaluateExpressionRange());
	}

	/**
	 * This should always evaluate as false
	 */
	@Test
	void TestNotTrueEvaluation() {
		eval.addExpression("not");
		eval.addExpression("true");

		assertEquals(false, eval.evaluateExpressionRange(0, 1));
	}

	/**
	 * This should always evaluate as false
	 */
	@Test
	void TestNotFalseEvaluation() {
		eval.addExpression("not");
		eval.addExpression("false");

		assertEquals(true, eval.evaluateExpressionRange(0, 1));
	}

	/**
	 * This should always evaluate as:
	 * 
	 * not true and false not (true and false) not false true
	 */
	@Test
	void TestNotTrueAndFalseEvaluation() {
		eval.addExpression("not");
		eval.addExpression("true");
		eval.addExpression("and");
		eval.addExpression("false");

		assertEquals(true, eval.evaluateExpressionRange());
	}
	
}
