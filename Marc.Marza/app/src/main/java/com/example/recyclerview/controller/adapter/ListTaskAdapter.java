package com.example.recyclerview.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.model.Task;

import java.util.List;

public class ListTaskAdapter extends RecyclerView.Adapter<ListTaskAdapter.ViewHolder> {
    private List<Task> tasks;
    private LayoutInflater inflater;
    private Context context;

    private Activity myActivity;
    private int typeTask;
    final ListTaskAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public ListTaskAdapter(int typeTask, List<Task> tasksList, Context context,Activity myActivity ,ListTaskAdapter.OnItemClickListener listener1) {
        this.tasks = tasksList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener1;
        this.typeTask = typeTask;
        this.myActivity = myActivity;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public ListTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_tasks, null);
        return new ListTaskAdapter.ViewHolder(view,myActivity);
    }

    @Override
    public void onBindViewHolder(ListTaskAdapter.ViewHolder holder, int position) {
        holder.bindData(tasks.get(position));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHourTask, txtNameTask, txtDateTask;

        ViewHolder(View itemView,Activity myActivity) {
            super(itemView);

            txtHourTask = itemView.findViewById(R.id.txtViewHourTask);
            txtNameTask = itemView.findViewById(R.id.txtNameTask);
            txtDateTask = itemView.findViewById(R.id.txtViewDateTask);
        }

        void bindData(final Task task) {
            txtDateTask.setText(task.getDate());
            txtHourTask.setText(task.getTime());
            txtNameTask.setText(task.getNameTask());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(task);
                }

            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_cardview));
                    View popupView = LayoutInflater.from(context).inflate(R.layout.popup_delete_task, null);

                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    popupWindow.showAtLocation(itemView, Gravity.BOTTOM, 0, 0);
                    popupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = myActivity.getIntent();
                            intent.putExtra("idTask",task.getId());
                            myActivity.setResult(Activity.RESULT_OK,intent);
                            myActivity.finish();
                            popupWindow.dismiss();
                        }
                    });
                    return true;
                }
            });

        }

    }
}
