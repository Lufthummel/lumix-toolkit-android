package org.secretwpn.lumixtoolkit.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.secretwpn.lumixtoolkit.model.CameraState;

/**
 * ImageView acting as a video display
 */
public class StreamDisplay extends ImageView {
    private final CameraState state = new CameraState();
    private final int[] batteryColors = new int[]{Color.parseColor("#CC0000"), Color.parseColor("#FFA500"), Color.parseColor("#00CC00")};
    private boolean drawFillImage = true;
    private Bitmap image;
    private Rect imageBounds;
    private Rect canvasBounds;
    private Paint batOutline;
    private Paint batFill;

    public StreamDisplay(Context context) {
        super(context);
        init();
    }

    public StreamDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StreamDisplay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CameraState getState() {
        return state;
    }

    private void init() {
        batOutline = new Paint();
        batOutline.setStyle(Paint.Style.FILL);
        batOutline.setColor(Color.WHITE);

        batFill = new Paint();
        batFill.setStyle(Paint.Style.FILL);
        batFill.setColor(Color.GREEN);

    }

    public void setImage(Bitmap img) {
        image = img;
        canvasBounds = new Rect(0, 0, getWidth(), getHeight());
        if (this.image != null)
            imageBounds = new Rect(0, 0, image.getWidth(), image.getHeight());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (image != null && imageBounds != null && canvasBounds != null) {
            if (drawFillImage)
                drawFillImage(canvas);
            else
                drawFitImage(canvas);
            drawBattery(canvas);
        }
    }

    private boolean isScreenLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    private void drawFillImage(Canvas canvas) {
        int min = Math.min(canvasBounds.width(), canvasBounds.height());
        int max = Math.max(canvasBounds.width(), canvasBounds.height());
        if (min <= 0 || max <= 0) return;

        float imageRatio = min / (float) max;
        int w = isScreenLandscape() ? imageBounds.width() : (int) (imageBounds.height() * imageRatio);
        int h = isScreenLandscape() ? (int) (imageBounds.width() * imageRatio) : imageBounds.height();
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(image, w, h);
        canvas.drawBitmap(thumbnail, new Rect(0, 0, thumbnail.getWidth(), thumbnail.getHeight()), canvasBounds, null);
    }

    private void drawFitImage(Canvas canvas) {
        int min = Math.min(imageBounds.width(), imageBounds.height());
        int max = Math.max(imageBounds.width(), imageBounds.height());
        if (min <= 0 || max <= 0) return;
        float hRatio = imageBounds.height() / (float) canvasBounds.height();
        float wRatio = imageBounds.width() / (float) canvasBounds.width();

        int w = isScreenLandscape() ? (int) (imageBounds.width() / hRatio) : canvasBounds.width();
        int h = isScreenLandscape() ? canvasBounds.height() : (int) (imageBounds.height() / wRatio);
        int x = isScreenLandscape() ? canvasBounds.width() / 2 - w / 2 : 0;
        int y = isScreenLandscape() ? 0 : canvasBounds.height() / 2 - h / 2;
        canvas.drawBitmap(image, imageBounds, new Rect(x, y, x + w, y + h), null);
    }

    private int getPercentColor(int percent) {
        if (percent <= 33)
            return batteryColors[0];
        else if (percent <= 66)
            return batteryColors[1];
        return batteryColors[2];
    }

    private void drawBattery(Canvas canvas) {
        int iconW = 100;
        int iconH = 40;
        int offsetX = 20, offsetY = 20;
        int fillOffset = 5;
        if (state != null) {
            int percent = state.getBattery();
            batFill.setColor(getPercentColor(percent));

            if (percent == 0)
                batOutline.setColor(Color.RED);
            canvas.drawRect(offsetX, offsetY, iconW + offsetX, offsetY + iconH, batOutline);
            canvas.drawRect(offsetX + iconW, offsetY + 10, offsetX + iconW + 10, offsetY + iconH - 10, batOutline);
            canvas.drawRect(offsetX + fillOffset, offsetY + fillOffset, offsetX + (iconW - fillOffset) * percent / 100.0F, offsetY + iconH - fillOffset, batFill);
        }
    }

    public void toggleZoomMode() {
        drawFillImage = !drawFillImage;
    }
}
