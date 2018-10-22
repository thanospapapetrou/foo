package com.github.thanospapapetrou.funcky;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.script.ScriptEngineFactory;

/**
 * Class implementing a Funcky engine factory.
 *
 * @author thanos
 */
public class FunckyEngineFactory implements ScriptEngineFactory {
    /**
     * Key corresponding to engine name.
     */
    public static final String ENGINE = "ScriptEngine.ENGINE";

    /**
     * Key corresponding to engine version.
     */
    public static final String ENGINE_VERSION = "ScriptEngine.ENGINE_VERSION";

    /**
     * Key corresponding to extension.
     */
    public static final String EXTENSION = "ScriptEngine.EXTENSION";

    /**
     * Key corresponding to language name.
     */
    public static final String LANGUAGE = "ScriptEngine.LANGUAGE";

    /**
     * Key corresponding to language version.
     */
    public static final String LANGUAGE_VERSION = "ScriptEngine.LANGUAGE_VERSION";

    /**
     * Key corresponding to MIME type.
     */
    public static final String MIME_TYPE = "ScriptEngine.MIME_TYPE";

    /**
     * Key corresponding to short name.
     */
    public static final String NAME = "ScriptEngine.NAME";

    /**
     * Key corresponding to threading model.
     */
    public static final String THREADING = "THREADING";

    private static final String DELIMITER = ",\\s*";
    private static final String PROPERTIES = "/funcky.properties";
    private final Properties properties;

    /**
     * Construct a new Funcky engine factory.
     * 
     * @throws IOException
     *             if any errors occur while loading Funcky properties.
     */
    public FunckyEngineFactory() throws IOException {
        properties = new Properties();
        properties.load(FunckyEngineFactory.class.getResourceAsStream(PROPERTIES));
    }

    @Override
    public String getEngineName() {
        return getStringProperty(ENGINE);
    }

    @Override
    public String getEngineVersion() {
        return getStringProperty(ENGINE_VERSION);
    }

    @Override
    public List<String> getExtensions() {
        return getListProperty(EXTENSION);
    }

    @Override
    public String getLanguageName() {
        return getStringProperty(LANGUAGE);
    }

    @Override
    public String getLanguageVersion() {
        return getStringProperty(LANGUAGE_VERSION);
    }

    @Override
    public String getMethodCallSyntax(final String object, final String method,
            final String... arguments) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getMimeTypes() {
        return getListProperty(MIME_TYPE);
    }

    @Override
    public List<String> getNames() {
        return getListProperty(NAME);
    }

    @Override
    public String getOutputStatement(final String message) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getParameter(final String key) {
        return (EXTENSION.equals(key) || MIME_TYPE.equals(key) || NAME.equals(key))
                ? getListProperty(key).get(0) : getStringProperty(key);
    }

    @Override
    public String getProgram(final String... statements) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FunckyEngine getScriptEngine() {
        final FunckyEngine engine = new FunckyEngine(this);
        // TODO set global scope bindings
        return engine;
    }

    private String getStringProperty(final String key) {
        return properties.getProperty(key);
    }

    private List<String> getListProperty(final String key) {
        final String property = properties.getProperty(key);
        return (property == null) ? null
                : Collections.unmodifiableList(Arrays.asList(property.split(DELIMITER)));
    }
}
