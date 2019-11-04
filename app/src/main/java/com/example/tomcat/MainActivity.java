package com.example.tomcat;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_pokeBelly;//腹部
    private Button btn_foot;//脚
    private Button btn_weiba;//尾巴
    private Button btn_leftFace;//左脸
    private Button btn_rightFace;//右脸

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    private RelativeLayout layout_animation;
    private SoundPool pool;//SoundPool可以同时播放多个声音 短声音
    private HashMap<Integer, Integer> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        btn_pokeBelly = (Button) findViewById(R.id.btn_pokeBelly);
        btn_pokeBelly.setOnClickListener(this);
        btn_foot = (Button) findViewById(R.id.btn_foot);
        btn_foot.setOnClickListener(this);
        btn_weiba = (Button) findViewById(R.id.btn_weiba);
        btn_weiba.setOnClickListener(this);
        btn_leftFace = (Button) findViewById(R.id.btn_leftFace);
        btn_leftFace.setOnClickListener(this);
        btn_rightFace = (Button) findViewById(R.id.btn_rightFace);
        btn_rightFace.setOnClickListener(this);

        initHashMap();
        initView();
    }

    private void initHashMap() {
        hashMap = new HashMap<Integer, Integer>();
        //SoundPool需要分版本进行处理（sdk版本21前后）
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入最多播放音频数量,
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            pool = builder.build();
        } else {
            pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);//同时播放最大数量 类型 音质
        }
        //将音乐加载到集合中
        hashMap.put(1, pool.load(this, R.raw.fart003_11025, 1));
        hashMap.put(2, pool.load(this, R.raw.cymbal, 1));
        hashMap.put(3, pool.load(this, R.raw.p_poke_foot3, 1));
        hashMap.put(4, pool.load(this, R.raw.p_belly, 1));
        hashMap.put(5, pool.load(this, R.raw.p_foot, 1));
        hashMap.put(6, pool.load(this, R.raw.angry, 1));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if (x1 - x2 > 50) {//向左滑

            } else if (x2 - x1 > 50) {//向右滑
                playAnimation(R.drawable.fart);
                playSound(hashMap.get(3));
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 播放声音
     */
    private void playSound(int soundId) {
        pool.play(soundId, 1, 1, 1, 0, 1);//声音id,左声道，右声道，优先级，循环，速率
    }

    /**
     * 播放动画
     */
    private void playAnimation(int res) {
        layout_animation.setBackgroundResource(res);
        AnimationDrawable anim = (AnimationDrawable) layout_animation.getBackground();
        anim.setOneShot(true);//是否播放一次(true将循环一次，然后停止并保持最后一帧。如果它设置为false，则动画将循环)
        if (anim.isRunning()) {//如果动画已经播放过
            anim.stop();//如果动画运行了，停止动画，动画才可以重新播放
        }
        //启动动画
        anim.start();

        int time = 0;
        for (int i = 0; i < anim.getNumberOfFrames(); i++) {
            time += anim.getDuration(i);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新设置背景,并播放动画
                layout_animation.setBackgroundResource(R.drawable.breath);
                AnimationDrawable anim2 = (AnimationDrawable) layout_animation.getBackground();
                anim2.start();
            }
        }, time);
    }

    private void initView() {
        layout_animation = (RelativeLayout) findViewById(R.id.layout_animation);
        //动画集
        AnimationDrawable anim = (AnimationDrawable) layout_animation.getBackground();
        anim.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_animation:
                break;
            case R.id.btn_pokeBelly://戳它腹部
                playAnimation(R.drawable.poke_belly_left);
                playSound(hashMap.get(4));
                break;
            case R.id.btn_foot://戳它脚
                playAnimation(R.drawable.poke_foot);
                playSound(hashMap.get(5));
                break;
            case R.id.btn_weiba://戳它尾巴
                playAnimation(R.drawable.poke_belly_right);
                playSound(hashMap.get(6));
                break;
            case R.id.btn_leftFace://打它左脸
                playAnimation(R.drawable.swipe_left);
                playSound(hashMap.get(2));
                break;
            case R.id.btn_rightFace://打它右脸
                playAnimation(R.drawable.swipe_right);
                playSound(hashMap.get(2));
                break;
            default:
                break;
        }
    }
}
