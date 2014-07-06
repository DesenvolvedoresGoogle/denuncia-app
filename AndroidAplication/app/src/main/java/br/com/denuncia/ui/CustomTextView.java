package br.com.denuncia.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import br.com.denuncia.R;

/**
 * Created by User on 05/07/2014.
 */
public class CustomTextView extends TextView {

    private final static int ROBOTO = 0;
    private final static int ROBOTO_BOLD = ROBOTO + 1;
    private final static int ROBOTO_ITALIC = ROBOTO_BOLD + 1;
    private final static int ROBOTO_LIGHT = ROBOTO_ITALIC + 1;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            parseAttributes(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            parseAttributes(context, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        //The value 0 is a default, but shouldn't ever be used since the attr is an enum
        int typeface = values.getInt(R.styleable.CustomTextView_typeface, 0);

        switch (typeface) {
            case ROBOTO:
            default:
                Typeface roboto = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
                setTypeface(roboto);
                break;
            case ROBOTO_BOLD:
                Typeface robotoBold = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
                setTypeface(robotoBold);
                break;
            case ROBOTO_ITALIC:
                Typeface robotoItalic = Typeface.createFromAsset(context.getAssets(), "Roboto-Italic.ttf");
                setTypeface(robotoItalic);
                break;
            case ROBOTO_LIGHT:
                Typeface robotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
                setTypeface(robotoLight);
                break;
        }

        values.recycle();
    }
}
