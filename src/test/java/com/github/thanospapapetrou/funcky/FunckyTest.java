package com.github.thanospapapetrou.funcky;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

import javax.script.ScriptException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.thanospapapetrou.funcky.parser.Token;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Boolean;
import com.github.thanospapapetrou.funcky.runtime.libraries.Booleans;

/**
 * Run tests.
 * 
 * @author thanos
 */
public class FunckyTest {
	private static final String CLASSPATH_ROOT = "/";
	private static final String COMMENT = String.valueOf(Character.toChars(Token.COMMENT.getCode()));
	private static final String FAILED = "%1$s in file %2$s at line %3$s";
	private static final String TESTS = "tests";

	private final FunckyScriptEngineFactory factory;

	@DataProvider(name = TESTS, parallel = true)
	private static Iterator<Object[]> listTests() throws URISyntaxException {
		final Iterator<File> tests = Arrays.asList(new File(FunckyTest.class.getResource(CLASSPATH_ROOT).toURI()).listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file.isFile();
			}
		})).iterator();
		return new Iterator<Object[]>() {
			@Override
			public boolean hasNext() {
				return tests.hasNext();
			}

			@Override
			public Object[] next() {
				return new Object[] {tests.next()};
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	private FunckyTest(final String expression) {
		factory = new FunckyScriptEngineFactory();
	}

	@Test(dataProvider = TESTS)
	private void test(final File test) throws IOException, ScriptException {
		final FunckyScriptEngine engine = factory.getScriptEngine();
		final Boolean booleanTrue = engine.getLiteral(Booleans.class, Booleans.TRUE);
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(test), StandardCharsets.UTF_8))) {
			String line = null;
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				counter++;
				if ((!line.isEmpty()) && (!line.startsWith(COMMENT))) {
					Assert.assertEquals(engine.eval(line), booleanTrue, String.format(FAILED, line, test, counter));
				}
			}
		}

	}
}
