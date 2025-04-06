package com.example.mathgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.NumberViewHolder> {

    private List<Integer> numbers;

    public NumbersAdapter(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_number, parent, false);
        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.numberText.setText(String.valueOf(numbers.get(position)));
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public static class NumberViewHolder extends RecyclerView.ViewHolder {
        TextView numberText;

        public NumberViewHolder(View itemView) {
            super(itemView);
            numberText = itemView.findViewById(R.id.numberText);
        }
    }
}


