package com.vanke.xphotoview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxn on 2016/12/30.
 */

public class ChangeLine extends ViewGroup {
    public ChangeLine(Context context) {
        super(context);
    }

    public ChangeLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        int width=0;
        int height=0;
        int lineHeight=0;
        int lineWidth=0;
        int count=getChildCount();
        for(int i=0;i<count;i++)
        {
            View child=getChildAt(i);
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int childHeight=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
            if(lineWidth+childWidth>widthSize)
            {
                width=Math.max(lineWidth,childWidth);
                height+=lineHeight;
                lineWidth=childWidth;//开启新的一行
                lineHeight=childHeight;

            }else
            {
                lineWidth+=childWidth;
                lineHeight=Math.max(lineHeight,childHeight);//取高度最大的作为行高
            }
            if(i==count-1)
            {
                width=Math.max(lineWidth,childWidth);
                height+=lineHeight;
            }
        }
        setMeasuredDimension((widthMode==MeasureSpec.EXACTLY)?widthSize:width,
                heightMode==MeasureSpec.EXACTLY?heightSize:height);

    }
    private List<List<View>> viewList =new ArrayList<List<View>>();//
    private List<Integer> heightList =new ArrayList<Integer>();//每一行的高度
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        viewList.clear();
        heightList.clear();
        int width=getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>();
        int count =getChildCount();
        for(int j=0;j<count;j++)
        {
            View child=getChildAt(j);
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int childHeight=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
            if(lineWidth+childWidth>width)
            {
                heightList.add(lineHeight);
                lineWidth=0;
                lineHeight=0;
                viewList.add(lineViews);
                lineViews=new ArrayList<>();
            }
            lineWidth+=childWidth;
            lineHeight=Math.max(childHeight,lineHeight);
            lineViews.add(child);
        }
        heightList.add(lineHeight);
        viewList.add(lineViews);
        int left=0;
        int top=0;
        int num=viewList.size();
        for(int k=0;k<num;k++)
        {
            lineViews=viewList.get(k);
            lineHeight = heightList.get(k);
            int lineNum=lineViews.size();
            for(int t=0;t<lineNum;t++)
            {
                View child=lineViews.get(t);
                if(child.getVisibility()==View.GONE)
                    continue;
                MarginLayoutParams mlp= (MarginLayoutParams) child.getLayoutParams();
                int lc=left+mlp.leftMargin;
                int rc=lc+child.getMeasuredWidth();
                int tc=top+mlp.topMargin;
                int bc=tc+child.getMeasuredHeight();
                child.layout(lc,tc,rc,bc);
                left+=child.getMeasuredWidth()+mlp.leftMargin+mlp.rightMargin;
            }
            left = 0;
            top+=lineHeight;
        }

    }
}
