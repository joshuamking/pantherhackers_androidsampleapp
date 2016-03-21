package com.antechdevelopment.todoapp.enums;

import android.support.annotation.AnyRes;
import com.antechdevelopment.todoapp.R;

/**
 Created by joshuaking on 3/17/16.
 */
public enum Priority {
	Low(R.color.transparent), Normal(R.drawable.ic_warning), High(R.drawable.ic_error);
	private int iconResource;

	Priority (@AnyRes int iconResource) {
		this.iconResource = iconResource;
	}

	public int getIconResource () {
		return iconResource;
	}
}
