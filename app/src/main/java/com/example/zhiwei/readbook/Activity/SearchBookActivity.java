package com.example.zhiwei.readbook.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.example.zhiwei.readbook.Model.BookInfo;
import com.example.zhiwei.readbook.Model.SettingConfig;
import com.example.zhiwei.readbook.R;
import com.example.zhiwei.readbook.adapter.BookInfoAdapter;
import com.example.zhiwei.readbook.tools.JsonParse;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;

public class SearchBookActivity extends BaseActivity {

    private MaterialEditText keyWordEditText;
    private ListView searchResultLv;
    private ProgressBar progressBar;
    private static Handler handler = new Handler();
    private BookInfoAdapter adapter;

    private ArrayList<BookInfo> bookInfoList = null;
    private ArrayList<BookInfo> newBooks = null;

    private int searchStart = 0;
    private int searchCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        //获取设置信息
        SettingConfig config = new SettingConfig(this);
        searchCount = config.getLoadNum();

        ActionBar bar = getSupportActionBar();
        bar.setCustomView(R.layout.search_edit_actionbar);
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
        keyWordEditText = (MaterialEditText) bar.getCustomView().findViewById(R.id.editText_search_keyword);
        searchResultLv = (ListView) findViewById(R.id.lv_search_result);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        searchResultLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        searchStart += searchCount;
                        new Thread(runnable).start();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        searchResultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookInfo bookInfo = bookInfoList.get(position);
                Intent intent = new Intent(SearchBookActivity.this, BookInfoActivity.class);
                intent.putExtra("title", bookInfo.getTitle());
                intent.putExtra("author",bookInfo.getAuthor());
                intent.putExtra("pages",bookInfo.getPages());
                intent.putExtra("summary",bookInfo.getSummary());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                progressBar.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive() && getCurrentFocus() != null) {
                    if (getCurrentFocus().getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                if (!keyWordEditText.getText().toString().trim().equals("")) {
                    searchStart = 0;
                    if (bookInfoList != null) {
                        bookInfoList.clear();
                    }
                    new Thread(runnable).start();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("您的输入有误，请检查")
                            .setPositiveButton("确定", null)
                            .create();
                    dialog.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String keyword = keyWordEditText.getText().toString();
            try {
                SettingConfig sc = new SettingConfig(SearchBookActivity.this);
                if (bookInfoList == null) {
                    bookInfoList = JsonParse.GetBookListByKeyword(sc, keyword, searchStart, searchCount);
                } else {
                    newBooks = JsonParse.GetBookListByKeyword(sc, keyword, searchStart, searchCount);
                    bookInfoList.addAll(newBooks);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    if (searchStart == 0) {
                        adapter = new BookInfoAdapter(SearchBookActivity.this, R.layout.book_item_info, bookInfoList);
                        searchResultLv.setAdapter(adapter);
                    } else {
                        adapter.addItems(newBooks);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

}
