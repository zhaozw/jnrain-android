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

import java.io.File;
import java.io.InputStream;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.jnrain.mobile.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.simple.BigBinaryRequest;


/*
 * this code taken from
 * stackoverflow.com/questions/7424512/android-html-imagegetter-as-asynctask
 * migrated to RoboSpice with the example robospice-sample-core.
 */
public class URLImageGetter implements ImageGetter {
    private static final String TAG = "URLImageGetter";

    protected Context _c;
    protected View _container;
    protected String _baseURL;
    protected Activity _activity;
    protected SpiceRequestListener<InputStream> _listener;

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh
     * the container
     * 
     * @param t
     * @param c
     * @param baseURL
     */
    public URLImageGetter(
            Activity activity,
            View t,
            Context c,
            String baseURL,
            SpiceRequestListener<InputStream> listener) {
        _activity = activity;
        _c = c;
        _container = t;
        _baseURL = baseURL;
        _listener = listener;
    }

    protected String transformSource(String source) {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            // Don't transform absolute source for now
            return source;
        }

        return transformRelativeSource(source);
    }

    protected String transformRelativeSource(String source) {
        return source;
    }

    public Drawable getDrawable(String source) {
        Log.v(TAG, "Image request: " + source);

        // Transform source
        String actualSource = transformSource(source);
        Log.v(TAG, "Transformed => " + actualSource);

        // set image to loading state
        URLDrawable urlDrawable = new URLDrawable(_activity
            .getResources()
            .getDrawable(R.drawable.loading_pic));

        // get the actual source
        String absoluteURL;

        if (actualSource.startsWith("http://")
                || actualSource.startsWith("https://")) {
            absoluteURL = actualSource;
        } else {
            absoluteURL = _baseURL + actualSource;
        }

        // make the request
        // hash the url
        // NOTE: we cannot directly use DigestUtils.sha1Hex, because of the
        // Android-specific quirk described in
        // stackoverflow.com/questions/9126567/method-not-found-using-digestutils-in-android
        String absURLHash = new String(Hex.encodeHex(DigestUtils
            .sha1(absoluteURL)));

        File cacheFile = new File(
                _activity.getApplication().getCacheDir(),
                absURLHash);
        BigBinaryRequest req = new BigBinaryRequest(absoluteURL, cacheFile);

        _listener.makeSpiceRequest(
                req,
                absURLHash,
                DurationInMillis.ONE_DAY,
                new URLImageRequestListener(
                        (TextView) _container,
                        urlDrawable,
                        absoluteURL,
                        _activity));

        // return reference to URLDrawable where I will change with actual
        // image
        // from
        // the src tag
        return urlDrawable;
    }
}
