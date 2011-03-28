package ast.readast;

import java.io.File;
import java.io.IOException;

public class ReadASTDemo {
	public static void main(String[] args) throws IOException {
		ReadASTDemo demo = new ReadASTDemo();
		demo.run();
	}

	private void run() throws IOException {
		EclipseAstParser parser = new EclipseAstParser();
		AstVisitor visitor = parser.visitFile(new File("./src/ast/testgen/HelloWorld.java"));
	}
}
