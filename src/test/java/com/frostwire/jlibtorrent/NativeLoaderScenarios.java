package com.frostwire.jlibtorrent;

/**
 * Scenarios for {@link NativeLibraryLoaderProcessTest}: {@code NativeLibraryLoader.load} loads a
 * native library once per JVM, so each scenario runs in a fresh JVM
 * ({@code java ... NativeLoaderScenarios <name>}) and prints {@code RESULT=<what was loaded>} or
 * {@code ERROR=<exception>: <message>}.
 */
public final class NativeLoaderScenarios {

    private NativeLoaderScenarios() {
    }

    public static void main(String[] args) {
        // a class loader that sees no jar: nothing is bundled
        ClassLoader empty = new ClassLoader(null) {
        };
        ClassLoader loader = args.length > 1 && args[1].equals("empty")
                ? empty : NativeLoaderScenarios.class.getClassLoader();
        try {
            if (args[0].equals("android")) {
                System.setProperty("java.runtime.name", "Android Runtime");
            }
            String loaded = NativeLibraryLoader.load("2.1.2.0", loader);
            System.out.println("RESULT=" + loaded);
            if (args[0].equals("twice")) {
                System.out.println("AGAIN=" + NativeLibraryLoader.load("2.1.2.0", empty));
                System.out.println("REMEMBERED=" + NativeLibraryLoader.loadedLibrary());
                try {
                    NativeLibraryLoader.checkNativeVersion("2.1.2.0", "2.0.15.0");
                } catch (LinkageError e) {
                    System.out.println("LINKAGE=" + e.getMessage());
                }
            }
        } catch (Throwable e) {
            System.out.println("ERROR=" + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        System.out.println("DONE");
        System.out.flush();
    }
}
