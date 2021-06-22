package com.example.filesmanager.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.filesmanager.R
import java.io.File as File

class PlayerMusicActivity : AppCompatActivity() {

    lateinit var songExtraData:Bundle
    lateinit var prev:ImageView
    lateinit var play :ImageView
    lateinit var next :ImageView
    private var position:Int = 0
    lateinit var mSeekBarTime:SeekBar
    lateinit var mMediaPlayer: MediaPlayer
    lateinit var songName:TextView
    lateinit var musicList :ArrayList<File>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_music)
        prev = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        mSeekBarTime = findViewById(R.id.mSeekBarTime);
        songName  = findViewById(R.id.songName);
        if(mMediaPlayer != null){
            mMediaPlayer.stop()
        }
        var intent :Intent = getIntent()
        songExtraData = intent.extras!!
       // musicList = songExtraData!!.getParcelableArrayList<File>("songsList") as ArrayList<File>
        position = songExtraData.getInt("position",0)
    }
}