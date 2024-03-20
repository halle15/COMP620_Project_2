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
		
		eval.evaluateExpressions();

		assertEquals(Integer.MAX_VALUE, eval.returnResult());
		
		eval.arraysToString();
	}
	

	@Test
	void TestSimpleTrueEvaluation() {
		eval.addExpression("true");
		
		eval.evaluateExpressions();
	    eval.arraysToString();


		assertEquals(0, eval.returnResult());
	}
	
	@Test
	void TestTrueAndFalseOrTrueOrFalseEvaluation() {
		eval.addExpression("true");
		eval.addExpression("and");
		eval.addExpression("false"); 
		eval.addExpression("or");
		eval.addExpression("true");
		eval.addExpression("or");
		eval.addExpression("false");
		
		eval.evaluateExpressions();
	      eval.arraysToString();

		
		assertEquals(0, eval.returnResult());
				
	}
	
	@Test
	/**
	 * This should have no method of evaluating to a positive.
	 */
	void TestFalseOrFalseEvaluation() {
		eval.addExpression("false");
		eval.addExpression("or");
		eval.addExpression("false"); // false or false = false
		
		eval.evaluateExpressions();
	      eval.arraysToString();

		
		assertEquals(Integer.MAX_VALUE, eval.returnResult());
				
	}
	
	   @Test
	    /**
	     * This should have no method of evaluating to a positive.
	     */
	    void TestTrueOrTrueAndFalseOrFalseEvaluation() {
           eval.addExpression("true");
           eval.addExpression("or");
           eval.addExpression("true");
           eval.addExpression("and");
           eval.addExpression("false");
           eval.addExpression("or");
           eval.addExpression("false");
           
           eval.evaluateExpressions();
           eval.arraysToString();

	        
	        assertEquals(3, eval.returnResult());
	                
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
		
	      eval.evaluateExpressions();
	        eval.arraysToString();

		assertEquals(0, eval.returnResult());
		
        eval.arraysToString();

	}
	
	@Test
	void TestTrueAndFalseEvaluation() {
	    System.out.println("TrueAndFalseTest\n===================");
	    
		eval.addExpression("true");
		eval.addExpression("and");
		eval.addExpression("false"); // true or true = true
		
	      eval.evaluateExpressions();
	        eval.arraysToString();

		assertEquals(Integer.MAX_VALUE, eval.returnResult());
        eval.arraysToString();

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
		
		
		eval.initializeArrays();

	      eval.evaluateExpressions();
	        eval.arraysToString();

		assertEquals(3, eval.returnResult());

	}
	

	/**
	 * This should result in:
	 * 
	 * true and true and true or false true and true and (true or false) true and
	 * true and true true and true true
	 * CONT
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
        eval.evaluateExpressions();
        eval.arraysToString();
        assertEquals(0, eval.returnResult());

	}
	
	
	@Test
	void FalseXORTrueAndTrueNandTrue() {
		eval.addExpression("false");
		eval.addExpression("xor");
		eval.addExpression("true");
		eval.addExpression("and");
		eval.addExpression("true");
		eval.addExpression("nand");
		eval.addExpression("true");
		// Results in true or true and false.
        eval.evaluateExpressions();
        eval.arraysToString();
        assertEquals(0, eval.returnResult());

	}
	
	/**
	 * There is no way to evaluate this other than false
	 */
	@Test
	void TestTrueXORTrueEvaluation() {
		eval.addExpression("true");
		eval.addExpression("xor");
		eval.addExpression("true"); // true xor true = false
		
        eval.evaluateExpressions();
        eval.arraysToString();
        assertEquals(Integer.MAX_VALUE, eval.returnResult());
		
	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestTrueXORFalseEvaluation() {
		eval.addExpression("true");
		eval.addExpression("xor");
		eval.addExpression("false"); // true xor true = false
		
        eval.evaluateExpressions();
        eval.arraysToString();
        assertEquals(0, eval.returnResult());
	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestFalseXORTrueEvaluation() {
		eval.addExpression("false");
		eval.addExpression("xor");
		eval.addExpression("true"); // true xor true = false
		
        eval.evaluateExpressions();
        eval.arraysToString();
        assertEquals(0, eval.returnResult());

	}

	/**
	 * This should always evaluate as false
	 */
	@Test
	void TestFalseXORFalseEvaluation() {
		eval.addExpression("false");
		eval.addExpression("xor");
		eval.addExpression("false"); // true xor true = false
		
	      eval.evaluateExpressions();
	        eval.arraysToString();
	        assertEquals(Integer.MAX_VALUE, eval.returnResult());

	}

	/**
	 * This should always evaluate as false
	 */
	@Test
	void TestTrueNANDTrueEvaluation() {
		eval.addExpression("true");
		eval.addExpression("nand");
		eval.addExpression("true"); // true nand true = false
		
	      eval.evaluateExpressions();
	        eval.arraysToString();
	        
	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestTrueNANDFalseEvaluation() {
		eval.addExpression("true");
		eval.addExpression("nand");
		eval.addExpression("false"); // true nand false = true
		
	      eval.evaluateExpressions();
	        eval.arraysToString();

	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestFalseNANDTrueEvaluation() {

		eval.addExpression("false");
		eval.addExpression("nand");
		eval.addExpression("true"); // false nand true = true


	}

	/**
	 * This should always evaluate as true
	 */
	@Test
	void TestFalseNANDFalseEvaluation() {
		eval.addExpression("false");
		eval.addExpression("nand");
		eval.addExpression("false"); // false nand false = true
		
	      eval.evaluateExpressions();
	        eval.arraysToString();

	}

	/**
	 * This should always evaluate as false
	 */
	@Test
	void TestNotTrueEvaluation() {
		eval.addExpression("not");
		eval.addExpression("true");
		
	      eval.evaluateExpressions();
	        eval.arraysToString();

	}

	/**
	 * This should always evaluate as false
	 */
	@Test
	void TestNotFalseEvaluation() {
		eval.addExpression("not");
		eval.addExpression("false");

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

	}
	
}
