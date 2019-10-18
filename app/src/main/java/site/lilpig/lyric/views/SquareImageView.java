package site.lilpig.lyric.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int hmsC = MeasureSpec.getSize(heightMeasureSpec)>MeasureSpec.getSize(widthMeasureSpec) ? heightMeasureSpec: widthMeasureSpec;
        super.onMeasure(widthMeasureSpec, hmsC);
    }
}
