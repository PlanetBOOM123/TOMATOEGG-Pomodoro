package com.android.sql.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.sql.activity.FocusActivity;
import com.android.sql.activity.R;

public class FocusSettingDialog extends DialogFragment {
    private NumberPicker npFocus;
    private NumberPicker npRest;
    private NumberPicker npCycles;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_focus_setting, null);

        // 初始化NumberPicker
        npFocus = view.findViewById(R.id.np_focus);
        npRest = view.findViewById(R.id.np_rest);
        npCycles = view.findViewById(R.id.np_cycles);

        // 设置专注时长选择范围：5-120分钟
        npFocus.setMinValue(5);
        npFocus.setMaxValue(120);
        npFocus.setValue(25);

        // 设置休息时长选择范围：1-30分钟
        npRest.setMinValue(1);
        npRest.setMaxValue(30);
        npRest.setValue(5);

        // 设置循环次数选择范围：1-10次
        npCycles.setMinValue(1);
        npCycles.setMaxValue(10);
        npCycles.setValue(2);

        builder.setTitle("自定义专注模式")
                .setView(view)
                .setPositiveButton("开始", (dialog, which) -> {
                    // 获取用户设置的值并启动FocusActivity
                    Intent intent = new Intent(requireContext(), FocusActivity.class);
                    intent.putExtra("focus_minutes", npFocus.getValue());
                    intent.putExtra("rest_minutes", npRest.getValue());
                    intent.putExtra("cycles", npCycles.getValue());
                    startActivity(intent);
                })
                .setNegativeButton("取消", null);

        return builder.create();
    }
}