package com.example.igorkutyrev.arc_control;

import android.app.Activity;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.OkHttpClient.Builder;
//import okhttp3.Request;
//import okhttp3.Response;

abstract class CommandSender
{
    private static RequestQueue requestQueue;
    private final String address;
    private final WeakReference<Activity> activity;

    static
    {
        requestQueue = new RequestQueue (new NoCache (),
                                         new BasicNetwork (new HurlStack ()),
                                         8);
        requestQueue.start ();
    }

    public CommandSender (String address, Activity activity)
    {
        this.address = address;
        this.activity = new WeakReference<> (activity);
    }

    public void send (int m1speed, int m2speed)
    {
        String url = formUrl (address, m1speed, m2speed);
        Request request = new StringRequest (
                Request.Method.HEAD,
                url,
                new Response.Listener<String> ()
                {
                    @Override
                    public void onResponse (String response)
                    {
                        activity.get ().runOnUiThread (new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                CommandSender.this.onSuccess ();
                            }
                        });
                    }
                },
                new Response.ErrorListener ()
                {
                    @Override
                    public void onErrorResponse (VolleyError error)
                    {
                        activity.get ().runOnUiThread (new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                CommandSender.this.onFailure ();
                            }
                        });
                    }
                });
        requestQueue.add (request);
    }

    private static String formUrl (String address, int m1speed, int m2speed)
    {
        String dir = "";
        dir += m1speed == 0 ? "x" : m1speed > 0 ? "f" : "b"; // Важно! Обязательна посылка стоп-значения
        dir += m2speed == 0 ? "x" : m2speed > 0 ? "f" : "b";
        return "http://" + address
               + "/drive"
               + "?" + "m1s=" + Math.abs (m1speed)
               + "&" + "m2s=" + Math.abs (m2speed)
               + "&" + "dir=" + dir;
    }

    /*
     * Эти функции надо доопределить на месте!
     */
    abstract protected void onSuccess ();
    abstract protected void onFailure ();

//    private class HeadRequest extends Request <Void>
//    {
//        private final Response.Listener<Void> mListener;
//
//        public HeadRequest (String url, Response.Listener<Void> listener,
//                            Response.ErrorListener errorListener) {
//            super(Method.HEAD, url, errorListener);
//            mListener = listener;
//        }
//
//        @Override
//        protected void deliverResponse (String response) {
//            mListener.onResponse(response);
//        }
//
//        @Override
//        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//            String parsed;
//            try {
//                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            } catch (UnsupportedEncodingException e) {
//                parsed = new String(response.data);
//            }
//            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
//        }
//    }
}

//abstract class CommandSender
//{
//    private final String address;
//    static private OkHttpClient client = new OkHttpClient.Builder ()
//            .readTimeout (100, TimeUnit.MILLISECONDS)
//            .writeTimeout (100, TimeUnit.MILLISECONDS)
//            .build ();
//    private final WeakReference<Activity> activity;
//
//    private int successCounter = 0;
//    private int failureCounter = 0;
//
//    public CommandSender (String address, Activity activity)
//    {
//        this.address = address;
//        this.activity = new WeakReference<> (activity);
//    }
//
//    public void send (int m1speed, int m2speed)
//    {
//        final String url = formUrl (address, m1speed, m2speed);
//        final Request request = new Request.Builder ()
//                .url (url)
//                .head ()
//                .build ();
//        client.newCall(request).enqueue(new Callback ()
//        {
//            @Override
//            public void onFailure (Call c, IOException e)
//            {
//                activity.get ().runOnUiThread (new Runnable ()
//                {
//                    @Override
//                    public void run ()
//                    {
//                        CommandSender.this.onFailure ();
//                    }
//                });
//                failureCounter++;
//            }
//
//            @Override
//            public void onResponse (Call c, Response response) throws IOException
//            {
//                successCounter++;
//                if (!response.isSuccessful ())
//                    throw new IOException ("Unexpected code " + response);
//                activity.get ().runOnUiThread (new Runnable ()
//                {
//                    @Override
//                    public void run ()
//                    {
//                        Log.d ("CommandSender", url + " s: " + successCounter + " f: " + failureCounter);
//                        CommandSender.this.onSuccess ();
//                    }
//                });
//                //response.close ();
//            }
//        });
//    }
//
//    private static String formUrl (String address, int m1speed, int m2speed)
//    {
//        String dir = "";
//        dir += m1speed == 0 ? "x" : m1speed > 0 ? "f" : "b"; // Важно! Обязательна посылка стоп-значения
//        dir += m2speed == 0 ? "x" : m2speed > 0 ? "f" : "b";
//        return "http://" + address
//               + "/drive"
//               + "?" + "m1s=" + Math.abs (m1speed)
//               + "&" + "m2s=" + Math.abs (m2speed)
//               + "&" + "dir=" + dir;
//    }
//
//    /*
//     * Эти функции надо доопределить на месте!
//     */
//    abstract protected void onSuccess ();
//    abstract protected void onFailure ();
//}
