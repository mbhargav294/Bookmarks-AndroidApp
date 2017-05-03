package com.madhu_bookmarks_manager.bookmarksmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mbhar on 4/29/2017 at 9:47 PM.
 * List adapter for displaying the list view in homepage
 */

public class BasicLinkAdapter extends ArrayAdapter<BasicLinkInfo>{
    Singleton singleton = Singleton.getInstance();
    private ListView mListView;

    BasicLinkAdapter(Activity context, int resource, ArrayList<BasicLinkInfo> basicLinkInfo) {
        super(context, resource, basicLinkInfo);
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView;

        mListView = (ListView) parent;
        if(convertView == null) {
            listItemView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.link_card_large, parent, false);
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
        TextView title = (TextView) listItemView.findViewById(R.id.link_title);
        title.setText(text);


        //setting the background of this card
        ImageView linkImage = (ImageView) listItemView.findViewById(R.id.link_image);
        TextView linkLetter = (TextView) listItemView.findViewById(R.id.link_letter);
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

        CircleImageView logoImage = (CircleImageView) listItemView.findViewById(R.id.linkLogo);
        Glide.with(listItemView.getContext()).load(basicLinkInfo.getmLogo()).asBitmap().into(logoImage);

        ImageView favView = (ImageView) listItemView.findViewById(R.id.favicon);
        favView.setOnClickListener(vdClickListener);

        ImageView mRemoveBut = (ImageView) listItemView.findViewById(R.id.remove_button);
        mRemoveBut.setOnClickListener(removeButtonListener);


        if(basicLinkInfo.isFavourite()) {
            favView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_fav));
        }
        else {
            favView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_not_fav));
        }

        listItemView.setHasTransientState(false);

        return listItemView;
    }

    private View.OnClickListener vdClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int x = mListView.getPositionForView((View) v.getParent());
            v.setHasTransientState(true);
            BasicLinkInfo obj = singleton.getmAllLinks().get(x);
            int vId = v.getId();

            if(vId == R.id.favicon) {
                if (obj.isFavourite()) {
                    singleton.modifyFavouritesToItem(x, false, getContext());
                } else {
                    singleton.modifyFavouritesToItem(x, true, getContext());
                }
            }
            v.setHasTransientState(false);
        }
    };


    private View.OnClickListener removeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int x = mListView.getPositionForView((View) v.getParent());
            BasicLinkInfo obj = singleton.getmAllLinks().get(x);
            ImageView ico = (ImageView) v;
            int vId = v.getId();

            if(vId == R.id.remove_button) {
                AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete: " + obj.getmTitle());
                final int positionToRemove = x;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        singleton.removeFromAllLinks(positionToRemove);
                        //notifyDataSetChanged();
                    }});
                adb.show();
            }
        }
    };


}
