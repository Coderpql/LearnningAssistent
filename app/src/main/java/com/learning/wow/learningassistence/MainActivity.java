package com.learning.wow.learningassistence;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.wow.learningassistence.fragment.NewsFragment;
import com.learning.wow.learningassistence.fragment.NoteFragment;
import com.learning.wow.learningassistence.fragment.ToolFragment;
import com.learning.wow.learningassistence.fragmentActivity.MoreDataActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private RadioButton mNoteButton;
    private RadioButton mToolButton;
    private RadioButton mNewsButton;
    private ArrayList<Fragment> mFragmentList;
    private Toolbar mToolbar;
    private TextView mTitleTextView;
    private Uri imageUri;
    private CircleImageView circleImageView;
    private Button mEditButton;
    private TextView mIdTextView;
    private TextView mSchoolTextView;
    private TextView mEmailTextView;
    public static final String EXTRA_CHOOSE_PHOTO= "com.learning.wow.learningassistence.mainactivity";
    public static final int TAKE_PHOTO = 0;
    private static final int CHOOSE_PHOTO = 1;
    private static final int MORE_DATA = 2;
    private File iconImage;
    private DrawerLayout mDrawerLayout;
    private ImageButton mOpenDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化界面和ViewPager
        initView();
        initViewPager();
        mTitleTextView.setText("记事本");
        setSupportActionBar(mToolbar);
    }
    public void initView(){
        mRadioGroup =(RadioGroup)findViewById(R.id.main_radio_group);

        mNoteButton =(RadioButton)findViewById(R.id.note_button);
        mToolButton =(RadioButton)findViewById(R.id.tools_button);
        mNewsButton =(RadioButton)findViewById(R.id.news_button);
        mOpenDrawer = (ImageButton)findViewById(R.id.open_drawer_button);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mTitleTextView=(TextView)findViewById(R.id.toolbar_title);

        NavigationView navigationView = (NavigationView)findViewById(R.id.personal_info) ;
        View headerLayout = navigationView.inflateHeaderView(R.layout.personnal_header);
        circleImageView = (CircleImageView) headerLayout.findViewById(R.id.icon);
        mIdTextView = (TextView)headerLayout.findViewById(R.id.user_name);
        mSchoolTextView = (TextView)headerLayout.findViewById(R.id.school);
        mEmailTextView = (TextView)headerLayout.findViewById(R.id.email);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mRadioGroup.setOnCheckedChangeListener(this);

        mOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setCheckedItem(R.id.more);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.more:
                        Intent intent = new Intent(MainActivity.this, MoreDataActivity.class);
                        startActivityForResult(intent,MORE_DATA);
                        break;
                    default:
                        break;
                }
              return true;
            }
        });

        //创建File对象用于存储拍照后的照片
        iconImage = new File(getExternalCacheDir(),"iconImage.jpg");
        try{
            iconImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(MainActivity.this,
                    "com.learning.wow.learningassistence.fileprovider",iconImage);
        }else {
            imageUri = Uri.fromFile(iconImage);
        }
        try{
            //设置头像
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            if (bitmap != null){
                circleImageView.setImageBitmap(bitmap);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        updateData();
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTypeDialog();
            }
        });

    }
    private void showTypeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this,R.layout.choose_photo_method,null);
        TextView takePhoto = (TextView)view.findViewById(R.id.take_photo_text_view);
        TextView choosePhoto = (TextView)view.findViewById(R.id.choose_from_album_text_view);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
                dialog.dismiss();
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从相册中选择照片
                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new
                            String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
    private void openAlbum(){
        //打开相册选择照片
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"您未获取相关权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        new MyAdapter(getSupportFragmentManager(),mFragmentList).getItem(1).onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK){
            return;
        }
        if (requestCode == CHOOSE_PHOTO){
            //判断手机系统版本号
            if (Build.VERSION.SDK_INT >= 19){
                //4.4及以上系统使用这个方法处理图片
                handleImageOnKitKat(data);
            }else {
                //4.4以下的系统使用这个方法处理图片
                handleImageBeforeKitKat(data);
            }
        }
        if (requestCode == TAKE_PHOTO){
            update(imageUri);
        }
        if (requestCode == MORE_DATA){
            updateData();
        }
    }
    public void updateData(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String ID = pref.getString("id","");
        String School = pref.getString("school","");
        String Email = pref.getString("email","");
        if (ID != ""){
            mIdTextView.setText(ID);
            mSchoolTextView.setText(School);
            mEmailTextView.setText(Email);
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri，则通过 document id处理
            String docId = DocumentsContract.getDocumentId(uri);//获取图片数据库数据表里指定的行
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                //获取Uri的路径 getAuthority()
                String id = docId.split(":")[1];//解析出数字格式的ID 以：为分割符 分割后将两部分存到一个数组中 数字id部分在后半部分
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //getScheme()：获取资源使用的协议，例如http、ftp等，没有默认值
            //如果不是document类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    public String getImagePath(Uri uri,String selection){
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//解析出相应图片的Bitmap
            try {
                FileOutputStream out = new FileOutputStream(iconImage);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                Log.d("icon", "已经保存");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            circleImageView.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"Failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
    public void initViewPager(){
        mViewPager = (ViewPager)findViewById(R.id.main_view_pager);

        mFragmentList = new ArrayList<Fragment>();

        Fragment noteFragment = new NoteFragment();
        Fragment toolFragment = new ToolFragment();
        Fragment newsFragment = new NewsFragment();

        //将各Fragment添加到数组中
        mFragmentList.add(noteFragment);
        mFragmentList.add(toolFragment);
        mFragmentList.add(newsFragment);

        //设置ViewPager的适配器
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),mFragmentList));
        //当前为第一个页面
        mViewPager.setCurrentItem(0);
        //ViewPager页面改变监听器
        mViewPager.addOnPageChangeListener(new MyListener());
    }
    public class MyAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> mList;
        public MyAdapter(FragmentManager fm,ArrayList<Fragment> mList){
            super(fm);
            this.mList = mList;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
    public class MyListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //获取当前页面用于改变对应RadioButton的状态
            int current = mViewPager.getCurrentItem();
            switch (current){
                case 0:
                    mTitleTextView.setText("记事本");
                    mRadioGroup.check(R.id.note_button);
                    break;
                case 1:
                    mTitleTextView.setText("工具箱");
                    mRadioGroup.check(R.id.tools_button);
                    break;
                case 2:
                    mTitleTextView.setText("每日推荐");
                    mRadioGroup.check(R.id.news_button);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        //获取当前被选中的RadioButton的ID，用于改变ViewPager的当前页
        int current = 0;
        switch(i){
            case R.id.note_button:
                current = 0;
                break;
            case R.id.tools_button:
                current = 1;
                break;
            case R.id.news_button:
                current = 2;
                break;
        }
        if (mViewPager.getCurrentItem()!=current){
            mViewPager.setCurrentItem(current);
        }
    }
    public void update(Uri imageUri){
        if (imageUri != null){
            try{
                //设置头像
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                circleImageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}
