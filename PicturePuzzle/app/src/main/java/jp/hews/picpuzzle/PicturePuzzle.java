/*
Copyright (c) 2009 Hideo KINAMI

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

/*
  絵合わせパネルゲーム
*/
package jp.hews.picpuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock; 
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;

// パネルゲームの画面の制御クラス
public class PicturePuzzle extends Activity {

    // （1）パネルを構成するボタン
    static final int mImageButtons[] = {
        R.id.image_button1, R.id.image_button2, R.id.image_button3,
        R.id.image_button4, R.id.image_button5, R.id.image_button6,
        R.id.image_button7, R.id.image_button8, R.id.image_button9
    };

    // （2）パネルに表示する画像の初期状態
    // 画像がこの状態になったら完成と判定する
    static final int mInitialImages[] = {
        R.drawable.image1, R.drawable.image2, R.drawable.image3,
        R.drawable.image4, R.drawable.image5, R.drawable.image6,
        R.drawable.image7, R.drawable.image8, R.drawable.blank_image
    };

    /* （3）交換可能な（隣接した）パネルのインデックス
        たとえば、0番（左上）のパネルは、1番（中央上）のパネルと
        3番（左中央）のパネルと隣り合っていることを{1,3}で表している
    */
    static final int mNextPanelIndex[][] = {
        {1,3},   {0,4,2},   {1,5},
        {0,4,6}, {1,3,5,7}, {2,4,8},
        {3,7},   {4,6,8},   {7,5}
    };
        
    // （4）ボタンを制御するためのコントローラの配列
    PanelController mPanels[] = 
        new PanelController[mImageButtons.length];

    // ボタンを制御するためのコントローラ
    class PanelController implements View.OnClickListener {
        ImageButton mButton;    // 制御するボタン
        int mCurrentImage = 0;  // 表示している画像のリソースID
        final int mNext[];      // 移動可能なパネル

        // 制御するボタン、画像、交換可能なパネルを指定
        public PanelController(ImageButton button,
                               int resid,
                               final int next[]) {
            mButton = button;
            mNext = next;
            setImageResource(resid);
            mButton.setOnClickListener(this);
        }
        
        // （5）新たな画像のリソースIDを設定する
        // 現在設定している画像のリソースIDを返す
        public int setImageResource(int resid) {
            int old = mCurrentImage;
            mCurrentImage = resid;
            mButton.setImageResource(resid);
            return old;
        }
        
        // 現在設定している画像のリソースIDを返す
        public int getImageResource() {
            return mCurrentImage;
        }
        
        // 表示しているのが空白画像ならtrueを返す
        public boolean isBlank() {
            return mCurrentImage == R.drawable.blank_image;
        }
        
        // （6）他のパネルと画像を交換する
        public void swapImage(PanelController other) {
            int previous = other.setImageResource(mCurrentImage);
            setImageResource(previous);
        }

        // （7）ボタンのタップ時の動作
        public void onClick(View v) {
            for(int i=0; i<mNext.length; ++i) {
        // 隣接するパネルが空白画像であれば、画像を交換する
                if (mPanels[mNext[i]].isBlank()) {
                    swapImage(mPanels[mNext[i]]);
                    break;
                } 
            }
            if (isCompleted())
                complete();
        }
    }

    // （8）アプリケーション起動時に呼び出されるメソッド
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        createPanelController();
        setShuffleButtonListener();
    }
    
    // （9）それぞれのボタンにPanelControllerを登録する
    private void createPanelController() {
        for(int i=0; i<mImageButtons.length; ++i) {
            ImageButton b = 
                (ImageButton)findViewById(mImageButtons[i]);
            mPanels[i] = new PanelController(b,
                                             mInitialImages[i],
                                             mNextPanelIndex[i]);
        }
    }
    
    // （10）パネルの画像が最初の状態と一致したら完成と判定する
    private boolean isCompleted() {
        for(int i=0; i<mInitialImages.length; ++i) {
            if (mInitialImages[i] != mPanels[i].getImageResource())
                return false;
        }
        return true;
    }

    // （11）パネルをシャッフルする
    private void shuffle() {
        int size = mInitialImages.length;
        for(int i=0; i<size-1; ++i) {
            int swap = (int)(Math.random()*(size-i));
            mPanels[i].swapImage(mPanels[i+swap]);
        }
    }

    // （12）現在時刻を設定して、タイマーを開始する
    private void startChronometer() {
        Chronometer c =
            (Chronometer)findViewById(R.id.chronometer);
        c.setBase(SystemClock.elapsedRealtime());
        c.start();
    }
    
    // （13）タイマーを停止して、経過時間を返す
    private long stopChronometer() {
        Chronometer c =
            (Chronometer)findViewById(R.id.chronometer);
        c.stop();
        return SystemClock.elapsedRealtime() - c.getBase();
    }
    
    // （14）シャッフルボタンが押された時の処理
    private void setShuffleButtonListener() {
        // シャッフルボタンを取り出す
        Button b = (Button)findViewById(R.id.shuffle_button);
        // タップ時に呼び出されるリスナーを登録する
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shuffle();
                startChronometer();
            }});
    }
    
    // （15）完成時にダイアログを表示する
    private void complete() {
        long msec = stopChronometer();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.complete_title);
        b.setMessage(msec/1000 + " sec");
        b.setIcon(R.drawable.congratulations);
        b.setPositiveButton(
            R.string.complete_button,
            new DialogInterface.OnClickListener(){
                // ボタンが押されたらダイアログを閉じる
                public void onClick(DialogInterface dialog,int which) {
                    dialog.dismiss();
                }
            });
        b.show();
    }
}
