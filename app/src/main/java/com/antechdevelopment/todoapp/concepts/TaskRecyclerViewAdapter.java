package com.antechdevelopment.todoapp.concepts;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.antechdevelopment.todoapp.R;
import com.antechdevelopment.todoapp.databse.Task;

import java.util.List;

/**
 Created by joshuaking on 3/17/16.
 */
public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {
	private final List<Task> tasks;
	private       int        animatedIndex;

	public TaskRecyclerViewAdapter (List<Task> tasks) {
		this.tasks = tasks;
	}

	public class TaskViewHolder extends RecyclerView.ViewHolder {
		private final CardView  cardView;
		private final Context   context;
		private final TextView  detailsTextView;
		private final String    dueDateFormat;
		private final String    dueDateMessage;
		private final TextView  dueDateTextView;
		private final Button    markAsDoneButton;
		private final ImageView priorityImageView;
		private final TextView  titleTextView;

		public TaskViewHolder (final CardView cardView) {
			super(cardView);
			this.cardView = cardView;
			context = cardView.getContext();
			dueDateFormat = context.getString(R.string.due_date_format);
			dueDateMessage = context.getString(R.string.due_date_message);

			titleTextView = (TextView) cardView.findViewById(R.id.task_title);
			detailsTextView = (TextView) cardView.findViewById(R.id.task_details);
			dueDateTextView = (TextView) cardView.findViewById(R.id.task_due);
			markAsDoneButton = (Button) cardView.findViewById(R.id.task_button);
			priorityImageView = (ImageView) cardView.findViewById(R.id.task_priority);

			cardView.setCardElevation(5);
			markAsDoneButton.setText(R.string.done);
		}

		public void setTask (final Task task, int position) {
			titleTextView.setText(task.getTitle());
			detailsTextView.setText(task.getDetails());
			dueDateTextView.setText(String.format(dueDateMessage, task.getDueDate().toString(dueDateFormat)));
			priorityImageView.setImageResource(task.getPriority().getIconResource());

			if (task.isCompleted()) {
				markAsDoneButton.setVisibility(View.GONE);
				dueDateTextView.setText(R.string.completed);
			}
			else {
				markAsDoneButton.setAlpha(1);
				markAsDoneButton.setVisibility(View.VISIBLE);
			}

			if (position >= animatedIndex) {
				cardView.setRotation(10);
				cardView.setAlpha(0);
				cardView.setTranslationY(100);
				cardView.animate().setDuration(500).setStartDelay(100).translationY(0).rotation(0).alpha(1).start();
				animatedIndex++;
			}

			markAsDoneButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick (final View doneButton) {
					doneButton.animate().alpha(0).setDuration(500).start();
					task.setCompleted(true);
					dueDateTextView.setText(R.string.completed);
					Snackbar.make(doneButton, "Marked as completed", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
						@Override
						public void onClick (View v) {
							task.setCompleted(false);
							doneButton.animate().alpha(1).setDuration(500).start();
							dueDateTextView.setText(String.format(dueDateMessage, task.getDueDate().toString(dueDateFormat)));
						}
					}).show();
				}
			});
		}
	}

	@Override
	public TaskViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		return new TaskViewHolder((CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false));
	}

	@Override
	public void onBindViewHolder (TaskViewHolder holder, int position) {
		holder.setTask(tasks.get(position), position);
	}

	@Override
	public int getItemCount () {
		return tasks.size();
	}
}