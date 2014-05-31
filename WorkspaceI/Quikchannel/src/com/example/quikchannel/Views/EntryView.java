package com.example.quikchannel.Views;

import com.example.quikchannel.R;
import com.example.quikchannel.R.color;
import com.example.quikchannel.data.Entry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class EntryView extends RelativeLayout {

	private Entry entry;
	private Paint boxPaint;
	private Paint linePaint;
	private TextView titleView;
	private TextView contentView;
	
	public EntryView(Context context) {
		super(context);
		create();
	}
	
	public EntryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		create();
	}
	
	private void create() {
		titleView = new TextView(getContext());
		titleView.setId(420);
		contentView = new TextView(getContext());
		contentView.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis et orci dolor. Vestibulum quis massa velit. Suspendisse potenti. Sed vel tellus porttitor enim commodo placerat. Donec eget gravida odio. Integer urna nisi, tempor ac scelerisque in, vestibulum ac mi. Duis tincidunt, nunc sed porta fermentum, dolor eros feugiat eros, sit amet sagittis justo ligula ut sem. Integer id purus diam. Sed sollicitudin mauris nisl.");
		SpannableString title = new SpannableString("entry title created by Anonymous at 4/20/2014 4:20 PM with id 3256");
		title.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.title_color)), 0, 12, 0);
		title.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.author_color)), 23, 32, 0);
		title.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.time_color)), 36, 53, 0);
		title.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.id_color)), 62, 66, 0);
		titleView.setText(title, BufferType.SPANNABLE);
		titleView.setPadding(10, 10, 10, 10);
		titleView.setTextSize(16);
		titleView.setTextColor(getContext().getResources().getColor(R.color.text_color));
		this.addView(titleView);
		RelativeLayout.LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.BELOW, titleView.getId());
		contentView.setLayoutParams(p);
		contentView.setTextColor(getContext().getResources().getColor(R.color.text_color));
		contentView.setPadding(10, 10, 10, 10);
		this.addView(contentView);
		
		boxPaint = new Paint();
		boxPaint.setColor(getContext().getResources().getColor(R.color.box_color));
		boxPaint.setStyle(Style.STROKE);
		boxPaint.setStrokeWidth(10);
		
		linePaint = new Paint(boxPaint);
		linePaint.setStrokeWidth(5);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
		canvas.drawLine(this.getPaddingLeft(), titleView.getHeight(), canvas.getWidth(), titleView.getHeight(), linePaint);
		canvas.drawRect(this.getPaddingLeft(), 0, canvas.getWidth(), contentView.getHeight() + titleView.getHeight(), boxPaint);
	}
	
	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
		SpannableString title = new SpannableString(entry.getTitle() + " created by " + entry.getAuthor() + " at " + entry.getTime() + " with ID " + entry.getID());
		title.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.title_color)), 0, entry.getTitle().length(), 0);
		title.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.author_color)), entry.getTitle().length() + 12, entry.getTitle().length() + 12 + entry.getAuthor().length(), 0);
		title.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.time_color)), entry.getTitle().length() + 12 + entry.getAuthor().length() + 4, entry.getTitle().length() + 12 + entry.getAuthor().length() + 4 + entry.getTime().length(), 0);
		title.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.id_color)), entry.getTitle().length() + 12 + entry.getAuthor().length() + 4 + entry.getTime().length() + 9, title.length(), 0);
		titleView.setText(title, BufferType.SPANNABLE);
		
		SpannableString contentText = new SpannableString(entry.getText());
		String[] lines = entry.getText().split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith(">")) {
				int start = 0;
				int end = 0;
				
				for (int j = 0; j < i; j++) {
					start += lines[j].length();
				}
				for (int j = 0; j <= i; j++) {
					end += lines[j].length();
				}
				start += i;
				end += i;
				ForegroundColorSpan s = new ForegroundColorSpan(getContext().getResources().getColor(R.color.greentext));
				contentText.setSpan(s, start, end, 0);
			}
		}
		contentView.setText(contentText);
		this.invalidate();
	}

}
