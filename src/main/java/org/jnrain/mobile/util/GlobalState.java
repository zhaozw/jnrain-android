/*
 * Copyright 2013 JNRain
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jnrain.mobile.util;

public class GlobalState {
    protected static boolean _cookieInited = false;
    protected static String _userName = "";

    public static synchronized boolean getCookieInited() {
        return _cookieInited;
    }

    public static void setCookieInited(boolean inited) {
        _cookieInited = inited;
    }

    public static synchronized String getUserName() {
        return _userName;
    }

    public static synchronized void setUserName(String userName) {
        _userName = userName;
    }
}
