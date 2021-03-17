package com.example.android.sqliteweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sqliteweather.data.FilmsEntityRepository;

import java.util.List;

public class SavedFilmsAdapter extends RecyclerView.Adapter<SavedFilmsAdapter.FilmsViewHolder> {
    private List<FilmsEntityRepository> filmsList;
    private OnNavigationItemClickListener navClickListener;

    public SavedFilmsAdapter(OnNavigationItemClickListener navCL){
        navClickListener = navCL;
    }

    public interface OnNavigationItemClickListener{
        void onNavigationItemClicked(FilmsEntityRepository film);
    }

    public void updateSavedFilms(List<FilmsEntityRepository> films){
        this.filmsList = films;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.saved_film_item, parent, false);
        return new FilmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedFilmsAdapter.FilmsViewHolder holder, int position) {
        holder.bind(this.filmsList.get(position));
    }


    @Override
    public int getItemCount() {
        if(this.filmsList != null)
            return this.filmsList.size();
        else
            return 0;
    }


    class FilmsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView filmsTV;
        FilmsViewHolder(View view) {
            super(view);
            filmsTV = view.findViewById(R.id.films);
            view.setOnClickListener(this);
        }

        public void bind(FilmsEntityRepository film) {
            filmsTV.setText(film.film);
        }

        @Override
        public void onClick(View v) {
            FilmsEntityRepository locations = filmsList.get(getAdapterPosition());
            navClickListener.onNavigationItemClicked(locations);
        }
    }
}
