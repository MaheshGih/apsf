package com.v2soft.apsf.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.v2soft.apsf.R;
import com.v2soft.apsf.adapter.ReferenceListAdapter;
import com.v2soft.apsf.adapter.ReferencesAdapter;
import com.v2soft.apsf.model.User;
import com.v2soft.apsf.shared.AppController;
import com.v2soft.apsf.shared.AppPreferences;
import com.v2soft.apsf.shared.NetWorkAvailable;
import com.v2soft.apsf.shared.Utility;
import com.v2soft.apsf.shared.WebServiceCall;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.v2soft.apsf.shared.Constants.URL;
import static com.v2soft.apsf.shared.Constants.USERS;
import static com.v2soft.apsf.shared.Utility.showMessageDialog;

/**
 * Created by srikanth.m on 3/17/2019.
 */

public class Home extends BaseSlider implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    //private ListView listView;
    private RecyclerView recyclerView;

    private ReferenceListAdapter referenceListAdapter;

    private ArrayList<User> mLstUsers;
    private ReferencesAdapter referencesAdapter;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mLstUsers = new ArrayList<>();
        //listView = (ListView) findViewById(R.id.listView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));

        referencesAdapter = new ReferencesAdapter(this, mLstUsers);
        referenceListAdapter = new ReferenceListAdapter(this, mLstUsers);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(referenceListAdapter);

        //listView.setAdapter(referencesAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        initData();
                                    }
                                }
        );

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Register.class);
                startActivityForResult(intent, 1010);
            }
        });

        TextView tv_footer = (TextView) findViewById(R.id.tv_footer);
        tv_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.text_site)));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getDrawer().isMenuVisible()) {
            getDrawer().closeMenu();
        } else
            showMessageDialog(Home.this, 0, "Exit", "Do you want to exit from " + getString(R.string.app_name) + "?");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            System.gc();
        } catch (Exception e) {}
    }

    public void openSlider(View v) {
        getDrawer().openMenu();
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                swipeRefreshLayout.setRefreshing(true);
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initData() {

        if (NetWorkAvailable.getNetWorkStatus()) {
            new LoadUsers().execute();
        } else {
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);

            Utility.customToast(getString(R.string.no_network), this);
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class LoadUsers extends AsyncTask<Void, Void, ArrayList<User>> {

        private String strResponse = "";

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {

            strResponse = WebServiceCall.callGETRequest(URL + USERS + "/" + AppPreferences.getInstance(Home.this).getValue("regid"), "");
            if (strResponse != null && !strResponse.isEmpty()) {

                mLstUsers.clear();

                try {

                    JSONArray jsonArray = new JSONArray(strResponse);
                    if (jsonArray != null && jsonArray.length() > 0) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<User>>() {
                        }.getType();
                        ArrayList<User> ist = gson.fromJson(strResponse, listType);
                        mLstUsers.addAll(ist);
                    }

                    return mLstUsers;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<User> articles) {

            if (articles != null) {
                mLstUsers = articles;

                // refresh listview
                //referencesAdapter = new ReferencesAdapter(Home.this, mLstUsers);
                //listView.setAdapter(referencesAdapter);

                referenceListAdapter.setData(mLstUsers);
                referenceListAdapter.notifyDataSetChanged();
            }

            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void callBackDataFromAsyncTask(String result) {
        Log.d("RESPONSE", result);

        try {
            if (result != null && !result.isEmpty()) {

            }
        } catch (Exception e) {
            Utility.CaughtException(this, e);
        }
    }
}