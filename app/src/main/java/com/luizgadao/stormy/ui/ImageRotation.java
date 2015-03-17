package com.luizgadao.stormy.ui;

import android.widget.ImageView;

/**
 * Created by luizcarlos on 17/03/15.
 */
public class ImageRotation {

    ImageView imageView;
    boolean rotation;
    int angle;

    public ImageRotation( ImageView imageView ) {
        this.imageView = imageView;
    }

    public void start(){
        rotation = true;
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                while ( rotation ) {
                    angle += 15;
                    angle %= 360;
                    imageView.post( new Runnable() {
                        @Override
                        public void run() {
                            imageView.setRotation( angle );
                        }
                    } );

                    try {
                        Thread.sleep( 25 );
                    } catch ( InterruptedException e ) {
                        e.printStackTrace();
                    }
                }
            }
        } );
        thread.start();
    }

    public void pause(){
        rotation = false;
        angle = 0;
        imageView.post( new Runnable() {
            @Override
            public void run() {
                imageView.setRotation( angle );
            }
        } );
    }
}
