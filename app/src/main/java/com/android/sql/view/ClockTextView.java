package com.android.sql.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.sql.MyApplication;

// ClockTextView类继承自TextView，是一个自定义的文本视图类，其目的可能是为了让该文本视图应用特定的字体样式，
// 通过关联应用程序全局的字体设置（从MyApplication类获取）来实现统一的文本显示风格。
@SuppressLint("AppCompatCustomView")
public class ClockTextView extends TextView {

    // 构造函数，在仅传入Context的情况下初始化ClockTextView实例，
    // 它会调用父类（TextView）的构造函数完成基础的初始化工作，
    // 然后通过调用setTypeface方法设置该文本视图的字体样式，字体样式从MyApplication类的单例实例中获取，
    // 这样就能保证该文本视图使用应用程序全局设置的特定字体进行文本显示。
    public ClockTextView(Context context) {
        super(context);
        setTypeface(MyApplication.getInstace().getTypeface());
    }

    // 构造函数，在传入Context和AttributeSet的情况下初始化ClockTextView实例，
    // 这种情况常用于在XML布局文件中使用自定义视图时，系统会传入相应的属性集合（AttributeSet），
    // 同样先调用父类构造函数进行基础初始化，然后设置字体样式为应用程序全局的特定字体，确保文本显示风格的一致性。
    public ClockTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(MyApplication.getInstace().getTypeface());
    }

    // 构造函数，传入Context、AttributeSet以及默认样式属性（defStyleAttr）来初始化ClockTextView实例，
    // 此构造函数在需要遵循特定主题样式等更多样式相关设置时使用，
    // 先完成父类对应构造函数的调用进行初始化，之后设置字体为全局的特定字体，实现统一的文本字体展示效果。
    public ClockTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(MyApplication.getInstace().getTypeface());
    }

    // 构造函数，传入Context、AttributeSet、默认样式属性（defStyleAttr）以及额外的样式资源（defStyleRes）来初始化ClockTextView实例，
    // 这是最完整的构造函数形式，可处理更多复杂的样式设定情况，
    // 先是调用父类构造函数完成初始化流程，然后设置字体为应用程序全局的特定字体，保证文本按照设定的统一字体进行显示。
    public ClockTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface(MyApplication.getInstace().getTypeface());
    }
}