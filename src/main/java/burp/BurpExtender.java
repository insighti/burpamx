package burp;

import com.insighti.burpamx.AmxSessionHandler;
import com.insighti.burpamx.AmxSuiteTab;

import java.io.PrintWriter;

public class BurpExtender implements IBurpExtender {

    public final static String EXTENSION_NAME = "AMX Authorization Burp Suite Extension";
    public static boolean DEBUG = false;

    public static PrintWriter stdout, stderr;
    public static IBurpExtenderCallbacks callbacks;
    public static IExtensionHelpers helpers;

    private static AmxSessionHandler amxSessionHandler;
    public static AmxSuiteTab amxSuiteTab;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        BurpExtender.stdout = new PrintWriter(callbacks.getStdout(), true);
        BurpExtender.stderr = new PrintWriter(callbacks.getStderr(), true);
        BurpExtender.callbacks = callbacks;
        BurpExtender.helpers = callbacks.getHelpers();

        amxSessionHandler = new AmxSessionHandler();
        amxSuiteTab = new AmxSuiteTab();

        callbacks.setExtensionName(EXTENSION_NAME);
        callbacks.registerSessionHandlingAction(amxSessionHandler);
        callbacks.addSuiteTab(amxSuiteTab);
    }
}
