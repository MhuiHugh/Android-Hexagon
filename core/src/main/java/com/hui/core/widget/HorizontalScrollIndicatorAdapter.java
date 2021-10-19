package com.hui.core.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hui.core.R;

import java.util.List;

/**
 * HorizontalScrollIndicatorView
 * 结合使用，横向滚动颜色选择适配器
 */
public class HorizontalScrollIndicatorAdapter {

    private int currentIndex = -1;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Integer> mDatas;

    public HorizontalScrollIndicatorAdapter(Context context, List<Integer> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.item_horizontal_scrollindicator_view, parent, false);
            viewHolder.view = convertView
                    .findViewById(R.id.collage_item_color);
            viewHolder.indicator = convertView
                    .findViewById(R.id.collage_item_colorselect);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.view.setBackgroundColor(mDatas.get(position));
        viewHolder.indicator.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        if (position == currentIndex) {
            viewHolder.indicator.setBackgroundColor(mContext.getResources().getColor(R.color.color_333));
        }
        return convertView;
    }

    public void setCurrentIndex(int index) {
        currentIndex = index;
    }

    private class ViewHolder {
        View view, indicator;
    }

}
