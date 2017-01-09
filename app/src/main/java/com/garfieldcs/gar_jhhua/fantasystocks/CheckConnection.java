/* Project: Ivory Rain
   1/9/2017
   This class checks and returns the status of connectivity to the internet.
   Use this so we don't have to create new ConnectivityManager every time.
*/

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class CheckConnection {
    private ConnectivityManager cManager;
    private Context c;
    private NetworkInfo networkInfo;
    private Toast t;

    public CheckConnection(Context c) {
        cManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = cManager.getActiveNetworkInfo();
        t = new Toast(c);
    }

    //Returns connection status each time it is called
    public boolean isConnected() {
        if (networkInfo != null && networkInfo.isConnected()) {
            t.makeText(c, "Connection Success", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            t.makeText(c, "Connection Failed", Toast.LENGTH_SHORT).show();
            return false;
        }
        /* If we don't need toast then we could do
           return networkInfo.isConnected();
         */
    }
}
