package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.TorrentAlert;
import com.frostwire.jlibtorrent.swig.libtorrent;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class AlertsSwitch {

    public static void main(String[] args) throws Exception {
        printForAlerts();
        System.out.println("=============================");
        printForTorrentAlertsAdapter();
        System.out.println("=============================");
        printForAlertType();
    }

    private static void printForAlerts() throws Exception {
        Class[] arr = getSwigAlerts();

        for (int i = 0; i < arr.length; i++) {
            String s = "arr[" + i + "] = new CastLambda() { @Override public Alert cast(alert a) { return new ";
            if (arr[i] != null) {
                String c = capitalizeAlertTypeName(arr[i].getSimpleName());
                s += c + "(cast_to_" + arr[i].getSimpleName() + "(a));";
            } else {
                s += "GenericAlert(a);";
            }

            s += "}};";

            System.out.println(s);
        }
    }

    private static void printForTorrentAlertsAdapter() throws Exception {
        Class[] arr = getSwigAlerts();

        for (int i = 0; i < arr.length; i++) {
            String s = "arr[" + i + "] = new InvokeLambda() { @Override public void invoke(TorrentAlertAdapter l, Alert a) { ";
            if (arr[i] != null) {
                String c = capitalizeAlertTypeName(arr[i].getSimpleName());
                Class<?> alertClass = Class.forName("com.frostwire.jlibtorrent.alerts." + c);
                if (TorrentAlert.class.isAssignableFrom(alertClass)) {
                    String cc = Character.toLowerCase(c.charAt(0)) + c.substring(1);
                    cc = cc.replace("Alert", "");
                    s += "l." + cc + "((" + c + ")a);";
                    s += "}};";
                } else {
                    s = "arr[" + i + "] = null;";
                }
            } else {
                s = "arr[" + i + "] = null;";
            }

            System.out.println(s);
        }
    }

    private static void printForAlertType() throws Exception {
        Class[] arr = getSwigAlerts();

        for (int i = 0; i < arr.length; i++) {
            String s = "arr[" + i + "] = ";
            if (arr[i] != null) {
                String c = arr[i].getSimpleName().toUpperCase();
                c = c.replace("_ALERT", "");
                s += c + ";";
            } else {
                s += "UNKNOWN;";
            }

            System.out.println(s);
        }
    }

    private static Class[] getSwigAlerts() throws Exception {
        int n = 0;
        Class[] arr = new Class[libtorrent.num_alert_types];
        for (Class c : getClasses("com.frostwire.jlibtorrent.swig")) {
            if (c.getName().endsWith("_alert")) {
                Field f = c.getDeclaredField("alert_type");
                int type = f.getInt(null);

                arr[type] = c;
                n++;
            }
        }

        n++; // 63 - rss_alert
        n++; // 72 - rss_item_alert
        if (n != arr.length) {
            throw new Exception("mismatch in number of alerts and types");
        }

        return arr;
    }

    private static Class[] getClasses(String packageName) throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = cl.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    private static List<Class> findClasses(File directory, String packageName) throws Exception {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private static String capitalizeAlertTypeName(String s) {
        StringBuilder sb = new StringBuilder(s.length());

        boolean capitalize = true;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (capitalize) {
                sb.append(Character.toUpperCase(ch));
                capitalize = false;
            } else if (ch == '_') {
                capitalize = true;
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }
}
