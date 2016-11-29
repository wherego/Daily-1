package com.example.zhiwei.readbook.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zhiwei.readbook.Model.Book;
import com.example.zhiwei.readbook.R;
import com.gc.materialdesign.views.ButtonFlat;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewBookActivity extends BaseActivity implements View.OnClickListener {

    private MaterialEditText addNameView;
    private MaterialEditText addAuthorView;
    private MaterialEditText addPagesView;
    private ButtonFlat addCover;

    private ImageView bookCoverView;
    private static final int COVER_REQUEST_TAKE_PHOTO = 1;
    private static final int COVER_REQUEST_GALLERY = 2;
    private static final int COVER_REQUEST_CUT = 3;
    private File coverPath;
    private File tempCoverFile = null;
    private File coverFile = null;

    private boolean addMode = true;     //false为编辑模式，true为新增模式，两种模式
    private int bookId;
    private boolean saveResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        initView();
        createCoverFilePath();
        Intent intent = getIntent();
        bookId = intent.getIntExtra("editBookId", -1);
        if (bookId != -1) {
            addMode = false;
            initBookInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case COVER_REQUEST_TAKE_PHOTO:
                startPhotoZoom(Uri.fromFile(tempCoverFile),120,180);
                break;
            case COVER_REQUEST_GALLERY:
                if (data!=null){
                    startPhotoZoom(data.getData(),240,360);
                }
                break;
            case COVER_REQUEST_CUT:
                if (data!=null){
                    setPicTOView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_book_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                String coverFilePath = "";
                if (coverFile!=null){
                    if (!addMode){
                        new File(DataSupport.find(Book.class,bookId).getCoverFile()).delete();
                    }
                    coverFilePath = coverFile.getPath();
                }else {
                    if (!addMode){
                        coverFilePath = DataSupport.find(Book.class,bookId).getCoverFile();
                    }
                }

                if (!addNameView.getText().toString().trim().equals("")
                        &&!addAuthorView.getText().toString().trim().equals("")
                        &&!addPagesView.getText().toString().trim().equals("")
                        &&!coverFilePath.equals("")){
                    int pages = Integer.parseInt(addPagesView.getText().toString());
                    Book book = new Book(addNameView.getText().toString(),
                            addAuthorView.getText().toString(),
                            pages,
                            0,
                            coverFilePath);
                    if (addMode){
                        if (book.save()){
                            saveResult = true;
                            Toast.makeText(this,"书籍添加成功",Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(this,"书籍添加失败",Toast.LENGTH_SHORT).show();
                    }else {
                        book.update(bookId);
                        saveResult = true;
                        Toast.makeText(this,"书籍更新成功",Toast.LENGTH_SHORT).show();
                    }
                    Intent intent =new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                    return true;
                }else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("书籍信息不完整，请检查您的输入")
                            .setPositiveButton("确定",null)
                            .create();
                    dialog.show();
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startPhotoZoom(Uri uri, int sizeX, int sizeY) {
        coverFile = new File(coverPath,getCoverFileName());
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");

        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 3);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", sizeX);
        intent.putExtra("outputY", sizeY);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);

        //set cover output file path and name
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(coverFile));
        startActivityForResult(intent,COVER_REQUEST_CUT);
    }

    private void setPicTOView(Intent coverIntent){
        addCover.setVisibility(View.GONE);
        bookCoverView.setVisibility(View.VISIBLE);
        Bundle bundle = coverIntent.getExtras();
        if (bundle!=null){
            Bitmap bitmap = bundle.getParcelable("data");
            bookCoverView.setImageBitmap(bitmap);
        }
    }

    private String getCoverFileName(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private void initView() {
        addNameView = (MaterialEditText) findViewById(R.id.addNameEditView);
        addAuthorView = (MaterialEditText) findViewById(R.id.addAuthorEditView);
        addPagesView = (MaterialEditText) findViewById(R.id.addPagesEditView);
        addCover = (ButtonFlat) findViewById(R.id.addBookCover);
        bookCoverView = (ImageView) findViewById(R.id.bookCoverView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (addMode)
            getSupportActionBar().setTitle("添加新书");
        else
            getSupportActionBar().setTitle("更新书籍");

        addCover.setOnClickListener(this);
        bookCoverView.setOnClickListener(this);
    }

    private void createCoverFilePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File sdDir = Environment.getExternalStorageDirectory();
            String path = sdDir.getPath() + "/Reading";
            File readingPath = new File(path);
            if (!readingPath.exists()) {
                readingPath.mkdir();
            }
            coverPath = new File(readingPath, "/.cover");
            if (!coverPath.exists()) {
                if (coverPath.mkdir()) {
                    Toast.makeText(this, "Create CoversFolder Succeed", Toast.LENGTH_SHORT).show();
                }
            }
            tempCoverFile = new File(coverPath, "tempCover.jpg");
        } else {
            Toast.makeText(this, "Create CoversFolder Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void initBookInfo() {
        Book book = DataSupport.find(Book.class, bookId);
        addCover.setVisibility(View.GONE);
        bookCoverView.setVisibility(View.VISIBLE);
        File bookFilePath = new File(book.getCoverFile());
        if (bookFilePath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(book.getCoverFile());
            bookCoverView.setImageBitmap(bitmap);
        }
        addNameView.setText(book.getName());
        addAuthorView.setText(book.getAuthor());
        addPagesView.setText(Integer.toString(book.getPages()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBookCover:
            case R.id.bookCoverView:
                setBookCover();
                break;
            default:
                break;
        }
    }

    private void setBookCover() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("获取封面")
                .setItems(new String[]{"拍照", "从图库选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent0 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent0.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempCoverFile));
                                startActivityForResult(intent0, COVER_REQUEST_TAKE_PHOTO);
                                break;
                            case 1:
                                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent1, COVER_REQUEST_GALLERY);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (!saveResult){
            if (coverFile!=null){
                coverFile.delete();
            }
        }
        if (tempCoverFile.exists()){
            tempCoverFile.delete();
        }
        super.onDestroy();
    }
}
