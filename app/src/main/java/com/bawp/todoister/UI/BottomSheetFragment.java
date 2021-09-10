package com.bawp.todoister.UI;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bawp.todoister.R;
import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{

    private EditText enterTodo;
    private ImageButton priorityButton;
    private ImageButton calenderButton;
    private RadioButton selectedRadioButton;
    private RadioGroup priorityRadioGroup;
    private int selectedButtonId;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate;
    Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private boolean isEdit;
    private Priority priority;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
//        selectedRadioButton = view.findViewById(R.id.selected);
        calenderButton = view.findViewById(R.id.today_calendar_button);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        priorityButton = view.findViewById(R.id.priority_todo_button);

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sharedViewModel.getSelectedItem().getValue() != null) {
            isEdit = sharedViewModel.isEdit();
            if(isEdit) {
                Task task = sharedViewModel.getSelectedItem().getValue();
                enterTodo.setText(task.getTask());
                Log.d("shared", "onResume: " + task.getTask());
            }
            else {
                enterTodo.setText(null);
            }
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        calenderButton.setOnClickListener(v -> {
            calendarGroup.setVisibility(
                    calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
            Utils.hideKeyboard(v);
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.clear();
                calendar.set(year , month , dayOfMonth);
                dueDate = calendar.getTime();
            }
        });

        priorityButton.setOnClickListener(v21 -> {
//            Utils.hideKeyboard(v);
            if(priorityRadioGroup.getVisibility() == View.GONE)
                priorityRadioGroup.setVisibility(View.VISIBLE);
            else
                priorityRadioGroup.setVisibility(View.GONE);

            priorityRadioGroup.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {

                if(priorityRadioGroup.getVisibility() == View.VISIBLE) {
                    selectedButtonId  = checkedId;
                    selectedRadioButton = view.findViewById(selectedButtonId);

                    if(selectedRadioButton.getId() == R.id.radioButton_high) {
                        priority = Priority.HIGH;
                    }
                    else if(selectedRadioButton.getId() == R.id.radioButton_med) {
                        priority = Priority.MEDIUM;
                    }
                    else if(selectedRadioButton.getId() == R.id.radioButton_low) {
                        priority = Priority.LOW;
                    }
                }
                else
                    priority = Priority.HIGH;
            });

        });

        saveButton.setOnClickListener(v -> {
            String task = enterTodo.getText().toString().trim();
            if(!TextUtils.isEmpty(task) && dueDate != null && priority != null) {
                Task mytask = new Task(task, priority, dueDate,
                        Calendar.getInstance().getTime(), false);

                if(isEdit) {
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);
                    TaskViewModel.update(updateTask);
                    sharedViewModel.setEdit(false);
                }
                else
                    TaskViewModel.insert(mytask);

                enterTodo.setText(null);
                if(this.isVisible()) {
                    this.dismiss();
                }
            }
            else {
                Snackbar.make(saveButton , R.string.empty_field , Snackbar.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.today_chip) {
            calendar.add(Calendar.DAY_OF_YEAR , 0);
            dueDate = calendar.getTime();
        }
        else if(id == R.id.tomorrow_chip) {
            calendar.add(Calendar.DAY_OF_YEAR , 1);
            dueDate = calendar.getTime();
        }
        else if(id == R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR , 2);
            dueDate = calendar.getTime();
        }
    }
}