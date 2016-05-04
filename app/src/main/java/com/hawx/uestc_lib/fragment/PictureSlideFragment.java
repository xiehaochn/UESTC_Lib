package com.hawx.uestc_lib.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.utils.Utils;

/**
 * Created by Administrator on 2016/5/3.
 */
public class PictureSlideFragment extends Fragment {
    private int mIndex;
    public static PictureSlideFragment newInstance(int index){
        PictureSlideFragment pictureSlideFragment=new PictureSlideFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("index",index);
        pictureSlideFragment.setArguments(bundle);
        return pictureSlideFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndex=getArguments()!=null?getArguments().getInt("index"):0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pictureslide,container,false);
        ImageView imageView= (ImageView) view.findViewById(R.id.fragment_pictureslide_imageview);
        Bitmap bitmap;
        switch (mIndex){
            case 0:{
                bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.test_pic2);
                Bitmap scaledBitmap=Utils.scaleDown(bitmap,Utils.getWindowWidth(getContext()),false);
                imageView.setImageBitmap(scaledBitmap);
                break;
            }
            case 1:{
                bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.test_pic0);
                Bitmap scaledBitmap=Utils.scaleDown(bitmap,Utils.getWindowWidth(getContext()),false);
                imageView.setImageBitmap(scaledBitmap);
                break;
            }
            case 2:{
                bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.test_pic1);
                Bitmap scaledBitmap=Utils.scaleDown(bitmap,Utils.getWindowWidth(getContext()),false);
                imageView.setImageBitmap(scaledBitmap);
                break;
            }
            case 3:{
                bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.test_pic2);
                Bitmap scaledBitmap=Utils.scaleDown(bitmap,Utils.getWindowWidth(getContext()),false);
                imageView.setImageBitmap(scaledBitmap);
                break;
            }
            case 4:{
                bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.test_pic0);
                Bitmap scaledBitmap=Utils.scaleDown(bitmap,Utils.getWindowWidth(getContext()),false);
                imageView.setImageBitmap(scaledBitmap);
                break;
            }
            default:
                break;
        }

        return view;
    }
}
