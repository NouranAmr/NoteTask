package iti.jets.mad.noteapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> allNotes = new ArrayList<>();
    private onItemClickListener clickListener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int position) {


            Note note = allNotes.get(position);
            noteHolder.titleTextView.setText(note.getTitle());
            noteHolder.contentTextView.setText(note.getContent());

    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    public Note getSpecificNote(int position) {
        return this.allNotes.get(position);
    }

    public void setAllNotes(List<Note> allNotes) {
        this.allNotes = allNotes;
        notifyDataSetChanged();
    }


    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, contentTextView;

        public NoteHolder(@NonNull final View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //itemView.getContext().startActivity(new Intent(itemView.get ));
                    clickListener.onItemClick(allNotes.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


}
