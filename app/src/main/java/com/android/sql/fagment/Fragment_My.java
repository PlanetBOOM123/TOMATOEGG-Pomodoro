package com.android.sql.fagment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.sql.Data;
import com.android.sql.activity.FocusActivity;
import com.android.sql.activity.LoginActivity;
import com.android.sql.activity.R;
import com.android.sql.bean.UserBean;
import com.android.sql.dao.UserDao;
import com.android.sql.utils.BitmapBase64Util;
import com.android.sql.view.FocusSettingDialog;

/**
 * 个人中心Fragment
 * 显示用户个人信息，提供修改密码、设置签名、退出登录等功能
 */
public class Fragment_My extends Fragment {
    // 界面相关变量
    private View settingView;     // Fragment的根View
    private Context context;      // 上下文对象

    // 界面控件
    private LinearLayout ll_msg;  // 修改密码按钮
    private LinearLayout ll_back; // 退出登录按钮
    private LinearLayout ll_zz;   // 专注设置按钮
    private TextView tv_jb;       // 金币数量显示
    private TextView tv_jf;       // 积分显示
    private TextView tv_name;     // 用户名显示
    private TextView tv_qm;       // 个性签名显示
    private ImageView iv_tx;      // 头像显示

    // 数据访问对象
    private UserDao userDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载Fragment布局
        settingView = inflater.inflate(R.layout.module_fragment_my, container, false);
        context = settingView.getContext();

        // 初始化视图和数据
        initView();

        // 设置各个按钮的点击事件
        ll_msg.setOnClickListener(v -> gerenxinxi());  // 修改密码
        ll_back.setOnClickListener(v -> back());       // 退出登录
        tv_qm.setOnClickListener(v -> setQM());       // 设置签名
        ll_zz.setOnClickListener(view -> zdy());      // 专注设置

        return settingView;
    }

    /**
     * 显示专注设置对话框
     */
    private void zdy() {
        FocusSettingDialog dialog = new FocusSettingDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "focus_setting");
    }

    /**
     * 设置个性签名
     * 弹出对话框让用户输入新的签名
     */
    private void setQM() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.qm_windows, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(view);
        alertDialog.show();

        // 获取输入框并设置当前签名
        EditText et_qm = view.findViewById(R.id.et_qm);
        et_qm.setText(tv_qm.getText().toString());

        // 取消按钮点击事件
        view.findViewById(R.id.button3).setOnClickListener(v -> alertDialog.dismiss());

        // 确认按钮点击事件
        view.findViewById(R.id.button4).setOnClickListener(v -> {
            String qm = et_qm.getText().toString().trim();

            // 验证输入
            if (qm.isEmpty()){
                Toast.makeText(context, "请输入內容", Toast.LENGTH_SHORT).show();
                return;
            }

            // 更新签名
            UserBean userBean = Data.userBean;
            userBean.setGxmsg(qm);
            userDao.update(userBean);
            tv_qm.setText(qm);
            Data.userBean = userBean;
            alertDialog.dismiss();
        });
    }

    /**
     * 退出登录
     * 清除登录状态并返回登录界面
     */
    private void back() {
        // 清除登录信息
        SharedPreferences sp = context.getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        // 跳转到登录界面
        startActivity(new Intent(context, LoginActivity.class));
        getActivity().finish();
    }

    /**
     * 修改密码
     * 弹出对话框让用户输入新密码
     */
    private void gerenxinxi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.twindows, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(view);
        alertDialog.show();

        // 获取密码输入框
        EditText et_pw1 = view.findViewById(R.id.et_id);
        EditText et_pw2 = view.findViewById(R.id.et_name);

        // 取消按钮点击事件
        view.findViewById(R.id.button3).setOnClickListener(v -> alertDialog.dismiss());

        // 确认按钮点击事件
        view.findViewById(R.id.button4).setOnClickListener(v -> {
            String pw1 = et_pw1.getText().toString().trim();
            String pw2 = et_pw2.getText().toString().trim();

            // 验证输入
            if (pw1.isEmpty()||pw2.isEmpty()){
                Toast.makeText(context, "请输入內容", Toast.LENGTH_SHORT).show();
                return;
            }

            // 验证两次密码是否一致
            if (pw1.equals(pw2)){
                // 更新密码
                UserBean userBean = Data.userBean;
                userBean.setPassword(pw1);
                userDao.update(userBean);
                // 退出到登录界面
                back();
            }else {
                Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }

            alertDialog.dismiss();
        });
    }

    /**
     * 初始化界面
     * 绑定控件并设置初始数据
     */
    private void initView() {
        userDao = new UserDao(context);

        // 绑定控件
        ll_msg = settingView.findViewById(R.id.ll_msg);
        ll_zz = settingView.findViewById(R.id.ll_zz);
        ll_back = settingView.findViewById(R.id.ll_back);
        tv_jb = settingView.findViewById(R.id.tv_jb);
        tv_jf = settingView.findViewById(R.id.tv_jf);
        tv_name = settingView.findViewById(R.id.tv_name);
        tv_qm = settingView.findViewById(R.id.tv_qm);
        iv_tx = settingView.findViewById(R.id.iv_tx);

        // 设置用户信息
        tv_name.setText(Data.userBean.getAccount());
        tv_qm.setText(Data.userBean.getGxmsg().isEmpty()?"【请设置个性签名】":Data.userBean.getGxmsg());
        tv_jb.setText(""+Data.userBean.getGold());
        tv_jf.setText(""+Data.userBean.getHistory_gold());
        iv_tx.setImageBitmap(BitmapBase64Util.base64ToBitmap(Data.userBean.getTouxiang()));
    }

    // Fragment相关参数
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    /**
     * 必需的空构造函数
     */
    public Fragment_My() {
        // Required empty public constructor
    }

    /**
     * 创建Fragment实例的工厂方法
     */
    public static Fragment_My newInstance(String param1, String param2) {
        Fragment_My fragment = new Fragment_My();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}