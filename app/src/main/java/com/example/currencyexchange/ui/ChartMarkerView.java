package com.example.currencyexchange.ui;

import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.example.currencyexchange.R;
import java.util.List;

public class ChartMarkerView extends MarkerView {
    private final TextView tvContent;
    private final List<String> dates;

    public ChartMarkerView(Context ctx, int layoutRes, List<String> dates) {
        super(ctx, layoutRes);
        this.dates = dates;
        tvContent = findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int idx = (int) e.getX();
        String date = dates.get(idx);
        tvContent.setText(date + ": " + String.format("%.4f", e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
