package com.digitalminds.dmssevent.interfaces;

import android.view.View;

/**
 * Created by Sandeep.Kumar on 02-02-2018.
 */

public interface AdaterClickCallBackForFavourite {
    public void itemClickFavourite(View view, int position, boolean favouriteStatus, int newsId,int likesCount);
}
