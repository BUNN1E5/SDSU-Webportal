package com.twobitdata.sdsuportalunffl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private LayoutInflater inflater;
    List<GradeItem> data = Collections.emptyList();

    public GradeAdapter(Context context, List<GradeItem> data){

        for (int i = 0; i < data.size(); i++){
            System.out.println(data.get(i).toString());
        }

        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void setData( List<GradeItem> data){
        this.data = data;
    }
    
    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grade_item, parent, false);
        GradeViewHolder holder = new GradeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GradeViewHolder holder, int position) {
        GradeItem item = data.get(position);
        holder.courseName.setText(item.courseName);
        holder.teacherName.setText(item.teacherName);
        holder.grade.setText(item.grade);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    
    class GradeViewHolder extends RecyclerView.ViewHolder{

        TextView courseName, teacherName, grade;


        public GradeViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.course_name);
            teacherName = (TextView) itemView.findViewById(R.id.teacher_name);
            grade = (TextView) itemView.findViewById(R.id.grade);

        }
    }
}
