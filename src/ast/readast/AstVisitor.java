package ast.readast;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;

public class AstVisitor extends ASTVisitor {

	public boolean DEBUG;
	
	public boolean visit(IfStatement ifStatement) {
		boolean result = super.visit(ifStatement);
		
		Expression expression = ifStatement.getExpression();
		
		System.out.println("Expression: " + expression.toString());
		
		return result;
	}

}
