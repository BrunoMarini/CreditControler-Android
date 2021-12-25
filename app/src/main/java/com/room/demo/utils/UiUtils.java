package com.room.demo.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UiUtils {
    public static TextView createDefaultTextView(Context context, int alignment, String message) {
        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setTextSize(20);

        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(alignment);

        textView.setLayoutParams(layoutParams);
        return textView;
    }

    public static RelativeLayout createRelativeLayout(Context context) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setPadding(10, 10, 10, 10);

        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        relativeLayout.setLayoutParams(layoutParams);
        return relativeLayout;
    }

    public static TextView createTextView(Context context, String message) {
        TextView textView = new TextView(context);
        textView.setText(message);
        return textView;
    }

    public static LinearLayout createLinearLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 10, 5, 0);
        linearLayout.setLayoutParams(lp);
        return linearLayout;
    }
}
