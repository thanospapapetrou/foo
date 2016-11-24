package com.github.thanospapapetrou.funcky;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.thanospapapetrou.funcky.parser.Token;
import com.github.thanospapapetrou.funcky.runtime.libraries.Booleans;

/**
 * Run tests.
 * 
 * @author thanos
 */
public class FunckyTest {
	private static final String CLASSPATH_ROOT = "/";
	private static final String COMMENT = String.valueOf(Character.toChars(Token.COMMENT.getCode()));
	private static final String PROVIDER = "funcky";

	private final FunckyScriptEngineFactory factory;

	@DataProvider(name = PROVIDER, parallel = true)
	private static Object[][] listTests() throws IOException, URISyntaxException {
		final List<Object[]> tests = new ArrayList<Object[]>();
		for (final File test : new File(FunckyTest.class.getResource(CLASSPATH_ROOT).toURI()).listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file.isFile();
			}
		})) {
			try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(test), StandardCharsets.UTF_8))) {
				String expression = null;
				while ((expression = reader.readLine()) != null) {
					if ((!expression.isEmpty()) && (!expression.startsWith(COMMENT))) {
						tests.add(new Object[] {expression});
					}
				}
			}
		}
		return tests.toArray(new Object[][] {});
	}

	private FunckyTest(final String expression) {
		factory = new FunckyScriptEngineFactory();
	}

	@Test(dataProvider = PROVIDER)
	private void test(final String expression) throws ScriptException {
		final FunckyScriptEngine engine = factory.getScriptEngine();
		Assert.assertEquals(engine.eval(expression), engine.getReference(Booleans.class, Booleans.TRUE).eval(), expression);
	}
}
