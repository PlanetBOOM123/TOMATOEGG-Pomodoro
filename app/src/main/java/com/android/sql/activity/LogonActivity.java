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

/**
 * 用户注册界面Activity
 * 实现新用户注册功能，包含账号、密码输入和头像选择
 */
public class LogonActivity extends AppCompatActivity {
    // 界面控件声明
    private EditText et_account,et_password1,et_password2;  // 账号输入框，密码输入框，确认密码输入框
    private Button btn_reg;      // 注册按钮
    private TextView tv_back;    // 返回按钮
    private ImageView iv_touxiang;  // 头像显示控件

    // 保存头像的Base64字符串
    private String 头像="";

    // 用户数据访问对象
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // 初始化界面控件
        initView();

        // 设置返回按钮点击事件
        tv_back.setOnClickListener(v -> finish());
        // 设置注册按钮点击事件
        btn_reg.setOnClickListener(v -> btn_reg());
        // 设置头像选择点击事件
        iv_touxiang.setOnClickListener(v -> gettouxiang());
    }

    /**
     * 选择头像方法
     * 调用系统相册选择图片作为头像
     */
    private void gettouxiang() {
        // 创建打开相册的Intent
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // 设置数据类型为图片
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        // 启动相册选择界面
        startActivityForResult(intent, 1);
    }

    /**
     * 注册按钮点击处理方法
     * 验证用户输入并进行注册操作
     */
    private void btn_reg() {
        // 获取用户输入
        String account = et_account.getText().toString().trim();
        String password1 = et_password1.getText().toString().trim();
        String password2 = et_password2.getText().toString().trim();

        // 验证输入是否完整
        if (account.isEmpty() || password1.isEmpty() || password2.isEmpty()){
            Toast.makeText(this, "请输入注册信息", Toast.LENGTH_SHORT).show();
            return;
        }

        // 验证是否选择了头像
        if (头像.isEmpty()){
            Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show();
            return;
        }

        // 验证两次输入的密码是否一致
        if (!password1.equals(password2)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查账号是否已被注册
        UserBean userBean = userDao.query(account);
        if (userBean == null){
            // 账号未注册，可以创建新用户
            // 参数说明：id(0表示自增)、头像、账号、密码、简介(空)、性别(0)、年龄(0)
            userDao.insert(new UserBean(0,头像,account,password1,"",0,0));
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            // 账号已存在
            Toast.makeText(this, "此账号已注册", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化界面控件和必要的对象
     */
    private void initView() {
        // 初始化用户数据访问对象
        userDao = new UserDao(this);
        // 绑定界面控件
        et_account = findViewById(R.id.et_account);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        tv_back = findViewById(R.id.tv_back);
        btn_reg = findViewById(R.id.btn_reg);
        iv_touxiang = findViewById(R.id.iv_touxiang);
    }

    /**
     * 处理相册选择图片返回的结果
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {  // 确认是从相册选择图片返回的结果
            if (data != null) {
                // 获取选择的图片URI
                Uri uri = data.getData();
                // 压缩图片大小
                Bitmap bitmap = ImageSizeCompress(this,uri);
                // 显示压缩后的图片
                iv_touxiang.setImageBitmap(bitmap);
                // 将图片转换为Base64字符串存储
                头像 = bitmapToBase64(bitmap);
            }
        }
    }
}