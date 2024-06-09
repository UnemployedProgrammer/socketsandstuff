package com.sebastian.sockets.updatesystem;

import com.sebastian.sockets.Sockets;

public class UrlConstants {
    public static String GOOGLE_API_REQUEST_URL = "https://script.google.com/macros/s/AKfycbzoGKu35lASK_M3tr0oM00L2uJxRl_2PQe93tFHdX3aqqCE-eyItHhZrqg3TjPS9BtE/exec";

    public static String getApiRequestUrlForVersion() {return GOOGLE_API_REQUEST_URL+"?q=vm&gv="+ Sockets.GAMEVER;}


}
