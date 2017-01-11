package com.it.juyou.utils;

import android.app.Activity;
import android.content.Context;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by lishuliang on 2016/12/19.
 */

public class PerssionUtils {

  private static  Activity mContext;
  public static  void requPerssion(Activity context,String[] pers){
      mContext = context;

  }

   public static interface CallBack{
       /**
        *
        * @param type 0 未获取 1 已获取
        */
      void   sucess(int type);
   };


}
