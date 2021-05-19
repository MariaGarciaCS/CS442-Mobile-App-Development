package com.marigarci.newsgateway;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private MyPageAdapter myPageAdapter;
    private List<Fragment> fragments;
    private ViewPager pager;
    Set<String> category_set = new HashSet<String>();
    static int source_pos = -1;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    static private ArrayList<String> items = new ArrayList<>();
    static private ArrayList<String> items_id = new ArrayList<>();
    static long adapter_counter = 0;

    static final String ACTION_NEWS_STORY = "ANS";
    static final String ACTION_MSG_TO_SVC = "AMTS";
    Intent intent;
    String rtitle[];
    String rdesc[];
    String rimgurl[];
    String rpublishedAt[];
    String rauthor[];
    String rnews_url[];

//    Bundle
    String Title[];
    String Desc[];
    String Urldata[];
    String publishedAt[];
    String author[];
    String news_url[];

    public NewsReceiver newsReceiver;
    ArrayList<Article> story_list = new ArrayList<>();
    HashMap<String, Integer> categoryColors = new HashMap<String, Integer>();
    static String sourcename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        newsReceiver = new NewsReceiver();
        intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);


        IntentFilter filter = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter);
        deleteCache(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList =  findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, items));



        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawerOpen,
                R.string.drawerClose
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        fragments = getFragments();
        myPageAdapter = new MyPageAdapter(getSupportFragmentManager());
        myPageAdapter.baseId = adapter_counter;
        pager = findViewById(R.id.viewpager);
        pager.setAdapter(myPageAdapter);
        NewsSource Dataloader = new NewsSource(this,"Null");
        new Thread(Dataloader).start();
    }

    public void sources_data_to_add(ArrayList<Source> sources_data) {
        items.clear();
        items_id.clear();
        for (int i = 0; i < sources_data.size(); i++) {
            items.add(sources_data.get(i).getName());
            items_id.add(sources_data.get(i).getId());
            category_set.add(sources_data.get(i).getCategory());

        }
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_item, items));
    }

