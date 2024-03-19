package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpressionEvaluator {

	public int[][] costsMap;
	public String[][] expressionMap;

	public ArrayList<String> expressions;

	public ExpressionEvaluator() {
		super();
		this.expressions = new ArrayList<String>();
	}

	public ExpressionEvaluator(ArrayList<String> expressions) {
		super();
		this.expressions = expressions;
	}

	public void clearExpressions() {
		this.expressions = new ArrayList<String>();
	}

	public ArrayList<String> getExpressions() {
		return expressions;
	}

	public void addExpression(String expression) {
		expressions.add(expression);
	}

	public void setExpressions(ArrayList<String> expressions) {
		this.expressions = expressions;
	}

	public void display2DArray(String[][] array) {
		for (String[] row : array) {
			for (String element : row) {
				System.out.print(element + "\t");
			}
			System.out.println();
		}
	}

	public void initializeArrays() {
		int length = expressions.size();

		costsMap = new int[length][length];
		expressionMap = new String[length][length];

		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length - 1; j++) {
				costsMap[i][j] = Integer.MAX_VALUE;
			}
		}

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				expressionMap[i][j] = "X";
			}
		}

	}

	public void arraysToString() {

		System.out.println("===========+");

		System.out.println("Costs");

		for (int i = 0; i < costsMap.length; i++) {
			for (int j = 0; j < costsMap[i].length; j++) {
				System.out.print("|" + costsMap[i][j] + "|"); // Use tab for spacing
			}
			System.out.println(); // New line after each row
		}

		System.out.println("Expressions");

		System.out.println("========");

		display2DArray(expressionMap);

		System.out.println("========");

	}

	public boolean isBool(String input) {
		return (input.equals("true") || input.equals("false"));
	}

	public boolean solveOne(String input) {
		return Boolean.parseBoolean(input);
	}

	public boolean solveTwo(String input1, String input2) {
		if (input1 == "not" && isBool(input2)) {

			boolean answer = !Boolean.parseBoolean(input2);

			return answer;

		}

		throw new IllegalArgumentException("the first of two should not be 'not'!");

	}

	public boolean solveThree(String input1, String input2, String input3) {

		boolean answer;

		if (input2.equals("and")) {
			answer = Boolean.parseBoolean(input1) && Boolean.parseBoolean(input3);

			return answer;
		} else if (input2.equals("or")) {
			answer = Boolean.parseBoolean(input1) && Boolean.parseBoolean(input3);

			return answer;
		} else {
			throw new IllegalArgumentException("We don't know how to do this operator yet :(");
		}
	}

	public int newEvaluateExpressions() {

		/*
		 * 1. Start by initializing the variable, then filling in the diagonal
		 * 
		 * 2. After we fill in diagonal (11, 33, 55), we continue to evaluate the
		 * statement collections, such as 1-3, 3-5, 5-7.
		 * 
		 * 3. When we get to the next diagonal, we are evaluating the tree from 1-5,
		 * 3-7, etc. In this instance, we must make a choice out of the two, so we must
		 * find a way to look at a function, and be able to evaluate it as truthy, not
		 * only this, we must set a cost for placing parentheses around these values (if
		 * we do/have to) that make it truthy.
		 * 
		 * 3 (cont). We are essentially trying to say
		 * "from a-b, what does it cost to make this evaluate to true"
		 * 
		 * In cases of 1-3 (or three expressions), we don't aren't able to force
		 * anything to change
		 * 
		 * In case of 1-5, this is where we have (at least) two possible operations to
		 * go first:
		 * 
		 * (1-3)-5 OR 1-(3-5)
		 * 
		 * If we have this, we need to see which one is more advantageous to evaluate
		 * first.
		 * 
		 * If we include NOT operators, the issue becomes more complicated. We will not
		 * be able to rely on expecting the minimum to be at least 3 operators, it could
		 * be 2 at this point.
		 * 
		 * TODO: handle not later :)
		 * 
		 * 4. We reiterate until we reach the top right corner, which will be our
		 * answer.
		 */

		/*
		 * 1. Start by initializing the variable, then filling in the diagonal
		 */

		int length = expressions.size();

		initializeArrays();

		/*
		 * 2. After we fill in diagonal (11, 33, 55), we continue to evaluate the
		 * statement collections, such as 1-3, 3-5, 5-7.
		 */

		/*
		 * for (int start = 0; start < length; start++) { // Iterate down the diagonal
		 * for (int row = 0, col = start; row < length && col < length; row++, col++) {
		 * try { evaluateExpressionRange(row, col); } catch (IllegalArgumentException e)
		 * { System.out.println("unexpected position of 'not' unary operator!"); } } }
		 */

		for (int rangeIncrement = 0; rangeIncrement < length; rangeIncrement += 2) {
			// Start from 0 to length - 1, increment by 2 after each full cycle
			for (int start = 0; start + rangeIncrement < length; start += 2) {
				int end = start + rangeIncrement;

				// Here you would check if the range from start to end is valid
				// and continue to the next iteration if not valid
				// For demonstration, let's assume isValidRange(start, end) is always true

				
				// Debug print to show the current range being processed
				System.out.println(end-start);
				System.out.println("(" + start + ")-(" + end + ")");
				System.out.println("(" + expressions.get(start) + ")" + expressions.get(start+1) + "(" + expressions.get(end) + ")");

			}
		}

		return Integer.MIN_VALUE;
	}

	public int evaluateExpressions() {
		int length = expressions.size();

		initializeArrays();

		// start by initializing our diagonals to equal corresponding values
		for (int i = 0; i < length; i++) {
			expressionMap[i][i] = expressions.get(i);

			try {
				evaluateExpressionRange(i, i);
			} catch (IllegalArgumentException e) {
				System.out.println("Tried evaluating non operand on initialization loop.");
			}
		}

		for (int start = 0; start < length; start++) {
			// Iterate down the diagonal
			for (int row = 0, col = start; row < length && col < length; row++, col++) {
				try {
					evaluateExpressionRange(row, col);
				} catch (IllegalArgumentException e) {
					System.out.println("unexpected position of 'not' unary operator!");
				}
			}
		}

		return costsMap[length - 1][length - 1];// return our top right corner, or result

	}

	/**
	 * 
	 * @param expr The expression to be evaluated
	 * @return Used for testing/validation purposes.
	 */

	public boolean evaluateExpressionRange(int start, int end) {

		/*
		 * case end - start = 0
		 * 
		 * just evaluate the individual expression
		 */

		if (end - start == 0) {

			if (isBool(expressionMap[start][start])) {
				boolean answer = solveOne(expressionMap[start][start]);

				if (answer) {
					costsMap[start][end] = 0;
				} else if (expressions.get(end).equals("false")) {
					costsMap[start][end] = Integer.MIN_VALUE;
				}

				expressionMap[start][end] = Boolean.toString(answer);
				return answer;

			}

			throw new IllegalArgumentException("Single expression but not a boolean!");
		}

		/*
		 * case end - start = 1
		 * 
		 * in this case, it is not + a primitive, invert the primitive
		 */

		if (end - start == 1) {
			if (expressions.get(start) == "not" && isBool(expressions.get(end))) {

				boolean answer = !Boolean.parseBoolean(expressions.get(end));

				expressionMap[start][end] = Boolean.toString(answer);

				return answer;

			}

			throw new IllegalArgumentException("the first of two should not be 'not'!");

		}

		/*
		 * case end - start = 2
		 * 
		 * in this case, it is primitive, operator, primitive.
		 */

		if (end - start == 2) {
			boolean answer = solveThree(expressionMap[start][start], expressionMap[start + 1][start + 1],
					expressionMap[end][end]);

			expressionMap[start][end] = Boolean.toString(answer);

			return answer;
		}

		/*
		 * case end - start = > 2
		 * 
		 * in this case we will have to make a decision on which to parenthesize.
		 */

		throw new IllegalArgumentException("We don't know how to do " + (end - start) + " expressions yet :(");

	}

}
