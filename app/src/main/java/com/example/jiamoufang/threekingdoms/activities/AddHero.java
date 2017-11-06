package com.example.jiamoufang.threekingdoms.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.R;

public class AddHero extends AppCompatActivity implements View.OnClickListener{
    private Button AddHerobutton;
    private Button SelectHeroButton;
    private Uri imgUri;
    private ImageView heroImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hero);
        /*
         * 设置拍照时屏幕不随手机旋转
         */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR); //不随手机旋转
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //设置直向显示

        initView();
        initEvent();
    }


    /*
    * 点击事件处理
    * */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_addhero:
                onGet();
                break;
            case R.id.button_selectHero:
                onGetPhotoFromStorage();
            default:
                break;
        }
    }

    private void onGetPhotoFromStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// 选取内容
        intent.setType("image/*");
        startActivityForResult(intent, 101);

    }

    /*
    * 启动相机拍照,获取本地存路径，拍完照存放
    * */

    private void onGet() {
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String fname = "p" + System.currentTimeMillis() + ".jpg"; //设置不会重复的文件名
        imgUri = Uri.parse("file://" + dir + "/" + fname); //按路径名创建Uri对象
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri); //将uri加到拍照的intent的额外数据中
        startActivityForResult(intent,100);
    }

    /*
    * 初始化控件
    * */
    public void initView() {
        AddHerobutton = (Button) findViewById(R.id.button_addhero);
        heroImage = (ImageView)findViewById(R.id.imageview_addhero);
        SelectHeroButton = (Button) findViewById(R.id.button_selectHero);
    }
    /*
    * 事件注册
    * */
    private void initEvent() {
        AddHerobutton.setOnClickListener(this);
        SelectHeroButton.setOnClickListener(this);
    }


    /*
    * 获取照片并处理
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,imgUri);
                    sendBroadcast(it);
                    break;
                case 101:
                    imgUri = convetUri(data.getData());
                    showImg();
                    break;
                default:
                    break;
            }
            showImg();
        } else {
            Toast.makeText(this, requestCode == 100? "没有拍到照片":"没有选取到照片", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * Uri 路径转换
    * */
    Uri convetUri(Uri uri) {
        if (uri.toString().substring(0, 7).equals("content")) {
            String[] colName = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(uri, colName, null, null, null);
            cursor.moveToFirst();
            uri = Uri.parse("file://" + cursor.getString(0));
            cursor.close();
        }
        return uri;
    }

    private void showImg() {
        int picture_w, picture_h,imageview_w, imageview_h;
        boolean neeedRrotate; //是否需要旋转
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只读取图像信息而不载入图像文件
        BitmapFactory.decodeFile(imgUri.getPath(), options);//将读取的文件信息存放到options对象中
        picture_w = options.outWidth;
        picture_h = options.outHeight;
        imageview_w = heroImage.getWidth();
        imageview_h = heroImage.getHeight();

        int scaleFactor;
        if (picture_w < picture_h) {
            neeedRrotate = false;  //不需要旋转
            scaleFactor = Math.min(picture_w/imageview_w,picture_h/imageview_h); //计算缩小比例
        } else {
            neeedRrotate = true;  //需要旋转
            scaleFactor = Math.min(picture_h/imageview_w,picture_w/imageview_h); //计算缩小比例
        }

        options.inJustDecodeBounds = false; //关闭
        options.inSampleSize = scaleFactor;
        Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath(),options);

        if (neeedRrotate) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(), matrix, true);
        }
        heroImage.setImageBitmap(bmp);

        new AlertDialog.Builder(this)
                .setTitle("图像信息")
                .setMessage("图像文件路径：" + imgUri.getPath() +
                "\n 原始尺寸：" + picture_w + "x" + picture_h +
                "\n 载入尺寸: " + bmp.getWidth() + "x" + bmp.getHeight() +
                "\n 显示尺寸: " + imageview_w + "x" + imageview_h)
                .setNegativeButton("关闭", null)
                .show();

    }
}
