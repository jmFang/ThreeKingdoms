package com.example.jiamoufang.threekingdoms.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.MainActivity;
import com.example.jiamoufang.threekingdoms.MyMusic;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.entities.Hero;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;

public class AddHero extends AppCompatActivity implements View.OnClickListener{
    private Uri imgUri;
    private ImageView heroImage;

    private MyMusic myMusic;
    /*
    * 人物属性
    * */
    private TextInputEditText heroName, heroSex, heroBirth, heroAddress, heroBelong;
    private TextInputEditText heroAttack, heroIntelligence,heroLeadership, heroFood;
    private EditText heroIntroduction;

    /*
    * 提交按钮
    * */
    private Button submit;
    /*进度条*/
    private ProgressBar progressBar;
    /*是否点击上传或拍照*/
    private int defaultImageId;
    /*
    * 图片的路径，作为把图片文件上传到云端的一个必然参数
    * */
    private String photoPath;

    /*
    * 区分当前是添加英雄状态还是修改英雄状态
    * */
    private int state = 0;

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

        myMusic = new MyMusic(this);

        /*
         * 编辑英雄信息
         */
        if(getIntent().getStringExtra("editHero") != null) {
            String name = getIntent().getStringExtra("editHero");
            EditHero(name);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MainActivity.isMute) {
            myMusic.initMusic(R.raw.addhero_music);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!MainActivity.isMute) {
            myMusic.releaseMusic();
        }
    }

    /*
    * 点击事件处理
    * */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_addhero:
                onAlertDialog();
                break;
            case R.id.add_hero_submit:
                onCheckAllTextInput();
            default:
                break;
        }
    }

    /*
    * 检查所有的输入是否合法
    * */
    private void onCheckAllTextInput() {
        String name = heroName.getText().toString();
        String sex = heroSex.getText().toString();
        String birth = heroBirth.getText().toString();
        String address = heroAddress.getText().toString();
        String belong = heroBelong.getText().toString();
        String attack = heroAttack.getText().toString();
        String intelligence = heroIntelligence.getText().toString();
        String leadership = heroLeadership.getText().toString();
        String food = heroFood.getText().toString();
        String introducton = heroIntroduction.getText().toString();

        if (name.length() == 0) {
            Toast.makeText(this, "人物名字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sex.length() == 0) {
            Toast.makeText(this, "人物性别未填写", Toast.LENGTH_SHORT).show();
            return;
        }
        if (birth.length() == 0) {
            Toast.makeText(this, "生卒年不能为空，不知则填写不明", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        * 籍贯的检查待优化，暂时只检查不为空，待云端数据库做好了再检查籍贯是否存在
        * */
        if (address.length() == 0) {
            Toast.makeText(this, "籍贯不能为空，不知则填写不明", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        * 主效势力待优化，暂时只检查不为空，待云端数据库做好了再检查主校势力是否存在
        * */
        if (belong.length() == 0) {
            Toast.makeText(this, "主效势力不能为空，若不知则填不明", Toast.LENGTH_SHORT).show();
            return;
        }

        if (attack.length() == 0 ) {
            Toast.makeText(this, "武力值不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(attack) > 100 || Integer.parseInt(attack) < 0) {
            Toast.makeText(this, "武力值不超过100", Toast.LENGTH_SHORT).show();
            return;
        }

        if (intelligence.length() == 0 ) {
            Toast.makeText(this, "智力值不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(intelligence) > 100 || Integer.parseInt(intelligence) < 0) {
            Toast.makeText(this, "智力值不超过100", Toast.LENGTH_SHORT).show();
            return;
        }

        if (leadership.length() == 0 ) {
            Toast.makeText(this, "统帅值不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(leadership) > 100 || Integer.parseInt(leadership) < 0) {
            Toast.makeText(this, "统帅值不超过100", Toast.LENGTH_SHORT).show();
            return;
        }

        if (food.length() == 0 ) {
            Toast.makeText(this, "粮草值不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(food) > 1000 || Integer.parseInt(leadership) < 0) {
            Toast.makeText(this, "粮草值不超过1000", Toast.LENGTH_SHORT).show();
            return;
        }

        if (introducton.length() == 0) {
            Toast.makeText(this, "人物简介不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        heroImage.setDrawingCacheEnabled(true);
        Bitmap heroBitmap = Bitmap.createBitmap(heroImage.getDrawingCache());

        if (defaultImageId == R.mipmap.ic_take_photo) {
            Toast.makeText(this, "人物头像未上传", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "人物头像已上传", Toast.LENGTH_SHORT).show();
            heroImage.setDrawingCacheEnabled(false);
        }

        /*
        * 检查完毕，没有非法数据
        * Hero(String name, Bitmap heroImage, String sex, String date, String place, String state, String introduction,
                int force, int inteligence, int leadship, int forage)
        * */

        /*
        * 与 HerosListFragment通信，将newHero传过去
        * 或者上传到云端
        * 这里有点坑啊，图片上传和添加到数据库如果分开写则是异步执行的，把添加动作放进图片上传成功后面执行才可以
        * */
        final BmobFile bmobFile = new BmobFile(new File(photoPath));
        final Hero newHero = new Hero( name,bmobFile,sex,birth,address,belong,introducton,Integer.parseInt(attack),
                Integer.parseInt(intelligence),Integer.parseInt(leadership),Integer.parseInt(food));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null) {
                    Toast.makeText(AddHero.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                    uploadToBmob(newHero);
                } else {
                    Toast.makeText(AddHero.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                }
            }
            /*
            * 上传进度，显示进度条，可以美化进度条
            * */
            @Override
            public void onProgress(Integer value) {
                super.onProgress(value);
                progressBar.setVisibility(View.VISIBLE);
                //int progress =  progressBar.getProgress();
                progressBar.setProgress(value);
                if (value == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

        });

    }
    /*
    * 将新添加的英雄加到云端
    * */
    private void uploadToBmob(Hero newHero) {
        newHero.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(AddHero.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddHero.this, "添加失败 "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*
    * 弹出对话框，选择拍照还是从相册上传
    * */
    private void onAlertDialog() {
        final String[] items = {"拍照","从相册选择"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddHero.this);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        onGet();
                        break;
                    case 1:
                        onGetPhotoFromStorage();
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    /*
    * 从外存拿图片
    * */
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
        heroImage = (ImageView)findViewById(R.id.imageview_addhero);
        heroName = (TextInputEditText) findViewById(R.id.tv_hero_name);
        heroSex = (TextInputEditText) findViewById(R.id.tv_hero_sex);
        heroBirth = (TextInputEditText) findViewById(R.id.tv_hero_birth);
        heroAddress = (TextInputEditText) findViewById(R.id.tv_hero_address);
        heroBelong = (TextInputEditText) findViewById(R.id.tv_hero_belong);
        heroAttack = (TextInputEditText) findViewById(R.id.tv_hero_attack);
        heroIntelligence = (TextInputEditText) findViewById(R.id.tv_hero_intelligence);
        heroLeadership = (TextInputEditText) findViewById(R.id.tv_hero_leadership);
        heroFood = (TextInputEditText) findViewById(R.id.tv_hero_food);
        heroIntroduction = (EditText) findViewById(R.id.etv_hero_introduction);
        submit = (Button) findViewById(R.id.add_hero_submit);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        /*
        * haveTakenPic 初始化为默认的R.mipmap.ic_take_photo,表示图片为上传
        * */
        defaultImageId = R.mipmap.ic_take_photo;
    }
    /*
    * 事件注册
    * */
    private void initEvent() {
        heroImage.setOnClickListener(this);
        submit.setOnClickListener(this);
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

    /*
    * 显示图片
    * */
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

        /*
        * 已经拿到图片，haveTakenPic置为R.mipmap.ic_take_photo +1;
        * */
        defaultImageId = R.mipmap.ic_take_photo + 1;
        /* 设置上传到云端的图片的路径参数*/
        photoPath = imgUri.getPath();

        new AlertDialog.Builder(this)
                .setTitle("图像信息")
                .setMessage("图像文件路径：" + imgUri.getPath() +
                "\n 原始尺寸：" + picture_w + "x" + picture_h +
                "\n 载入尺寸: " + bmp.getWidth() + "x" + bmp.getHeight() +
                "\n 显示尺寸: " + imageview_w + "x" + imageview_h)
                .setNegativeButton("关闭", null)
                .setCancelable(false)
                .show();

    }

    private void EditHero(String heroname) {
        LocalHero tem = Herolist.get(0);
        for(int i = 0; i < Herolist.size(); i++) {
            if(heroname.equals(Herolist.get(i).getName())){
                tem = Herolist.get(i);
                break;
            }
        }

        heroName.setText(tem.getName());
        heroSex.setText(tem.getSex());
        heroBirth.setText(tem.getDate());
        heroAddress.setText(tem.getPlace());
        heroBelong.setText(tem.getState());
        heroIntroduction.setText(tem.getIntroduction());
        heroAttack.setText(""+ tem.getForce());
        heroIntelligence.setText(""+ tem.getIntelligence());
        heroLeadership.setText(""+ tem.getLeadership());
        heroFood.setText(""+ tem.getForage());
        heroImage.setImageResource(tem.getHeroImageId());
        /*
         * 从人物详情跳转过来时，人物头像不为空，所以在设置了界面信息之后，需要修改defaultImageId
         * 头像不是默认的图片，修改defaultImageId，使得它不等于默认的图片的id */
        defaultImageId = R.mipmap.ic_take_photo + 1;
    }
}
