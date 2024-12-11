package com.android.sql;

import android.app.Application;
import android.graphics.Typeface;

// MyApplication类继承自Application，通常用于在整个应用程序范围内进行一些全局的初始化操作、资源管理以及提供全局可访问的实例和数据等
public class MyApplication extends Application {
    // 定义一个静态的MyApplication实例变量，用于保存整个应用程序唯一的MyApplication对象实例，方便在其他地方获取到该实例
    private static MyApplication instance;
    // 用于存储自定义的字体类型（Typeface）对象，后续可以通过该变量获取并应用到相应的文本显示组件上，以实现使用特定字体展示文字的效果
    private Typeface typeface;

    // 重写Application的onCreate方法，该方法会在应用程序启动时被自动调用，在这里进行一些全局的初始化工作
    @Override
    public void onCreate() {
        super.onCreate();
        // 将当前应用的上下文（ApplicationContext）转换并赋值给instance变量，使得instance指向整个应用程序唯一的MyApplication实例
        instance = (MyApplication) getApplicationContext();
        // 通过调用Typeface的createFromAsset方法，从应用程序的资源资产（Assets）目录下加载名为"fonts/led.ttf"的字体文件，创建对应的Typeface对象，并赋值给typeface变量
        // 这里假设"fonts/led.ttf"是已经下载好放置在Assets目录下的自定义字体文件，通过这种方式可以在应用中使用该自定义字体
        typeface = Typeface.createFromAsset(instance.getAssets(), "fonts/led.ttf");
    }

    // 定义一个静态方法，用于在其他类中获取MyApplication的唯一实例，方便访问该类中提供的全局资源或方法等
    public static MyApplication getInstace() {
        return instance;
    }

    // 定义一个方法，用于获取之前创建并存储的Typeface对象，外部类可以调用该方法获取到应用中设置的自定义字体对象，进而应用到文本显示相关的操作中
    public Typeface getTypeface() {
        return typeface;
    }

    // 定义一个方法，用于重新设置Typeface对象，外部可以通过该方法更新应用中使用的字体，例如切换不同的自定义字体等情况时可以使用
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }
}