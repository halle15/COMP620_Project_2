package src;

import java.util.ArrayList;

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

		// Initialize all costs to max value
		for (int i = 0; i < length; i++) { // Fixed to iterate up to length
			for (int j = 0; j < length; j++) { // Fixed to iterate up to length
				costsMap[i][j] = 999;
			}
		}

		for (int i = 0; i < length; i += 2) {
			if (!isBool(expressions.get(i))) {
				i -= 1;
				continue;
			}

			costsMap[i][i] = evaluateCostOfExpressionRange(i, i);

		}
	}

	public void arraysToString() {

		System.out.println("===========+");

		System.out.println(expressions.toString());

		System.out.println("===========+");

		System.out.println("Costs - Length: " + costsMap.length);

		for (int i = 0; i < costsMap.length; i++) {
			for (int j = 0; j < costsMap[i].length; j++) {
				System.out.print("|" + costsMap[i][j] + "|");
			}
			System.out.println();
		}

		System.out.println("===========+");

	}

	public boolean isBool(String input) {
		return (input.equals("true") || input.equals("false"));
	}

	public int solve(String input) {

		boolean bool = Boolean.parseBoolean(input);

		return bool ? 0 : Integer.MAX_VALUE;
	}

	public int solve(String input1, String input2) {
		if (input1 == "not" && isBool(input2)) {

			boolean answer = !Boolean.parseBoolean(input2);

			return solve(Boolean.toString(answer));

		}

		throw new IllegalArgumentException("the first of two should not be 'not'!");

	}

	public boolean LogicalNAND(boolean input1, boolean input2) {
		return !(input1 && input2);
	}
	
	
	public int solve(String input1, String input2, String input3) {

		boolean answer;

		if (input2.equals("and")) {
			answer = Boolean.parseBoolean(input1) && Boolean.parseBoolean(input3);
			return answer ? 0 : Integer.MAX_VALUE;
		} else if (input2.equals("or")) {
			answer = Boolean.parseBoolean(input1) || Boolean.parseBoolean(input3);
			return answer ? 0 : Integer.MAX_VALUE;
		} else if (input2.equals("xor")) {
			answer = Boolean.logicalXor(Boolean.parseBoolean(input1), Boolean.parseBoolean(input3));

			return answer ? 0 : Integer.MAX_VALUE;
		} else if (input2.equals("nand")) {
			answer = LogicalNAND(Boolean.parseBoolean(input1), Boolean.parseBoolean(input3));

			return answer ? 0 : Integer.MAX_VALUE;
		} else {
			throw new IllegalArgumentException("We don't know how to do " + input1 + input2 + input3 + " at   yet :(");
		}
	}
	
	

	public int evaluateExpressions() {

		/*
		 * ==== General Plain English Problem Workthrough ====
		 * 
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
		 * be able to rely on expecting the MAXimum to be at least 3 operators, it could
		 * be 2 at this point.
		 * 
		 * TODO: handle not later :)
		 * 
		 * 4. We reiterate until we reach the top right corner, which will be our
		 * answer.
		 */

		/*
		 * 1. Start by initializing the map, then filling in the diagonal
		 */

		int length = expressions.size();

		initializeArrays();

		/*
		 * 2. After we fill in diagonal (0-0, 2-2, 4-4), we continue to evaluate the
		 * statement collections, such as 0-2, 2-4, 4-6. we do NOT go 0-1, 1-2
		 * 
		 * we need to increment 3 each time, so that we continue to the next expression.
		 * Example
		 * 
		 * when we fill out 0-2, 2-4, 4-6, we need to continue to 1-4, 2-6. then we
		 * finish by going to the top right, 0-6.
		 * 
		 * TODO: find solution to include potential not statements
		 */

		// Fill the costs for ranges like (0,2), (1,3), (2,4), etc.
		for (int rangeIncrement = 2; rangeIncrement < length; rangeIncrement += 2) {
			for (int start = 0; start + rangeIncrement < length; start += 2) {
				int end = start + rangeIncrement;

				costsMap[start][end] = evaluateCostOfExpressionRange(start, end);

				// Debug print to show the current range being processed
				
				
				/*System.out.println("Evaluating: \n" + expressions.get(start) + " " + expressions.get(start + 1) + " "
						+ expressions.get(end) + "\n=============");
						
						*/ 
				
				
				// Only add debugging prints if expressions and costsMap are correctly
				// referenced
				// System.out.println(expressions.get(start) + " to " + expressions.get(end) + "
				// cost: " + costsMap[start][end]);
			}
		}

		return costsMap[length - 1][length - 1];// return our top right corner, or result

	}

	public int returnResult() {
		int length = expressions.size();
		return costsMap[0][length - 1];// return our top right corner, or result
	}

	public int evaluateCostOfExpressionRange(int start, int end) {
		/*
		 * case end - start = 0
		 * 
		 * just evaluate the individual expression
		 */

		if (end - start == 0) {

			String expression = expressions.get(start);

			//System.out.println(expression);

			if (isBool(expression)) {
				return solve(expression);
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

				return solve(expressions.get(end));

			}

			throw new IllegalArgumentException("the first of two should not be 'not'!");

		}

		/*
		 * 
		 * case end - start = 2
		 * 
		 * in this case, it is primitive, operator, primitive.
		 */

		else {
			if (end - start == 2) {
			   
				return solve(expressions.get(start), expressions.get(start + 1), expressions.get(end));
			}

			/*
			 * 
			 * in this case, we are at a given x,y, or start, end.
			 * 
			 * when we are at our start, end in the map, we must consider two choices:
			 * 
			 * we can do nothing, and place no parentheses at a cost of nothing and have the expression eval from left to right
			 * 
			 * we can place parentheses around (x, y+2) for a cost of minimum(cost[x][y+2] + (end - start), 0)
			 * 
			 * (e.g. if we are given true and true or false nand true, when we are evaluating 7, 3, we must choose the min between
			 * 
			 * 
			 *       min(cost[7-2][3], 0)
			 *       min(cost[7][3+2] + (end - start), 0)
			 *       
			 *       ===============================================================================
			 *       
			 *       MAYBE RATHER THAN THE BRUTE FORCE COST DIFFERENCE, AS IT SEEMS THERE ARE TWO OPTIONS. SOMETIMES WE WANT TO FORCE A FALSE RESULT
			 *       IN ORDER TO END UP WITH A TRUE RESULT IN EXPRESSIONS
			 *       
			 *       WE JUST ANAYLZE TO SEE (EXAMPLE TRUE OR TRUE AND FALSE)
			 *       
			 *       IF 1-3, IN ANSWER TO 1-3, OPERATOR 4, EXPRESSION 5, (TRUE OR TRUE) AND FALSE, WERE TO BE TRUTHY, WOULD THIS WORK?
			 *           THIS CHOICE DOES NOT CARRY A COST AS THERE DO NOT ACTUALLY NEED TO BE PARENTHESES THERE.
			 *           
			 *       IF 3-5, IN EXPRESSION 1, OPERATOR 2, ANSWER TO 3-5, TRUE OR (TRUE AND FALSE) WERE TO BE TRUTHY, WOULD THIS WORK DESPITE THE ADDED COST?
			 *       
			 *       1-3 AND FALSE : THIS WILL NOT BE WORTH CHECKING EVEN IF 1-3 IS TRUE! IF WE ACTUALLY DID TAKE THIS, WE READ FROM LEFT TO RIGHT,
             *           COSTING NOTHING.
			 *       
			 *       TRUE OR 3-5 : IN THIS CASE, IT DOESN'T EVEN MATTER IF 3-5 IS TRUE! WE DON'T REALLY HAVE TO EVALUATE IT! WE AUTOMATICALLY ASSIGN IT THE
			 *       COST OF (END-START)+2, AS WE DO NOT NEED TO EVALUATE 3-5 TO TRUE, WE ONLY NEED TO PARENTHESIZE.
			 *       
			 *       SO IN THIS CASE IT WILL BE A COST OF (5-1)+2, OR 3, AS WE ARE PARENTHESIZING THREE OBJECTS.
			 *       
			 *       IF WE EVALUATED 1-7, IT WOULD BE (7-1)+2, OR 5, PARENTHESIZING 5, ETC.
			 *       
			 *       
			 *       I SHOULD WALK THROUGH EACH CASES GIVEN AN OPERATOR, ESPECIALLY SO WITH NAND AND XOR.
			 *       
			 *       
			 *       
			 *       
			 * 
			 */
			
			
			/*
			 * Left to right (cheap case)
			 */
			
			boolean cheapCaseSubExpressionResult = (costsMap[start][end - 2] < Integer.MAX_VALUE);
			String endOperation = expressions.get(end - 1);
			boolean endExpression = Boolean.valueOf(expressions.get(end));
			
			int previousCost = costsMap[start][end - 2];
			
			if(previousCost > Integer.MAX_VALUE/2) {
                previousCost = 0;
            }
			
			int cheapCaseScore = 0 + previousCost;
			
			
			
		    switch (endOperation) {
	        case "and":
	            
	            // Handle 'and' operation
	            boolean result = cheapCaseSubExpressionResult && endExpression;
	            
	            if(!result) {
	                cheapCaseScore = Integer.MAX_VALUE;
	            }

	            break;
	        case "or":
	            // Handle 'or' operation
	            result = cheapCaseSubExpressionResult || endExpression;
	            
	            if(!result) {
	                    cheapCaseScore = Integer.MAX_VALUE;
	            }

	            break; 
	        case "xor":
	            // Handle 'xor' operation
	            result = cheapCaseSubExpressionResult ^ endExpression;
	            
	           if(!result) {
                       cheapCaseScore = Integer.MAX_VALUE;
               }

	            break;
	        case "nand":	            
	            // Handle 'nand' operation
	            result = !(cheapCaseSubExpressionResult && endExpression);
	            
	            if(!result) {
	                    cheapCaseScore = Integer.MAX_VALUE;
	            }
	            

	            break;
	        // Add more cases for each known operator
	        default:
	            // Handle unknown operator
	            throw new IllegalArgumentException("Unknown operation: " + endOperation);
	    }
		    
		    
	          String startOperation = expressions.get(start + 1);
	          boolean rightSideSubExpressionResult = (costsMap[start + 2][end] < Integer.MAX_VALUE);
	          boolean firstExpression = Boolean.valueOf(expressions.get(start));
	          
	          previousCost = costsMap[start + 2][end];
	          
	          if(previousCost > Integer.MAX_VALUE/2) {
	              previousCost = 0; // maybe this will work?
	          }
	          
	          int expensiveCaseScore = ((end - start) - 1) + previousCost;
	          
	          System.out.println("spensive" + expensiveCaseScore);
		    
		    switch (startOperation) {
            case "and":
                
                // Handle 'and' operation
                boolean result = firstExpression && rightSideSubExpressionResult;
                
                if(!result) {
                    expensiveCaseScore = Integer.MAX_VALUE;
                }

                break;
            case "or":
                // Handle 'or' operation
                result = firstExpression || rightSideSubExpressionResult;
                
                if(!result) {
                    expensiveCaseScore = Integer.MAX_VALUE;
                }

                break; 
            case "xor":
                // Handle 'xor' operation
                result = firstExpression ^ rightSideSubExpressionResult;
                
               if(!result) {
                   expensiveCaseScore = Integer.MAX_VALUE;
               }

                break;
            case "nand":                
                // Handle 'nand' operation
                result = !(firstExpression && rightSideSubExpressionResult);
                
                if(!result) {
                    expensiveCaseScore = Integer.MAX_VALUE;
                }
                

                break;
            // Add more cases for each known operator
            default:
                // Handle unknown operator
                throw new IllegalArgumentException("Unknown operation: " + endOperation);
        }
			   
			// current faulty approach
			
			/*
			boolean leftSideSubExpressionResult = (costsMap[start][end - 2] < Integer.MAX_VALUE);
			boolean rightSideSubExpressionResult = (costsMap[start + 2][end] < Integer.MAX_VALUE);

			String startOperation = expressions.get(start + 1);
			String endOperation = expressions.get(end - 1);

			boolean firstExpression = Boolean.valueOf(expressions.get(start));
			boolean endExpression = Boolean.valueOf(expressions.get(end));

			int leftSideSolution = solve(Boolean.toString(leftSideSubExpressionResult), endOperation,
					Boolean.toString(endExpression)) + costsMap[start][end - 2];

			int rightSideSolution = solve(Boolean.toString(firstExpression), startOperation,
					Boolean.toString(rightSideSubExpressionResult));

			rightSideSolution = Math.max(rightSideSolution += (end - start - 1), 0);
*/
			/*System.out.println("\n\n\nEVALUATING:\n" + leftSideSubExpressionResult + " " + endOperation + " "
					+ endExpression + "\nAGAINST\n" + firstExpression + " " + startOperation + " "
					+ rightSideSubExpressionResult);

			System.out.println("SOLUTIONS!\n" + leftSideSolution + "R:" + rightSideSolution);
	
			*/
			
			
			return Math.min(cheapCaseScore, expensiveCaseScore); // we must assure it does not go below 0!

		}

	}

	public boolean getBooleanFromCost(int i) {
		return (i < Integer.MAX_VALUE);

	}

	/**
	 * 
	 * @param expr The expression to be evaluated
	 * @return Used for testing/validation purposes.
	 */

	/*
	 * public boolean evaluateExpressionRange(int start, int end) {
	 * 
	 * /* case end - start = 0
	 * 
	 * just evaluate the individual expression
	 * 
	 * 
	 * if (end - start == 0) {
	 * 
	 * if (isBool(expressionMap[start][start])) { boolean answer =
	 * solve(expressionMap[start][start]);
	 * 
	 * if (answer) { costsMap[start][end] = 0; } else if
	 * (expressions.get(end).equals("false")) { costsMap[start][end] =
	 * Integer.MAX_VALUE; }
	 * 
	 * expressionMap[start][end] = Boolean.toString(answer); return answer;
	 * 
	 * }
	 * 
	 * throw new IllegalArgumentException("Single expression but not a boolean!"); }
	 * 
	 * /* case end - start = 1
	 * 
	 * in this case, it is not + a primitive, invert the primitive
	 * 
	 * 
	 * if (end - start == 1) { if (expressions.get(start) == "not" &&
	 * isBool(expressions.get(end))) {
	 * 
	 * boolean answer = !Boolean.parseBoolean(expressions.get(end));
	 * 
	 * expressionMap[start][end] = Boolean.toString(answer);
	 * 
	 * return answer;
	 * 
	 * }
	 * 
	 * throw new IllegalArgumentException("the first of two should not be 'not'!");
	 * 
	 * }
	 * 
	 * /* case end - start = 2
	 * 
	 * in this case, it is primitive, operator, primitive.
	 * 
	 * 
	 * if (end - start == 2) { boolean answer = solve(expressionMap[start][start],
	 * expressionMap[start + 1][start + 1], expressionMap[end][end]);
	 * 
	 * expressionMap[start][end] = Boolean.toString(answer);
	 * 
	 * return answer; }
	 * 
	 * /* case end - start = > 2
	 * 
	 * in this case we will have to make a decision on which to parenthesize.
	 * 
	 * 
	 * throw new IllegalArgumentException("We don't know how to do " + (end - start)
	 * + " expressions yet :(");
	 * 
	 * }
	 */
}