//  OPTIONS MENUE ///////////////////////////////////////////////////
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.opt_menu, menu);

        int i = 0;
        for (Iterator<String> it = category_set.iterator(); it.hasNext(); ) {
            String current = it.next();
            MenuItem item = menu.add( Menu.NONE, i,2, current);
            if (categoryColors.containsKey(current)){
//                item.setTextColor(categoryColors.get(current));
            }else{
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                SpannableString s = new SpannableString(item.getTitle().toString());
                s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
                item.setTitle(s);
            }
            i++;
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }



    //ARTICLES////////////////////////////////////////////////////////////////////////////////////////////

    public void noDataFound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Check that WiFi is turned on.");
        builder.setTitle("No Data Found");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        return fragmentList;
    }

    private void reDoFragments(ArrayList<Article> articlesList, int pos) {
        pager.setBackground(null);
        for (int i = 0; i < myPageAdapter.getCount(); i++)
            myPageAdapter.notifyChangeInPosition(i);
        fragments.clear();
        for (int i = 0; i < articlesList.size(); i++) {
            fragments.add(NewsFragment.newInstance(articlesList.get(i).getTitle(), articlesList.get(i).getDescription(), articlesList.get(i).getUrlToImage(), i + 1 + " of " + articlesList.size(), articlesList.get(i).getPublishedAt(), articlesList.get(i).getAuthor(), this, articlesList.get(i).getUrlToArticle()));
        }
        myPageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }

    private void restore_reDoFragments(ArrayList<Article> articlesList, int pos) {
        if(articlesList.size()==0)
            setTitle(R.string.app_name);
        else
            pager.setBackground(null);
        fragments.clear();
        for (int i = 0; i < articlesList.size(); i++) {
            fragments.add(NewsFragment.newInstance(articlesList.get(i).getTitle(), articlesList.get(i).getDescription(), articlesList.get(i).getUrlToImage(), i + 1 + " of " + articlesList.size(), articlesList.get(i).getPublishedAt(), articlesList.get(i).getAuthor(), this, articlesList.get(i).getUrlToArticle()));
        }

        for (int i = 0; i < myPageAdapter.getCount(); i++)
            myPageAdapter.notifyChangeInPosition(i);

        myPageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }


    //Drawer////////////////////////////////////////////////////////////////

    //click on item in drawer
    @SuppressLint("ResourceAsColor")
    private void selectItem(int position) {
        source_pos = position;
        sourcename = items.get(position);
        setTitle(items.get(position));
        Intent intent = new Intent();
        intent.setAction(MainActivity.ACTION_MSG_TO_SVC);
        intent.putExtra("SOURCE_DATA", items_id.get(position));
        sendBroadcast(intent);


        mDrawerLayout.closeDrawer(mDrawerList);
        int color = R.color.colorAccent;
        mDrawerLayout.setBackgroundColor(color);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(newsReceiver);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        NewsSource dl = new NewsSource(this,item.toString());
        new Thread(dl).start();
        return true;
    }

    //BUNDLE/////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Title = new String[story_list.size()];
        Desc = new String[story_list.size()];
        Urldata = new String[story_list.size()];
        publishedAt = new String[story_list.size()];
        author = new String[story_list.size()];
        news_url = new String[story_list.size()];
        for (int i = 0; i < story_list.size(); i++) {
            Title[i] = story_list.get(i).getTitle();
            Desc[i] = story_list.get(i).getDescription();
            Urldata[i] = story_list.get(i).getUrlToImage();
            publishedAt[i] = story_list.get(i).getPublishedAt();
            author[i] = story_list.get(i).getAuthor();
            news_url[i] = story_list.get(i).getUrlToArticle();
        }
        outState.putLong("baseId_count", myPageAdapter.baseId);
        outState.putInt("Source_index", source_pos);
        outState.putString("Source", sourcename);
        outState.putStringArray("Title", Title);
        outState.putStringArray("Desc", Desc);
        outState.putStringArray("Urldata", Urldata);
        outState.putStringArray("publishedAt", publishedAt);
        outState.putStringArray("author", author);
        outState.putStringArray("news_url", news_url);
        super.onSaveInstanceState(outState);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState1) {
        setTitle(savedInstanceState1.getString("Source"));
        adapter_counter = savedInstanceState1.getLong("baseId_count");
        Log.d("adapter_counter:- ", Long.toString(adapter_counter));
        rtitle = savedInstanceState1.getStringArray("Title");
        rdesc = savedInstanceState1.getStringArray("Desc");
        rimgurl = savedInstanceState1.getStringArray("Urldata");
        rpublishedAt = savedInstanceState1.getStringArray("publishedAt");
        rauthor = savedInstanceState1.getStringArray("author");
        rnews_url = savedInstanceState1.getStringArray("news_url");
        story_list.clear();
        for (int i = 0; i < rtitle.length; i++) {
            story_list.add(new Article(rauthor[i], rtitle[i], rdesc[i], rimgurl[i], rpublishedAt[i],rnews_url[i]));
        }
        restore_reDoFragments(story_list, source_pos);

        int color = R.color.colorAccent;
        mDrawerLayout.setBackgroundColor(color);

        super.onRestoreInstanceState(savedInstanceState1);
    }

    public static void deleteCache(Context context) {
        File dir = context.getExternalCacheDir();
        if (dir != null && dir.isDirectory())
            deleteDir(dir);

    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }



    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        @Override
        public int getCount() {
            return fragments.size();
        }
        @Override
        public long getItemId(int position) {
            return baseId + position;
        }
        public void notifyChangeInPosition(int n) {
            baseId += getCount() + n;
        }
    }

    //  BROADCAST////////////////////////////////////////////////////////
    class NewsReceiver extends BroadcastReceiver {
        String title[];
        String desc[];
        String imgurl[];
        String publishedAt[];
        String author[];
        String news_url[];

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_NEWS_STORY:
                    if (intent.hasExtra("TITLE")) {
                        story_list.clear();
                        title = intent.getStringArrayExtra("TITLE");
                        desc = intent.getStringArrayExtra("DESC");
                        imgurl = intent.getStringArrayExtra("URL");
                        publishedAt = intent.getStringArrayExtra("PUBLISHEDAT");
                        author = intent.getStringArrayExtra("AUTHOR");
                        news_url = intent.getStringArrayExtra("NEWSURL");
                        for (int i = 0; i < title.length; i++) {
                            story_list.add(new Article(author[i], title[i], desc[i], imgurl[i], publishedAt[i], news_url[i]));
                        }
                        reDoFragments(story_list, source_pos);
                    }
                    break;
            }
        }
    }
}