package com.example.tomcat;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    private RelativeLayout layout_animation;
    private SoundPool pool;//SoundPool可以同时播放多个声音 短声音
    private HashMap<Integer,Integer> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHashMap();
        initView();
    }

    private void initHashMap(){
        hashMap=new HashMap<Integer,Integer>();
        pool=new SoundPool(3, AudioManager.STREAM_MUSIC,1);//同时播放最大数量 类型 音质
        //将音乐加载到集合中
        hashMap.put(1,pool.load(this,R.raw.fart003_11025,1));
        hashMap.put(2,pool.load(this,R.raw.cymbal,1));
        hashMap.put(3,pool.load(this,R.raw.p_poke_foot3,1));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(x1 - x2 > 50) {
                Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
            } else if(x2 - x1 > 50) {
                Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
                playAnimation();
                playSound(1);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 播放声音
     */
    private void playSound(int key){
        pool.play(hashMap.get(3),1,1,1,0,1);//声音id,左声道，右声道，优先级，循环，速率
    }

    /**
     * 播放动画
     */
    private void playAnimation(){
        layout_animation.setBackgroundResource(R.drawable.fart);
        AnimationDrawable anim = (AnimationDrawable) layout_animation.getBackground();
        anim.setOneShot(true);//播放一次
        if(anim.isRunning()){//如果动画已经播放过
            anim.stop();//如果动画运行了，停止动画，动画才可以重新播放
        }
        anim .start();

        int time=0;
        for(int i=0;i<anim.getNumberOfFrames();i++){
            time+=anim.getDuration(i);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新设置背景,并播放动画
                layout_animation.setBackgroundResource(R.drawable.breath);
                AnimationDrawable anim2 = (AnimationDrawable) layout_animation.getBackground();
                anim2 .start();
            }
        },time);
    }

    private void initView(){
        layout_animation= (RelativeLayout) findViewById(R.id.layout_animation);
        //动画集
        AnimationDrawable anim = (AnimationDrawable) layout_animation.getBackground();
        anim .start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_animation:
                break;
            default:
                break;
        }
    }
}
