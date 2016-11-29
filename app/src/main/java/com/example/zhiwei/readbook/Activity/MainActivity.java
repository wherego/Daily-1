package com.example.zhiwei.readbook.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconCompat;
import com.dexafree.materialList.view.MaterialListView;
import com.example.zhiwei.readbook.Model.Book;
import com.example.zhiwei.readbook.Model.DrawerItem;
import com.example.zhiwei.readbook.R;
import com.example.zhiwei.readbook.adapter.DrawerItemAdapter;
import com.example.zhiwei.readbook.adapter.ReadingBookAdapter;
import com.example.zhiwei.readbook.tools.ActivityCollector;
import com.gc.materialdesign.views.ButtonFloat;
import java.util.ArrayList;
import org.litepal.crud.DataSupport;

public class MainActivity extends BaseActivity {

    private MaterialListView materialListView;
    private DrawerLayout drawerLayout;
    private RelativeLayout drawerView;
    private ListView drawerListView;
    private ListView settingAndExitView;
    private MaterialMenuIconCompat materialMenu;
    private RelativeLayout mainBookListLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ButtonFloat fabAdd;


    private ArrayList<Book> BookList;
    private ReadingBookAdapter adapter;

    //MainActivity content model and title string
    private String titleStr;
    private static final String DAILY = "Daily";
    private static final String READING = "读书";
    private static final String ANDROID = "Android";
    private static final String IOS = "iOS";
    private static final String JAVASCRIPT = "前端";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialMenu = new MaterialMenuIconCompat(this, Color.WHITE, MaterialMenuDrawable.Stroke.EXTRA_THIN);
        setContentView(R.layout.activity_main);
        titleStr = READING;
        initView();
        BookList = (ArrayList<Book>) DataSupport.where("isFinish=?", "0").find(Book.class);
        updateBook();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        materialMenu.onSaveInstanceState(outState);
    }


    @Override protected void onResume() {
        BookList = (ArrayList<Book>) DataSupport.findAll(Book.class);
        updateBook();
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        materialMenu.syncState(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void updateBook() {
        adapter = new ReadingBookAdapter(MainActivity.this, R.layout.book_item, BookList);
        materialListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()){
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this,SearchBookActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    updateBook();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK){
                    updateBook();
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        materialListView = (MaterialListView) findViewById(R.id.material_listView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (RelativeLayout) findViewById(R.id.drawer_view);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        fabAdd = (ButtonFloat) findViewById(R.id.fab_Button);
        mainBookListLayout = (RelativeLayout) findViewById(R.id.mainBookListLayout);
        settingAndExitView = (ListView) findViewById(R.id.settingAndExitView);
        initDrawerItem();
        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                materialMenu.animatePressedState(MaterialMenuDrawable.IconState.BURGER);
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(titleStr);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                materialMenu.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(DAILY);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNewBookActivity.class);
                startActivityForResult(intent,1);
            }
        });
        materialListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        materialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book readingBook = BookList.get(position);
                Intent intent = new Intent(MainActivity.this,BookDetailActivity.class);
                intent.putExtra("bookId",readingBook.getId());
                startActivityForResult(intent,2);
            }
        });
    }

    private void initDrawerItem() {
        ArrayList<DrawerItem> drawerItemList = new ArrayList<>();
        int[] drawerItemIcons = new int[]{
                R.drawable.ic_bookmark_black_48dp,
                R.drawable.ic_android_black_48dp,
                R.drawable.ic_favorite_black_48dp,
                R.drawable.ic_grade_black_48dp
        };
        String[] drawerItemNames = new String[]{
            READING,
            ANDROID,
            IOS,
            JAVASCRIPT
        };
        for (int i = 0; i < drawerItemIcons.length; i++) {
            DrawerItem drawerItem = new DrawerItem(drawerItemIcons[i], drawerItemNames[i]);
            drawerItemList.add(drawerItem);
        }
        DrawerItemAdapter adapter = new DrawerItemAdapter(MainActivity.this, R.layout.drawer_item, drawerItemList);
        drawerListView.setAdapter(adapter);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mainBookListLayout.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawer(drawerView);
                        titleStr = READING;
                        BookList = (ArrayList<Book>) DataSupport.where("isFinish=?", "0").find(Book.class);
                        updateBook();
                        break;
                    case 1:
                        drawerLayout.closeDrawer(drawerView);
                        Intent intent1 = new Intent(MainActivity.this, ContentInfoActivity.class);
                        intent1.putExtra(ContentInfoActivity.TYPE_KEY,"Android");
                        startActivity(intent1);
                        break;
                    case 2:
                        drawerLayout.closeDrawer(drawerView);
                        Intent intent2 = new Intent(MainActivity.this, ContentInfoActivity.class);
                        intent2.putExtra(ContentInfoActivity.TYPE_KEY,"iOS");
                        startActivity(intent2);
                        break;
                    case 3:
                        drawerLayout.closeDrawer(drawerView);
                        Intent intent3 = new Intent(MainActivity.this, ContentInfoActivity.class);
                        intent3.putExtra(ContentInfoActivity.TYPE_KEY,"前端");
                        startActivity(intent3);
                    default:
                        break;
                }
            }
        });

        ArrayList<DrawerItem> functionArrayList = new ArrayList<>();
        DrawerItem drawerItem;
        drawerItem = new DrawerItem(R.drawable.ic_settings_black_48dp,"设置");
        functionArrayList.add(drawerItem);
        drawerItem = new DrawerItem(R.drawable.ic_people_black_48dp,"关于");
        functionArrayList.add(drawerItem);
        drawerItem = new DrawerItem(R.drawable.ic_drawer_exit,"退出");
        functionArrayList.add(drawerItem);
        DrawerItemAdapter adapter1 = new DrawerItemAdapter(MainActivity.this,R.layout.drawer_item,functionArrayList);
        settingAndExitView.setAdapter(adapter1);
        settingAndExitView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        drawerLayout.closeDrawer(drawerView);
                        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        drawerLayout.closeDrawer(drawerView);
                        Intent intent1 = new Intent(MainActivity.this, AboutTeamActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        ActivityCollector.finishAll();
                        break;
                    default:
                        break;
                }
            }
        });

    }


}
