package com.frostwire.jlibtorrent.tools;

import java.lang.reflect.Constructor;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Main {

    public static void main(String[] args) {
        args = new String[]{"ltversion"};
        if (args == null || args.length == 0) {
            System.out.println("usage: <toolname> args");
            System.exit(1);
        }

        try {
            String toolName = args[0];
            String toolClass = null;
            if (toolName.equals("announce")) {
                toolClass = "Announce";
            } else if (toolName.equals("getpeers")) {
                toolClass = "GetPeers";
            } else if (toolName.equals("ltversion")) {
                toolClass = "LTVersion";
            } else if (toolName.equals("mktorrent")) {
                toolClass = "MkTorrent";
            } else if (toolName.equals("readtorrent")) {
                toolClass = "ReadTorrent";
            } else {
                System.out.println("Tool name not supported");
                System.exit(1);
            }

            Class<?> clazz = Class.forName("com.frostwire.jlibtorrent.tools." + toolClass);
            Constructor<?> constructor = clazz.getConstructor(String[].class);

            String[] toolArgs = new String[args.length - 1];
            System.arraycopy(args, 1, toolArgs, 0, toolArgs.length);
            constructor.newInstance((String[]) toolArgs);
        } catch (Throwable e) {
            System.out.println("usage: <toolname> args");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
