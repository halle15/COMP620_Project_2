package src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewExpressionEvaluator {

	// A map for memoization to store evaluated subexpressions
	// Key: Subexpression as a string, Value: Evaluation result as Boolean
	private Map<String, Boolean> memo = new HashMap<>();

	public boolean evaluate(List<String> expression) {
		String exprStr = String.join(" ", expression); // Convert list to string for memoization
		return evaluateRecursive(expression, 0, expression.size() - 1);
	}

	private boolean evaluateRecursive(List<String> expression, int start, int end) {
		// Convert the current subexpression to a string key for memoization
		String key = String.join(" ", expression.subList(start, end + 1));
		if (memo.containsKey(key)) {
			return memo.get(key);
		}

		// Base case: simple true/false evaluation
		if (start == end) {
			boolean result = Boolean.parseBoolean(expression.get(start));
			memo.put(key, result);
			return result;
		}

		boolean result = false;
		for (int i = start + 1; i < end; i += 2) {
			String operator = expression.get(i);
			boolean left = evaluateRecursive(expression, start, i - 1);
			boolean right = evaluateRecursive(expression, i + 1, end);

			// Apply the operation
			switch (operator) {
			case "and":
				result = left && right;
				break;
			case "or":
				result = left || right;
				break;
			// Expand this switch case to handle other operations
			}
		}

		// Memoize and return the result
		memo.put(key, result);
		return result;
	}
}
