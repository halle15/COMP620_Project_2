package src;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		NewExpressionEvaluator e = new NewExpressionEvaluator();
	
		ArrayList<String> input = new ArrayList<String>();
		
		input.add("true");
		
		System.out.println(e.evaluate(input));
	}

}
