package com.android.sql.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.sql.Data;
import com.android.sql.bean.UserBean;
import com.android.sql.dao.UserDao;

/**
 * 登录界面Activity
 * 实现用户登录功能，包含账号密码输入、登录状态保存、自动登录等功能
 */
public class LoginActivity extends AppCompatActivity {
    // 界面控件
    private EditText et_account,et_password;  // 账号密码输入框
    private Button btn_login;                 // 登录按钮
    private UserDao userDao;                  // 用户数据访问对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化界面控件
        initView();

        // 设置登录按钮点击事件
        btn_login.setOnClickListener(v -> btn_login());

        // 设置注册文本点击事件，跳转到注册界面
        findViewById(R.id.textView2).setOnClickListener(v -> startActivity(new Intent(this,LogonActivity.class)));

        // 设置忘记密码文本点击事件，显示联系管理员提示
        findViewById(R.id.textView3).setOnClickListener(v -> Toast.makeText(this, "请联系管理员微信：", Toast.LENGTH_SHORT).show());

        // 检查是否有保存的登录状态，有则自动登录
        getlogin();
    }

    /**
     * 登录按钮点击处理方法
     * 验证用户输入并进行登录操作
     */
    private void btn_login(){
        // 获取用户输入的账号和密码
        String account = et_account.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        // 检查输入是否为空
        if (account.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "请输入登录信息", Toast.LENGTH_SHORT).show();
            return;
        }

        // 查询用户信息
        UserBean userBean = userDao.query(account);
        if (userBean == null){
            // 账号不存在
            Toast.makeText(this, "账号未注册", Toast.LENGTH_SHORT).show();
        }else {
            // 验证密码
            String pw = userBean.getPassword();
            if (pw.equals(password)){
                // 密码正确，登录成功
                Data.userBean = userBean;
                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                // 保存登录状态
                save(account,password);
                // 跳转到主界面
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }else {
                // 密码错误
                Toast.makeText(this, "密码输入错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化界面控件和必要的对象
     */
    private void initView() {
        // 初始化用户数据访问对象
        userDao = new UserDao(this);

        // 设置透明状态栏和导航栏（仅在Android 4.4及以上版本生效）
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        // 绑定界面控件
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
    }

    /**
     * 保存登录状态
     * @param ac 账号
     * @param pw 密码
     */
    private void save(String ac,String pw){
        // 获取SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        // 清除旧数据
        editor.clear();
        // 保存新的账号密码
        editor.putString("ac",ac);
        editor.putString("pw",pw);
        editor.commit();
    }

    /**
     * 检查登录状态并自动登录
     * 从SharedPreferences中读取保存的账号密码，验证后自动登录
     */
    private void getlogin(){
        // 获取SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

        // 读取保存的账号密码
        String account = sp.getString("ac","");
        String password = sp.getString("pw","");

        // 如果没有保存的登录信息，直接返回
        if (account.isEmpty() || password.isEmpty()){
            return;
        }

        // 验证登录信息
        UserBean userBean = userDao.query(account);
        if (userBean == null){
            Toast.makeText(this, "异常", Toast.LENGTH_SHORT).show();
        }else {
            String pw = userBean.getPassword();
            if (pw.equals(password)){
                // 验证成功，执行自动登录
                Data.userBean = userBean;
                // 更新保存的登录状态
                save(account,password);
                // 跳转到主界面
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }else {
                // 密码已变更，登录状态失效
                Toast.makeText(this, "登录状态过期", Toast.LENGTH_SHORT).show();
            }
        }
    }
}