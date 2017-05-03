package com.madhu_bookmarks_manager.bookmarksmanager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by mbhar on 5/2/2017 at 8:21 PM.
 */

public class SubtaskAdapter extends ArrayAdapter<BasicLinkInfo> {
    SubtaskAdapter(Activity context, int resource, ArrayList<BasicLinkInfo> basicLinkInfo) {
        super(context, resource, basicLinkInfo);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView;

        GridView mListView = (GridView) parent;
        if(convertView == null) {
            listItemView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.link_card_small, parent, false);
        } else {
            listItemView = convertView;
        }

        listItemView.setHasTransientState(true);

        Animation fadeOut = new AlphaAnimation(0, 1);  // the 1, 0 here notifies that we want the opacity to go from opaque (1) to transparent (0)
        fadeOut.setInterpolator(new AccelerateInterpolator());
        //fadeOut.setStartOffset(500); // Start fading out after 500 milli seconds
        fadeOut.setDuration(500); // Fadeout duration should be 1000 milli seconds

        listItemView.setAnimation(fadeOut);
        BasicLinkInfo basicLinkInfo = getItem(position);

        String text = basicLinkInfo.getmTitle();

        //Setting title of this card
        TextView title = (TextView) listItemView.findViewById(R.id.link_title_small);
        title.setText(text);


        //setting the background of this card
        ImageView linkImage = (ImageView) listItemView.findViewById(R.id.link_image_small);
        TextView linkLetter = (TextView) listItemView.findViewById(R.id.link_letter_small);
        if(basicLinkInfo.getmImage() != null && basicLinkInfo.getmImage().length() > 1){
            linkLetter.setVisibility(View.INVISIBLE);
            //linkImage.setVisibility(View.VISIBLE);
            Glide.with(listItemView.getContext()).load(basicLinkInfo.getmImage()).asBitmap().into(linkImage);
        }
        else{
            linkImage.setImageDrawable(null);
            linkLetter.setVisibility(View.VISIBLE);
            linkLetter.setText(""+Character.toUpperCase(text.charAt(0)));
            int color = basicLinkInfo.getmColor();
            linkImage.setBackgroundColor(Constants.COLORS[color]);
        }

        listItemView.setHasTransientState(false);

        return listItemView;
    }
}