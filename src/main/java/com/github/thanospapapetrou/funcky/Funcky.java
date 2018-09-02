package com.github.thanospapapetrou.funcky;

import java.io.IOException;

public class Funcky {
    public static void main(final String[] arguments) {
        try {
            final FunckyEngineFactory factory = new FunckyEngineFactory();
            System.out.println("Language: " + factory.getLanguageName());
            System.out.println("Language Version: " + factory.getLanguageVersion());
            System.out.println("Engine: " + factory.getEngineName());
            System.out.println("Engine Version: " + factory.getEngineVersion());
            System.out.println("Names: " + factory.getNames());
            System.out.println("MIME Types: " + factory.getMimeTypes());
            System.out.println("Extensions: " + factory.getExtensions());
            System.out.println(
                    "ENGINE Parameter: " + factory.getParameter(FunckyEngineFactory.ENGINE));
            System.out.println("ENGINE_VERSION Parameter: "
                    + factory.getParameter(FunckyEngineFactory.ENGINE_VERSION));
            System.out.println(
                    "EXTENSION Parameter: " + factory.getParameter(FunckyEngineFactory.EXTENSION));
            System.out.println(
                    "LANGUAGE Parameter: " + factory.getParameter(FunckyEngineFactory.LANGUAGE));
            System.out.println("LANGUAGE_VERSION Parameter: "
                    + factory.getParameter(FunckyEngineFactory.LANGUAGE_VERSION));
            System.out.println(
                    "MIME_TYPE Parameter: " + factory.getParameter(FunckyEngineFactory.MIME_TYPE));
            System.out.println("NAME Parameter: " + factory.getParameter(FunckyEngineFactory.NAME));
            System.out.println(
                    "THREADING Parameter: " + factory.getParameter(FunckyEngineFactory.THREADING));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
