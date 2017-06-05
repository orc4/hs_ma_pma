package de.hsmannheim.pma.run.storage;


import de.hsmannheim.pma.run.model.MyCredentials;

/**
 * Created by aaron on 31.05.17.
 */

public class WebConnectionFactory {
    // Private Variablen
    private static WebConnectionFactory instance = new WebConnectionFactory();
    private WebConnection myWebConnection;
    // Methoden
    public static WebConnectionFactory getInstance() {
        return (instance);
    }

    public WebConnection getWebConnection(MyCredentials myCredentials) {
        if(myWebConnection==null){
            myWebConnection = new WebConnectionImpl(myCredentials);
        }
        return myWebConnection;
    }
}
