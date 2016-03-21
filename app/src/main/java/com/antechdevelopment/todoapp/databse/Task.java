package com.antechdevelopment.todoapp.databse;

import android.os.Parcel;
import android.os.Parcelable;
import com.antechdevelopment.todoapp.enums.Priority;
import org.joda.time.LocalDate;

/**
 Created by joshuaking on 3/17/16.
 */
public class Task implements Parcelable {
	public static final Creator<Task> CREATOR = new Creator<Task>() {
		@Override
		public Task createFromParcel (Parcel in) {
			return new Task(in);
		}

		@Override
		public Task[] newArray (int size) {
			return new Task[size];
		}
	};
	private boolean   completed;
	private String    details;
	private LocalDate dueDate;
	private Priority  priority;
	private String    title;

	public Task (Parcel in) {
		title = in.readString();
		details = in.readString();
		dueDate = (LocalDate) in.readSerializable();
		priority = Priority.valueOf(in.readString());
		completed = in.readByte() != 0;
	}

	public Task (String title, String details, LocalDate dueDate, Priority priority, boolean completed) {
		this.title = title;
		this.details = details;
		this.dueDate = dueDate;
		this.priority = priority;
		this.completed = completed;
	}

	@Override
	public int describeContents () {
		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(details);
		dest.writeSerializable(dueDate);
		dest.writeString(priority.name());
		dest.writeByte((byte) (completed ? 1 : 0));
	}

	public String getDetails () {
		return details;
	}

	public LocalDate getDueDate () {
		return dueDate;
	}

	public Priority getPriority () {
		return priority;
	}

	public String getTitle () {
		return title;
	}

	public boolean isCompleted () {
		return completed;
	}

	public boolean isIncomplete () {
		return !isCompleted();
	}

	public void setCompleted (boolean completed) {
		this.completed = completed;
	}

	public void setDetails (String details) {
		this.details = details;
	}

	public void setDueDate (LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public void setPriority (Priority priority) {
		this.priority = priority;
	}

	public void setTitle (String title) {
		this.title = title;
	}
}
