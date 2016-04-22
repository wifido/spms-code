package com.sf.module.esbinterface.util;

import java.lang.management.ManagementFactory;

public class VMUtils {
    private static final String currentMachine;
    private static final String currentMachineName;

    static {
        currentMachine = ManagementFactory.getRuntimeMXBean().getName();
        currentMachineName = currentMachine.substring(currentMachine.indexOf('@') + 1);
    }

    public static String getCurrentMachineName() {
        return currentMachineName;
    }
}
