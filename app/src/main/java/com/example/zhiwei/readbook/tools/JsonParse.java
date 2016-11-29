package com.example.zhiwei.readbook.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.zhiwei.readbook.Model.BookInfo;
import com.example.zhiwei.readbook.Model.SettingConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhiwei on 2016/5/23.
 */
public class JsonParse {

    public static URL getSearchUrl(String keyword, int start, int count)
        throws MalformedURLException {
        //豆瓣API调用地址
        final String SEARCH_URL = "http://api.douban.com/v2/book/search?q=";
        keyword = keyword.replace(" ", "%20");
        String searchUrl = SEARCH_URL + keyword + "&start" + start + "&count" + count;
        return new URL(searchUrl);
    }


    public static ArrayList<BookInfo> GetBookListByKeyword(SettingConfig sc, String keyword, int start, int count)
        throws IOException, JSONException {
        URL url = null;
        HttpURLConnection coon = null;
        InputStream is = null;
        url = JsonParse.getSearchUrl(keyword, start, count);

        assert url != null;

        coon = (HttpURLConnection) url.openConnection();

        assert coon != null;
        is = coon.getInputStream();

        return JsonParse.GetBookInfo(sc, GetJsonString(is));
        //        URL url = null;
        //        HttpURLConnection conn = null;
        //        InputStream is = null;
        //
        //        try {
        //            url = JsonParse.getSearchUrl(keyword, start, count);
        //        } catch (MalformedURLException e) {
        //            e.printStackTrace();
        //        }
        //
        //        assert url != null;
        //        try {
        //            conn = (HttpURLConnection) url.openConnection();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //
        //        assert conn != null;
        //        try {
        //            is = conn.getInputStream();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //        return JsonParse.GetBookInfo(sc, GetJsonString(is));
    }


    public static String GetJsonString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len;
        byte[] data = new byte[1024];
        while ((len = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, len);
        }
        return new String(outputStream.toByteArray());

        //另一种方式解析json数据成String
        //        InputStreamReader reader = new InputStreamReader(inputStream);
        //        StringBuffer stringBuffer = new StringBuffer();
        //        String line;
        //        BufferedReader rd = new BufferedReader(reader);
        //        while ((line = rd.readLine()) != null) {
        //            stringBuffer.append(line);
        //        }
        //        return stringBuffer.toString();
    }


    public static ArrayList<BookInfo> GetBookInfo(SettingConfig sc, String jsonString)
        throws IOException, JSONException {
        ArrayList<BookInfo> bookInfoList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("books");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            //get id
            String id = object.getString("id");
            //get title
            String title = object.getString("title");
            //get author
            JSONArray authors = object.getJSONArray("author");
            String author = (authors.get(0)).toString();
            //get coverUrl
            JSONObject images = object.getJSONObject("images");
            String imageUrl;
            switch (sc.getLoadCoverQuality()) {
                default:
                case "一般":
                    imageUrl = images.getString("small");
                    break;
                case "中等":
                    imageUrl = images.getString("medium");
                    break;
                case "清晰":
                    imageUrl = images.getString("large");
                    break;
            }
            //get pages
            int pages = 0;
            String pagesStr = object.getString("pages");
            if (!pagesStr.equals("")) {
                // 提取页码字符串中的数字，正则表达式写法
                pagesStr = pagesStr.replaceAll("[^\\d]", "");
                pages = Integer.parseInt(pagesStr);
            }
            //get summary
            String summary = object.getString("summary");

            BookInfo bookInfo;
            if (sc.isLoadCoverMode()) {
                bookInfo = new BookInfo(id, title, author, imageUrl,
                    GetBitmapFormUrlString(imageUrl), pages, summary);
            } else {
                bookInfo = new BookInfo(id, title, author, imageUrl, pages, summary);
            }
            bookInfoList.add(bookInfo);
        }
        return bookInfoList;
    }


    public static Bitmap GetBitmapFormUrlString(String url) throws IOException {
        URL imageUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream is = connection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        is.close();
        return bitmap;
    }

}