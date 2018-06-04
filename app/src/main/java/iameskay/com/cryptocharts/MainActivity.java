package iameskay.com.cryptocharts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static String nameData = null;
    private static JSONObject nameJSONObject = null;
    ArrayList<Currency> currencies = new ArrayList<>();

    private RecyclerView mCurrencyRecyclerView;
    private RecyclerView.Adapter mCurrencyAdapter;
    private RecyclerView.LayoutManager mCurrencyLayoutManager;
    private LinearLayout spinner;

    private int numCurrenciesToDisplay = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrencyRecyclerView = findViewById(R.id.currency_recycler_view);
        mCurrencyLayoutManager = new LinearLayoutManager(this);
        mCurrencyRecyclerView.setLayoutManager(mCurrencyLayoutManager);

        spinner = findViewById(R.id.progress_bar_layout);

        spinner.setVisibility(View.VISIBLE);
        new FetchDataTask().execute(getString(R.string.name_endpoint));
    }

    private class FetchDataTask extends AsyncTask<String, Void, ArrayList<Currency>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Currency> doInBackground(String... strings) {

            String nameUrl = strings[0];
            OkHttpClient client = new OkHttpClient();

            Request nameRequest = new Request.Builder()
                    .url(nameUrl)
                    .build();

            Response nameResponse = null;
            try {
                nameResponse = client.newCall(nameRequest).execute();
                nameData = nameResponse.body().string();
            } catch (IOException e) {
                Log.e("RESPONSE", "Name response error", e);
            }

            try {
                JSONObject jsonObj = new JSONObject(nameData);
                nameJSONObject = jsonObj.getJSONObject("Data");
            } catch (JSONException e) {
                Log.e("RESPONSE", "JSON Error while obtaining name data", e);
            }

            Response priceResponse = null;

            String priceData = null;
            Iterator<String> iter = nameJSONObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    if (numCurrenciesToDisplay != 0) {
                        JSONObject currency = nameJSONObject.getJSONObject(key);
                        String name = currency.getString("Name");
                        priceResponse = client.newCall(createPriceRequest(name)).execute();
                        priceData = priceResponse.body().string();
                        Double price = new JSONObject(priceData).getDouble("CAD");
                        currencies.add(new Currency(name, price, false));
                        numCurrenciesToDisplay--;
                    }
                } catch (Exception e) {
                    Log.d("exception", e.toString());
                }
            }

            return currencies;
        }

        @Override
        protected void onPostExecute(ArrayList<Currency> currencies) {
            mCurrencyAdapter = new CurrencyAdapter(currencies);
            mCurrencyRecyclerView.setAdapter(mCurrencyAdapter);
            spinner.setVisibility(View.GONE);
        }
    }

    private Request createPriceRequest(String currency) {
        String url = getString(R.string.price_endpoint, currency);
        return new Request.Builder().url(url).build();
    }
}
