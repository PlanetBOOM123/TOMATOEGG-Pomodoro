package com.android.sql.activity;



import static com.android.sql.utils.BitmapBase64Util.ImageSizeCompress;
import static com.android.sql.utils.BitmapBase64Util.bitmapToBase64;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.sql.dao.UserDao;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sql.bean.UserBean;


public class LogonActivity extends AppCompatActivity {
    private EditText et_account,et_password1,et_password2;
    private Button btn_reg;
    private TextView tv_back;
    private ImageView iv_touxiang;

    private String 头像="";

    private UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        initView();

        tv_back.setOnClickListener(v -> finish());
        btn_reg.setOnClickListener(v -> btn_reg());
        iv_touxiang.setOnClickListener(v -> gettouxiang());
    }

    private void gettouxiang() {
        // 获取头像
        //打开相册
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    private void btn_reg() {
        //  注册
        String account = et_account.getText().toString().trim();
        String password1 = et_password1.getText().toString().trim();
        String password2 = et_password2.getText().toString().trim();
        if (account.isEmpty() || password1.isEmpty() || password2.isEmpty()){
            Toast.makeText(this, "请输入注册信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (头像.isEmpty()){
            Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password1.equals(password2)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        UserBean userBean = userDao.query(account);
        if (userBean == null){
            // 添加用户到数据库—————— 注册
            userDao.insert(new UserBean(0,头像,account,password1,"",0,0));
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, "此账号已注册", Toast.LENGTH_SHORT).show();
        }
    }


    private void initView() {
        userDao = new UserDao(this);
        et_account = findViewById(R.id.et_account);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        tv_back = findViewById(R.id.tv_back);
        btn_reg = findViewById(R.id.btn_reg);
        iv_touxiang = findViewById(R.id.iv_touxiang);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                Bitmap bitmap = ImageSizeCompress(this,uri);
                iv_touxiang.setImageBitmap(bitmap);
                头像 = bitmapToBase64(bitmap);
            }
        }
    }
}