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

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.thanospapapetrou.funcky.parser.Token;
import com.github.thanospapapetrou.funcky.runtime.libraries.Booleans;

public class FunckyTest implements FileFilter {
	private static final String CLASSPATH_ROOT = "/";

	private final FunckyScriptEngine engine;

	public FunckyTest() {
		engine = new FunckyScriptEngineFactory().getScriptEngine();
	}

	@Override
	public boolean accept(final File file) {
		return file.isFile();
	}

	@DataProvider(name = "tests")
	public Iterator<Object[]> listTests() throws URISyntaxException {
		final Iterator<File> tests = Arrays.asList(new File(getClass().getResource(CLASSPATH_ROOT).toURI()).listFiles(this)).iterator();
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
				tests.remove();
			}
		};
	}

	@Test(dataProvider = "tests")
	public void test(final File test) throws IOException, ScriptException {
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(test), StandardCharsets.UTF_8))) {
			engine.getContext().setAttribute(ScriptEngine.FILENAME, test.getCanonicalPath(), ScriptContext.ENGINE_SCOPE);
			final String comment = String.valueOf(Character.toChars(Token.COMMENT.getCode()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if ((!line.isEmpty()) && (!line.startsWith(comment))) {
					Assert.assertEquals(engine.eval(line), engine.getReference(Booleans.class, Booleans.TRUE).eval(), line);
				}
			}
		}
	}
}
