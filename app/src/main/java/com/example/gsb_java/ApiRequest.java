package com.example.gsb_java;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ApiRequest {
    private static ApiRequest instance;
    private RequestQueue requestQueue;

    private ApiRequest(Context context) {
        requestQueue = getRequestQueue(context);
    }

    public static synchronized ApiRequest getInstance(Context context) {
        if (instance == null) {
            instance = new ApiRequest(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(Context ctx) {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static <T> void makeRequest(Context context, Object caller, String tag, String url, int RESTmethod, @Nullable JSONObject body, @Nullable T passValue) throws APICallbackException{
        makeRequest(context,caller,tag,url,RESTmethod,body,passValue,null);
    }

    public static <T> void makeRequest(Context context, Object caller, String tag, String url, int RESTmethod, @Nullable JSONObject body, @Nullable T passValue, String tagError) throws APICallbackException{
        if (Objects.isNull(caller)) {
            throw new APICallbackException("the caller is null");
        }

        Class<?> callerClass = caller.getClass();
        Method callbackMethod = null;
        for (Method method : callerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(APICallback.class)) {
                APICallback callbackAnnotation = method.getAnnotation(APICallback.class);
                if (callbackAnnotation.tag().equals(tag)) {
                    callbackMethod = method;
                    method.setAccessible(true);
                    break;
                }
            }
        }
        if (callbackMethod == null) {
            throw new APICallbackException(String.format("No Method with tag %s found in class %s",tag,caller));
        }

        Method errorCallbackMethod = null;
        if (tagError != null){
            for (Method method : callerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(APICallback.class)) {
                    APICallback callbackAnnotation = method.getAnnotation(APICallback.class);
                    if (callbackAnnotation.tag().equals(tagError)) {
                        errorCallbackMethod = method;
                        method.setAccessible(true);
                        break;
                    }
                }
            }
            if (errorCallbackMethod == null) {
                throw new APICallbackException(String.format("No Method with tag %s found in class %s",tagError,caller));
            }
        }

        Method finalCallbackMethod = callbackMethod;
        Method finalErrorCallbackMethod = errorCallbackMethod;
        String base_url = "http://10.0.2.2/GsbApi/";
        JsonObjectRequest APIRequest = new JsonObjectRequest(RESTmethod, base_url + url, body, response -> {
            try {
                if (passValue != null) {
                    finalCallbackMethod.invoke(caller, passValue, response);
                } else {
                    finalCallbackMethod.invoke(caller,response);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ExceptionInInitializerError | NullPointerException error) {
                error.printStackTrace();
            }
        },
                //TODO : pass the error code to the error method
                error -> {
                    error.printStackTrace();
                    if (finalErrorCallbackMethod != null) {
                        try {
                            if (passValue != null) {
                                finalErrorCallbackMethod.invoke(caller, passValue);
                            } else {
                                finalErrorCallbackMethod.invoke(caller);
                            }
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ExceptionInInitializerError | NullPointerException errorm) {
                            errorm.printStackTrace();
                        }
                    }
                });
        RequestQueue queue = ApiRequest.getInstance(context).getRequestQueue(context);
        queue.add(APIRequest);
        queue.start();


    }


    public static class APICallbackException extends Throwable {
        public APICallbackException(String message) {
            super(message);
        }
    }
}