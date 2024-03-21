package src;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ExpressionEvaluator e = new ExpressionEvaluator();

        System.out.println("Dynamic Programming Expression Evalauator");

        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println("Enter an expression (comma separated) or type 'quit' to exit:");

            userInput = scanner.nextLine().trim().toLowerCase();

            if ("quit".equalsIgnoreCase(userInput)) {
                System.out.println("Exiting program...");
                break;
            }
            
            String result = "";

            try {
            processUserInput(userInput, e);
            
            e.evaluateExpressions();

            result = (e.returnResult() < Integer.MAX_VALUE) ? String.valueOf(e.returnResult())
                    : "Impossible to evaluate as true!";
            
            }catch(IllegalArgumentException ex) {
                System.out.println("Error encountered.\n");
                ex.printStackTrace();
                
                result = "";
            }
            System.out.println("Evaluation Result: " + result);

            e.clearExpressions();

            System.out.println("Try another expression? (yes/no)");
            userInput = scanner.nextLine().trim();
            
            if("map".equalsIgnoreCase(userInput)) {
                e.arraysToString();
            }
            else if ("no".equalsIgnoreCase(userInput)) {
                System.out.println("Exiting program...");
                break;
            }
        }

        scanner.close();
    }

    private static void processUserInput(String userInput, ExpressionEvaluator e) {
        String[] expressions = userInput.split(",");
        for (String expression : expressions) {
            e.addExpression(expression.trim());
        }
    }

}
