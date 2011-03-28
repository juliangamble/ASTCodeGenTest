package ast.testgen;

import static org.eclipse.jdt.core.dom.ASTNode.IF_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.PARENTHESIZED_EXPRESSION;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.REMAINDER;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;


public class CodeGenDemo {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws BadLocationException 
	 * @throws MalformedTreeException 
	 */
	public static void main(String[] args) throws IOException, MalformedTreeException, BadLocationException {
		CodeGenDemo test = new CodeGenDemo();
		test.processJavaFile(new File("./src/ast/testgen/HelloWorld.java"));
	}

	@SuppressWarnings("unchecked")
	private void processJavaFile(File file) throws IOException, MalformedTreeException, BadLocationException {
	    String source = FileUtils.readFileToString(file);
	    Document document = new Document(source);
	    ASTParser parser = ASTParser.newParser(AST.JLS3);
	    parser.setSource(document.get().toCharArray());
	    CompilationUnit unit = (CompilationUnit)parser.createAST(null);
	    unit.recordModifications();

	    // to get the imports from the file
	    List<ImportDeclaration> imports = unit.imports();
	    //for (ImportDeclaration i : imports) {
	        //System.out.println(i.getName().getFullyQualifiedName());
	    //}

	    // to create a new import
	    AST ast = unit.getAST();
	    ImportDeclaration id = ast.newImportDeclaration();
	    String classToImport = "ast.testgen.HelloWorld";
	    
	    
	    id.setName(ast.newName(classToImport.split("\\.")));
	    unit.imports().add(id); // add import declaration at end

	    // to save the changed file
	    TextEdit edits = unit.rewrite(document, null);
	    edits.apply(document);
	    FileUtils.writeStringToFile(file, document.get());

	    // to iterate through methods
	    List<AbstractTypeDeclaration> types = unit.types();
	    for (AbstractTypeDeclaration type : types) {
	        if (type.getNodeType() == ASTNode.TYPE_DECLARATION) {
	        	//System.out.println("type: "+type.getName());
	            // Class def found
	            List<BodyDeclaration> bodies = type.bodyDeclarations();
	            for (BodyDeclaration body : bodies) {
	                if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
	                    MethodDeclaration method = (MethodDeclaration)body;
	                    //System.out.println("name: " + method.getName().getFullyQualifiedName());
	                    
	                    Block methodBody = method.getBody();
	                    List<Statement> methodStatements = methodBody.statements();
	                    for (Statement methodStatement: methodStatements ) {
	                    	String statementString = methodStatement.toString();
	                    	//methodStatement.

	                    	//System.out.println("statementString:" + statementString);
	                    	if (isIf(methodStatement)) {
	                    		//System.out.println("This is an if");
	                    	
	                    		IfStatement ifStatement = (IfStatement) methodStatement;
	                    		Expression expression = ifStatement.getExpression();
	                    		//System.out.println(expression.toString());
	                    		//System.out.println("expression:" + expression.getClass());
	                    		
	                    		if (expression.getNodeType() == INFIX_EXPRESSION) {
	                    			//System.out.println("Infix Expression");
	                    			InfixExpression infixExpression = (InfixExpression)expression;
	                    			//System.out.println(infixExpression.hasExtendedOperands());
	                    			//List operands = infixExpression.extendedOperands();
	                    			//for (Object operand: operands) {
	                    			//	System.out.println("operands: " + operand);
	                    			//}
	                    			Expression leftOperand = infixExpression.getLeftOperand();
	                    			Expression rightOperand = infixExpression.getRightOperand();
	                    			//System.out.println(leftOperand);
	                    			//System.out.println(rightOperand);
	                    			
	                    			//System.out.println(leftOperand.getClass());
	                    			if (leftOperand.getNodeType() == PARENTHESIZED_EXPRESSION) {
	                    				ParenthesizedExpression pe = (ParenthesizedExpression)leftOperand;
	                    				Expression expr = pe.getExpression();
	                    				//System.out.println(expr.getClass());
	                    				
	                    				if (expr.getNodeType() == INFIX_EXPRESSION) {
	                    					InfixExpression infixExpr = (InfixExpression)expr;
	                    					Operator operator =  infixExpr.getOperator();
	                    					//System.out.println(operator.getClass());
	                    					//System.out.println(operator.equals(REMAINDER));
	                    					
	                    					Expression rightOper = infixExpr.getRightOperand();
	                    					//System.out.println(rightOper.getClass());
	                    					
	                    					
	                    					NumberLiteral numberliteral = (NumberLiteral)rightOper;
	                    					
	                    					// Assume integer for remainder
	                    					Integer number = Integer.valueOf(numberliteral.toString());
	                    					
	                    					//System.out.println(numberliteral);
	                    					List<Integer> remainderFalseList = remainderFalseListTest(number);
	                    					
	                    					String origClassName = ""+type.getName();
	                    					
	                    					String unitTestClassName = origClassName + "Test";
	                    					String methodTestName = "test" + method.getName().getFullyQualifiedName();
	                    				
	                    					String instantiateOrigClass = "    " + origClassName + " t  =  new " + origClassName + "();\n";
	                    					
	                    					String testStatement = generateTrueTest(number, method.getName().getFullyQualifiedName());
	                    					String testStatements = generateTrueTest(remainderFalseList, method.getName().getFullyQualifiedName());
	                    					
	                    					String testClassImports = 
	                    						"import static org.junit.Assert.assertFalse;\n" + 
	                    						"import static org.junit.Assert.assertTrue;\n" +
	                    						"import org.junit.Test;\n";
	                    					
	                    					String testClassName = "public class "+unitTestClassName+" {\n";
	                    					
	                    					String testClassMethodName = "  @Test\n" +
	                    						"  public void "+methodTestName+"() {\n";
	                    					
	                    					String actualTests = testStatement + testStatements;
	                    					
	                    					String closeClass = "}\n";
	                    					
	                    					String closeTestMethod = "  " +  closeClass ;
	                    					
	                    					String testClass = testClassImports
	                    					+ testClassName
	                    					+ testClassMethodName
	                    					+ instantiateOrigClass
	                    					+ actualTests
	                    					+ closeTestMethod
	                    					+ closeClass;
	                    					
	                    					System.out.println(testClass);
	                    				}	                    				
	                    			}
	                    		}
	                    		
	                    		//expression.getNodeType();
	                    		//expression.
	                    		
	                    	}	                    			
	                    }	                    
	                }
	            }
	        }
	    }		
	}

	private String generateTrueTest(List<Integer> boundaryList,
			String testMethodNameOrig) {
		String result = "";
		
		for (Integer falseBoundary : boundaryList) {
			result += "    assertTrue(t."+testMethodNameOrig+"("+falseBoundary+"));\n";
		}
		
		return result;
	}

	private String generateTrueTest(Integer boundary,String testMethodNameOrig) {
		
		return "    assertTrue(t."+testMethodNameOrig+"("+boundary+"));\n";
	}

	private List<Integer> remainderFalseListTest(Integer boundary) {
		List<Integer> result = new ArrayList<Integer>();

		//result.add(boundary);
		result.add(boundary - 1);
		result.add(boundary + 1);
		
		return result;
	}

	private boolean isIf(Statement methodStatement) {
		if (methodStatement.getNodeType() == IF_STATEMENT)
			return true;
		else
			return false;
	}
}