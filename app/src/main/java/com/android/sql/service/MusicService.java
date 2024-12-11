package com.android.sql.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.android.sql.activity.R;

// MusicService类继承自Service，用于在安卓应用后台执行与音乐播放相关的操作，比如播放音频文件、控制播放状态等，
// 可以独立于应用的Activity运行，即使用户切换到其他应用或者手机屏幕关闭等情况下，仍能持续提供音乐播放服务。
public class MusicService extends Service {
    private MediaPlayer player;// 定义一个MediaPlayer对象，用于控制音频的播放、暂停、停止等操作，它是安卓中用于处理多媒体音频播放的核心类。
    public static boolean isplay;// 定义一个静态的布尔变量，用于记录音乐当前是否正在播放的状态，方便在不同的地方（比如其他类中）获取和判断音乐播放情况。

    // 公共的无参构造函数，目前为空实现，在Service实例化时会被调用，
    // 但通常一些重要的初始化操作会放在onCreate等生命周期方法中进行，而不是在此构造函数里。
    public MusicService() {
    }

    // 重写Service的onBind方法，该方法用于返回一个IBinder对象，用于实现与服务端的通信，
    // 当其他组件（比如Activity）通过bindService方式绑定到这个服务时，会调用此方法获取通信通道，
    // 这里直接抛出一个UnsupportedOperationException异常，表示当前服务暂不支持绑定操作，即没有实现绑定相关的功能。
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    int[] audioFiles = {R.raw.first, R.raw.second, R.raw.third};
    int currentIndex = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        playAudio();
    }

    private void playAudio() {
        if (currentIndex < audioFiles.length) {
            int resId = getResources().getIdentifier("audio_file_name", "raw", getPackageName());
            player = MediaPlayer.create(this, audioFiles[currentIndex]);
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentIndex++;
                    playAudio();
                }
            });
        }
    }



    // 重写Service的onCreate方法，此方法会在服务首次创建时被调用，通常用于进行一些一次性的初始化操作，
    // 在这里创建一个MediaPlayer对象，并通过MediaPlayer的create方法加载指定的音频资源（R.raw.first表示在res/raw目录下的first音频文件），
    // 然后设置音频循环播放（setLooping(true)），保证音乐可以持续不断地播放，最后调用父类的onCreate方法完成其他必要的初始化工作。

   /* public void onCreate() {
        player = MediaPlayer.create(this, R.raw.first);
        player.setLooping(true);
        super.onCreate();
    }*/

    // 重写Service的onStartCommand方法，每当通过startService方法启动服务时，都会调用此方法，
    // 在这里判断如果媒体播放器（player）当前没有正在播放音乐，就调用start方法开始播放音频，
    // 并且更新isplay静态变量的值，使其反映当前音乐播放的实际状态（通过player.isPlaying()获取），
    // 最后返回父类的onStartCommand方法的执行结果，以确定服务后续的行为（比如在系统资源紧张等情况下的重启策略等）。
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!player.isPlaying()) {
            player.start();
            isplay = player.isPlaying();
        }
        else{
            player.pause();
            isplay=player.isPlaying();//暂停播放
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // 重写Service的onDestroy方法，该方法在服务被销毁时调用，用于释放相关资源，进行一些清理工作，
    // 在这里先暂停正在播放的音频（通过player.pause()），再次更新isplay变量以反映当前停止播放的状态，
    // 然后调用MediaPlayer的release方法释放媒体播放器占用的系统资源，最后调用父类的onDestroy方法完成其他必要的清理操作。
    @Override
    public void onDestroy() {
        player.pause();
        isplay = player.isPlaying();
        player.release();
        super.onDestroy();
    }
}