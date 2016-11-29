package com.example.zhiwei.readbook.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhiwei.readbook.Model.Book;
import com.example.zhiwei.readbook.R;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarDeterminate;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookDetailActivity extends BaseActivity {

    private int bookId;
    private ImageView bookCoverView;
    private TextView bookNameView, bookAuthorView, bookPagesView,
            addDateView, finDateView, bookReadingPgrView;
    private ProgressBarDeterminate readProgressBar;
    private FloatingActionButton addBookMark;
    private LinearLayout readingInfoView, finishDateView;
    private static final int REQUEST_CODE_EDIT_BOOK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        bookId = intent.getIntExtra("bookId", 0);
        updateBookInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(BookDetailActivity.this,AddNewBookActivity.class);
                intent.putExtra("editBookId",bookId);
                startActivityForResult(intent,REQUEST_CODE_EDIT_BOOK);
                return true;
            case R.id.action_delete:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定删除此书？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File temp = new File(DataSupport.find(Book.class,bookId).getCoverFile());
                                if (temp.exists()){
                                    temp.delete();
                                }
                                DataSupport.delete(Book.class,bookId);
                                Intent intent1 = new Intent();
                                setResult(RESULT_OK,intent1);
                                finish();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_EDIT_BOOK:
                updateBookInfo();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        bookCoverView = (ImageView) findViewById(R.id.bookCoverView);
        bookNameView = (TextView) findViewById(R.id.bookNameView);
        bookAuthorView = (TextView) findViewById(R.id.bookAuthorView);
        bookPagesView = (TextView) findViewById(R.id.bookPagesView);
        addDateView = (TextView) findViewById(R.id.addDateView);
        finDateView = (TextView) findViewById(R.id.finDateView);
        bookReadingPgrView = (TextView) findViewById(R.id.bookReadProgressView);
        readProgressBar = (ProgressBarDeterminate) findViewById(R.id.readProgressBar);
        addBookMark = (FloatingActionButton) findViewById(R.id.addBookmark);
        readingInfoView = (LinearLayout) findViewById(R.id.readingInfoView);
        finishDateView = (LinearLayout) findViewById(R.id.finishDateView);

        addBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog;
                View view = LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.add_bookmark_dialog, null);
                final EditText et = (EditText) view.findViewById(R.id.bookmarkEditView);
                ButtonFlat sureAddBookmark = (ButtonFlat) view.findViewById(R.id.updateBookmarkButton);

                AlertDialog.Builder builder = new AlertDialog.Builder(BookDetailActivity.this);
                dialog = builder.setView(view).create();
                dialog.show();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                sureAddBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!et.getText().toString().trim().equals("")) {
                            int readPage = Integer.parseInt(et.getText().toString());
                            Book book = DataSupport.find(Book.class, bookId);
                            if (readPage <= book.getPages()){
                                Book newBook = new Book();
                                newBook.setReadPages(readPage);
                                if (readPage==book.getReadPages()){
                                    newBook.setFinishDate(new Date(System.currentTimeMillis()));
                                    newBook.setFinish(true);
                                }else {
                                    newBook.setToDefault("isFinish");
                                }
                                newBook.update(bookId);
                                updateBookInfo();
                                dialog.cancel();
                            }else {
                                AlertDialog alertDialog = new AlertDialog.Builder(BookDetailActivity.this)
                                        .setTitle("提示")
                                        .setMessage("您输入的页数有误")
                                        .setPositiveButton("确定",null)
                                        .show();
                            }
                        }
                    }
                });
            }
        });
    }

    private void updateBookInfo() {
        Book book = DataSupport.find(Book.class,bookId);
        File bookCoverFile = new File(book.getCoverFile());
        if (bookCoverFile.exists()){
            Bitmap coverBitmap = BitmapFactory.decodeFile(book.getCoverFile());
            bookCoverView.setImageBitmap(coverBitmap);
        }
        bookNameView.setText(book.getName());
        bookAuthorView.setText(book.getAuthor());
        bookPagesView.setText(book.getPages() + " 页");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        addDateView.setText(dateFormat.format(book.getAddDate()));

        if (book.isFinish()){
            readingInfoView.setVisibility(View.GONE);
            finishDateView.setVisibility(View.VISIBLE);
            finDateView.setText(dateFormat.format(book.getFinishDate()));
            addBookMark.setIcon(R.drawable.ic_action_done);
        }else {
            readingInfoView.setVisibility(View.VISIBLE);
            finishDateView.setVisibility(View.GONE);
            addBookMark.setIcon(R.drawable.ic_fab_bookmark);
            readProgressBar.setMax(book.getPages());
            readProgressBar.setProgress(book.getReadPages());
            bookReadingPgrView.setText(book.getReadPages() + "/" + book.getPages());
        }
    }

}
