package com.goka.highlighttextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;

public class HighlightTextView extends EditText {

    private static final String TAG = HighlightTextView.class.getSimpleName();

    private static final int DEFAULT_CHARACTER_LIMIT = 200;
    private static final int DEFAULT_OVER_LIMIT_BACKGROUND_COLOR = Color.RED;

    private int characterLimit = DEFAULT_CHARACTER_LIMIT;
    private int overLimitBackgroundColor = DEFAULT_OVER_LIMIT_BACKGROUND_COLOR;


    public HighlightTextView(Context context) {
        this(context, null);
    }

    public HighlightTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightTextView, defStyleAttr, 0);
        this.characterLimit = a.getInt(R.styleable.HighlightTextView_characterLimit, DEFAULT_CHARACTER_LIMIT);
        this.overLimitBackgroundColor = a.getColor(R.styleable.HighlightTextView_overLimitBackgroundColor, DEFAULT_OVER_LIMIT_BACKGROUND_COLOR);
        a.recycle();

        watchHighlightText();
    }

    public void setHighlightText(CharSequence text) {
        if (text.length() < this.characterLimit) {
            return;
        }
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(text);
        spannable.setSpan(new BackgroundColorSpan(this.overLimitBackgroundColor), this.characterLimit, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setText(spannable);
        super.setSelection(spannable.length());
    }

    public void watchHighlightText() {
        addTextChangedListener(new Watcher(this));
    }

    private class Watcher implements TextWatcher {

        private final HighlightTextView highlightTextView;

        public Watcher(HighlightTextView highlightTextView) {
            this.highlightTextView = highlightTextView;
        }

        public void afterTextChanged(Editable s) {}

        public void beforeTextChanged(CharSequence s, int start,int count, int after) {}

        public void onTextChanged(CharSequence s, int start,
                              int before, int count) {
            this.highlightTextView.removeTextChangedListener(this);
            this.highlightTextView.setHighlightText(s);
            this.highlightTextView.addTextChangedListener(this);
        }
    }
}
