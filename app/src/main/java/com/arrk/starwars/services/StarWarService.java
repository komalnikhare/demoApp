package com.arrk.starwars.services;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arrk.starwars.BuildConfig;
import com.arrk.starwars.dbConfig.DatabaseCreator;
import com.arrk.starwars.models.Character;
import com.arrk.starwars.utils.ProgressDialog;
import com.arrk.starwars.utils.RequestUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StarWarService {
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public StarWarService(Context context){
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void getStarCharacterList(final Context context, final Handler mHandler){

        showProgressDialog();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BuildConfig.GET_CHARACTER_LIST_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        try {
                            if (response!=null && response.has("results")){
                                JSONArray array = response.optJSONArray("results");
                                if (array!=null && array.length()>0){
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Character>>(){}.getType();
                                    List<Character> mCharacters = gson.fromJson(array.toString(),listType);

                                    DatabaseCreator.getInstance(context).deleteAll();
                                    DatabaseCreator.getInstance(context).insertAll(mCharacters);
                                    sendError(mCharacters, 0, mHandler);
                                }else {
                                    sendError("No records found", 1, mHandler);
                                }
                            }else {
                                sendError("No records found", 1, mHandler);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                String errorMessage = "";
                NetworkResponse response = error.networkResponse;
                if (response != null){
                    try {
                        errorMessage = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                sendError(errorMessage, 1, mHandler);
            }
        });

        RequestUtils.addRequest(mContext, request);
    }

    private void sendError(Object object, int value, Handler mHandler){
        Message msg = new Message();
        msg.obj = object;
        msg.arg1 = value;
        mHandler.sendMessage(msg);
    }
}
