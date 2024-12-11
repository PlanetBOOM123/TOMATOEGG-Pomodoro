package com.android.sql.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import com.android.sql.Data;
import com.android.sql.bean.UserBean;
import com.android.sql.dao.UserDao;

/**
 * 专注计时器Activity
 * 实现番茄工作法，包含工作时间和休息时间的循环计时功能
 * 支持自定义专注时长、休息时长和循环次数
 * 每完成一次专注周期可获得金币奖励
 */
public class FocusActivity extends AppCompatActivity {
    // 界面控件
    private TextView tvTitle;    // 显示当前状态（专注中/休息中）
    private TextView tvTime;     // 显示倒计时时间
    private TextView tvCycle;    // 显示当前循环次数

    // 计时相关参数
    private int focusMinutes = 25;   // 专注时长（默认25分钟）
    private int restMinutes = 5;     // 休息时长（默认5分钟）
    private int cycles = 2;          // 总循环次数（默认2次）
    private int currentCycle = 1;    // 当前循环次数
    private boolean isFocusing = true;  // 当前是否处于专注状态

    // 系统服务
    private CountDownTimer timer;   // 倒计时器
    private Vibrator vibrator;      // 震动器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        // 初始化界面控件
        tvTitle = findViewById(R.id.tv_title);
        tvTime = findViewById(R.id.tv_time);
        tvCycle = findViewById(R.id.tv_cycle);

        // 初始化震动器服务
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // 获取Intent中传递的参数，设置计时参数
        Intent intent = getIntent();
        if (intent != null) {
            focusMinutes = intent.getIntExtra("focus_minutes", 25);
            restMinutes = intent.getIntExtra("rest_minutes", 5);
            cycles = intent.getIntExtra("cycles", 2);
        }

        // 初始化循环次数显示
        updateCycleText();

        // 开始第一次专注计时
        startFocusTimer();

        // 设置退出按钮点击事件
        findViewById(R.id.btn_exit).setOnClickListener(view -> finish());
    }

    /**
     * 更新循环次数显示
     * 显示格式：第 当前次数/总次数 轮
     */
    private void updateCycleText() {
        tvCycle.setText(String.format("第 %d/%d 轮", currentCycle, cycles));
    }

    /**
     * 开始专注计时
     * 设置标题为"专注中..."并启动专注时长的计时器
     */
    private void startFocusTimer() {
        tvTitle.setText("专注中...");
        isFocusing = true;
        startTimer(focusMinutes);
    }

    /**
     * 开始休息计时
     * 设置标题为"休息中..."并启动休息时长的计时器
     */
    private void startRestTimer() {
        tvTitle.setText("休息中...");
        isFocusing = false;
        startTimer(restMinutes);
    }

    /**
     * 启动计时器
     * @param minutes 需要计时的分钟数
     */
    private void startTimer(int minutes) {
        // 如果已有计时器在运行，先取消它
        if (timer != null) {
            timer.cancel();
        }

        // 将分钟转换为毫秒
        long millisInFuture = minutes * 60 * 1000;

        timer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 更新时间显示，格式：MM:SS
                int minutes = (int) (millisUntilFinished / 1000 / 60);
                int seconds = (int) (millisUntilFinished / 1000 % 60);
                tvTime.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                // 计时结束，震动提醒
                vibrate();

                // 专注结束后发放奖励（100金币）
                UserBean userBean = Data.userBean;
                userBean.setGold(userBean.getGold() + 100);
                userBean.setHistory_gold(userBean.getHistory_gold() + 100);
                UserDao userDao = new UserDao(FocusActivity.this);
                userDao.update_gold(userBean);

                // 处理计时结束后的状态转换
                if (isFocusing) {
                    // 专注结束，开始休息
                    startRestTimer();
                } else {
                    // 休息结束，判断是否需要开始下一轮
                    if (currentCycle < cycles) {
                        currentCycle++;
                        updateCycleText();
                        startFocusTimer();
                    } else {
                        // 所有循环完成，退出活动
                        finish();
                    }
                }
            }
        };

        timer.start();
    }

    /**
     * 执行震动提醒
     * 根据系统版本使用不同的震动方式
     */
    private void vibrate() {
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8.0及以上使用新的震动API
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Android 8.0以下使用旧的震动API
                vibrator.vibrate(1000);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 活动销毁时取消计时器，避免内存泄漏
        if (timer != null) {
            timer.cancel();
        }
    }
}