package com.frostwire.jlibtorrent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calls every public no-argument accessor of an object, to exercise plain getter classes and to
 * report the accessors that throw.
 *
 * @author gubatron
 * @author aldenml
 */
final class Accessors {

    private static final Set<String> SKIPPED = new HashSet<>(Arrays.asList(
            "wait", "notify", "notifyAll", "getClass", "clone", "delete"));

    private Accessors() {
    }

    /** Invokes the accessors and returns a description of each one that threw. */
    static List<String> touch(Object o) {
        List<String> failures = new ArrayList<>();
        for (Method m : o.getClass().getMethods()) {
            if (m.getParameterCount() != 0 || Modifier.isStatic(m.getModifiers())
                    || SKIPPED.contains(m.getName()) || m.getDeclaringClass() == Object.class) {
                continue;
            }
            try {
                m.setAccessible(true);
                m.invoke(o);
            } catch (InvocationTargetException e) {
                failures.add(o.getClass().getSimpleName() + "." + m.getName() + "() threw " + e.getCause());
            } catch (ReflectiveOperationException | RuntimeException e) {
                failures.add(o.getClass().getSimpleName() + "." + m.getName() + "() could not be called: " + e);
            }
        }
        return failures;
    }
}
