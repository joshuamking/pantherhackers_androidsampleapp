package com.antechdevelopment.todoapp.screens;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.*;
import com.antechdevelopment.todoapp.R;
import com.antechdevelopment.todoapp.concepts.StupidDevException;
import com.antechdevelopment.todoapp.concepts.TaskRecyclerViewAdapter;
import com.antechdevelopment.todoapp.databse.Task;
import com.antechdevelopment.todoapp.enums.Priority;
import org.joda.time.LocalDate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Parcelable {
	public static final Creator<MainActivity> CREATOR = new Creator<MainActivity>() {
		@Override
		public MainActivity createFromParcel (Parcel in) {
			return new MainActivity(in);
		}

		@Override
		public MainActivity[] newArray (int size) {
			return new MainActivity[size];
		}
	};
	public  SwipeRefreshLayout swipeRefresh;
	private AppBarLayout       appBarLayout;
	private TasksPagerAdapter  mTasksPagerAdapter;
	private ViewPager          mViewPager;
	private TabLayout          tabLayout;
	private ArrayList<Task>    tasks;

	public MainActivity () {
	}

	protected MainActivity (Parcel in) {
		tasks = in.createTypedArrayList(Task.CREATOR);
	}

	public static class TasksFragment extends Fragment {
		private static final String ARG_IS_COMPLETED_FRAGMENT = "is_completed_fragment";
		private static final String ARG_TASKS                 = "tasks";

		public TasksFragment () {
		}

		public static TasksFragment newInstance (ArrayList<Task> allTasks, boolean isCompletedFragment) {
			TasksFragment fragment = new TasksFragment();
			Bundle        args     = new Bundle();
			args.putParcelableArrayList(ARG_TASKS, allTasks);
			args.putBoolean(ARG_IS_COMPLETED_FRAGMENT, isCompletedFragment);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			RecyclerView               recyclerView        = (RecyclerView) inflater.inflate(R.layout.fragment_tasks, container, false);
			ArrayList<Task>            allTasks            = getArguments().getParcelableArrayList(ARG_TASKS);
			boolean                    isCompletedFragment = getArguments().getBoolean(ARG_IS_COMPLETED_FRAGMENT);
			int                        numberOfColumns     = getResources().getInteger(R.integer.number_of_columns_for_root_log);
			StaggeredGridLayoutManager layoutManager       = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);

			assert allTasks != null;
			ArrayList<Task> tasksToKeep = new ArrayList<>();
			for (Task task : allTasks) {
				if (task.isCompleted() == isCompletedFragment) { tasksToKeep.add(task); }
			}

			layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
			recyclerView.setLayoutManager(layoutManager);
			recyclerView.setAdapter(new TaskRecyclerViewAdapter(tasksToKeep));
			return recyclerView;
		}
	}

	public class TasksPagerAdapter extends FragmentPagerAdapter {
		private final ArrayList<Task> tasks;

		public TasksPagerAdapter (FragmentManager fm, ArrayList<Task> tasks) {
			super(fm);
			this.tasks = tasks;
		}

		@Override
		public int getCount () {
			return 2;
		}

		@Override
		public CharSequence getPageTitle (int position) {
			switch (position) {
				case 0:
					return getString(R.string.done);
				case 1:
					return getString(R.string.incomplete);
			}
			return null;
		}

		@Override
		public Fragment getItem (int position) {
			switch (position) {
				case 0:
					return TasksFragment.newInstance(tasks, true);
				case 1:
					return TasksFragment.newInstance(tasks, false);
				default:
					throw new StupidDevException();
			}
		}
	}

	@Override
	public int describeContents () {
		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeTypedList(tasks);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mViewPager = (ViewPager) findViewById(R.id.container);
		swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
		tabLayout = (TabLayout) findViewById(R.id.tabs);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
		Toolbar              toolbar = (Toolbar) findViewById(R.id.toolbar);
		FloatingActionButton fab     = (FloatingActionButton) findViewById(R.id.fab);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();

		assert actionBar != null;
		actionBar.setIcon(R.drawable.ic_app_icon_no_background);
		actionBar.setDisplayShowTitleEnabled(false);

		populateTasks();
		swipeRefresh.setOnRefreshListener(this);
		swipeRefresh.setColorSchemeResources(R.color.colorAccent);
		swipeRefresh.setProgressViewOffset(true, 0, 100);
		updatePagerData();
		mViewPager.setCurrentItem(1, false);
		mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged (int state) {
				swipeRefresh.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
			}
		});
		appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged (AppBarLayout appBarLayout, int verticalOffset) {
				swipeRefresh.setEnabled(verticalOffset == 0);
			}
		});

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		Snackbar.make(mViewPager, String.format("Menu item \"%s\" was clicked", item.getTitle()), Snackbar.LENGTH_LONG).show();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh () {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run () {
				updatePagerData();
				swipeRefresh.setRefreshing(false);
			}
		}, 1000);
	}

	private void populateTasks () {
		if (tasks == null) { tasks = new ArrayList<>(); }

		String[] taskTitles     = getResources().getStringArray(R.array.tasks_titles);
		String[] taskDetails    = getResources().getStringArray(R.array.tasks_details);
		String[] taskDueDates   = getResources().getStringArray(R.array.tasks_due_dates);
		int[]    taskPriorities = getResources().getIntArray(R.array.tasks_priorities);

		tasks.clear();
		for (int i = 0; i < taskTitles.length; i++) {
			String[] splitDate = taskDueDates[i].split("\\.");
			int year = Integer.parseInt(splitDate[0]);
			int month = Integer.parseInt(splitDate[1]);
			int day = Integer.parseInt(splitDate[2]);

			String title = taskTitles[i];
			String details = taskDetails[i];
			LocalDate dueDate = new LocalDate(year, month, day);
			Priority priority = Priority.values()[taskPriorities[i]];

			tasks.add(new Task(title, details, dueDate, priority, false));
		}
	}

	private void updatePagerData () {
		int indexAtRefresh = mViewPager.getCurrentItem();
		mTasksPagerAdapter = new TasksPagerAdapter(getSupportFragmentManager(), tasks);
		mViewPager.setAdapter(mTasksPagerAdapter);
		tabLayout.setupWithViewPager(mViewPager);
		mViewPager.setCurrentItem(indexAtRefresh, false);
	}
}
