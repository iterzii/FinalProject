package project.afinal.anuchar.mobilemarket;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 2/28/2017.
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    ConstraintLayout cslTechNews,cslNewsDetail;
    WebView webView;

    View view;

    String API_KEY = "6fc842f7ab954479821b330793b187ba";
    ListView listNews;
    ProgressBar loader,loader1;
    ImageButton btnBack;



    ArrayList<HashMap<String, String>> dataList;
    HashMap<String, String> map;

    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,@Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab1_fragment,container,false);

        cslTechNews = (ConstraintLayout) view.findViewById(R.id.cslTechNews);
        cslNewsDetail = (ConstraintLayout) view.findViewById(R.id.cslNewsDetail);
        cslNewsDetail.setVisibility(View.INVISIBLE);

        btnBack = (ImageButton) view.findViewById(R.id.btnBackWeb);
        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setVisibility(View.INVISIBLE);
                cslTechNews.setVisibility(View.VISIBLE);
                cslNewsDetail.setVisibility(View.INVISIBLE);
                webView.loadUrl("about:blank");
            }
        });

        listNews = (ListView) view.findViewById(R.id.listNews);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        listNews.setEmptyView(loader);

        if(Function.isNetworkAvailable(getContext()))
        {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        }else{
            useToast("No Internet Connection");
        }

        return view;
    }

    public void useToast(String txt){
        Toast.makeText(this.getContext(), ""+txt, Toast.LENGTH_SHORT).show();
    }

    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... args) {
            String xml = "";

            String ThAPIkey = "https://newsapi.org/v2/top-headlines?country=th&category=technology&apiKey=";
            String UsaAPIkey = "https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=";

            String urlParameters = "";
            xml = Function.excuteGet(
                    ThAPIkey+API_KEY, urlParameters);
            return  xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            if(xml.length()>10){ // Just checking if not empty
                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");
                    dataList = new ArrayList<HashMap<String, String>>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map = new HashMap<String, String>();
                        map.put(KEY_URLTOIMAGE, ""+jsonObject.optString(KEY_URLTOIMAGE).toString());
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                        if( !jsonObject.optString(KEY_DESCRIPTION).toString().equals("null"))
                            map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                        else
                            map.put(KEY_DESCRIPTION, "รายละเอียดเพิมเติมด้านใน");
                        map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());

                        map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    useToast("Unexpected error");
                }

                ListNewsAdapter adapter = new ListNewsAdapter((Activity) getContext(), dataList);
                listNews.setAdapter(adapter);

                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        cslTechNews.setVisibility(View.INVISIBLE);
                        cslNewsDetail.setVisibility(View.VISIBLE);
                        btnBack.setVisibility(View.VISIBLE);
                        useWebView(dataList.get(+position).get(KEY_URL));

                                            }
                });

            }else{
                useToast("No news found");
            }
        }

    }

    public void useWebView(String url){
        loader1 = (ProgressBar) view.findViewById(R.id.loader2);
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl(url);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    loader1.setVisibility(View.GONE);
                } else {
                    loader1.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}


