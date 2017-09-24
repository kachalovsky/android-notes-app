package fit.bstu.gsusapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import fit.bstu.gsusapp.models.NoteModel;

/**
 * Created by andre on 23.09.2017.
 */

interface NoteRecyclerDelegate {
    void onDeletePressed(String id);
    void onEditPressed(String id, String oldNoteText);
    void onChecked(String id, boolean isChecked);
}

class NotesAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    ArrayList<NoteModel> list = new ArrayList<NoteModel>();
    public NoteRecyclerDelegate delegate;

    private int findNoteIndexById (String id) {
        for(NoteModel item : list) {
            if (item.id == id) return list.indexOf(item);
        }
        return -1;
    }

    public void fetchNotes(Context context, Date date) {
        int count = list.size();
        list.clear();
        notifyItemRangeRemoved(0, count);
        list.addAll(TaskManager.getTasksForDate(context, date));
        notifyItemRangeInserted(0, list.size());
    }

    public void insertNote(Context context, NoteModel note) {
        int pos = getItemCount();
        list.add(note);
        notifyItemInserted(pos);
    }

    public void deleteNoteById(String id) {
        int index = findNoteIndexById(id);
        list.remove(index);
        notifyItemRemoved(index);
    }

    public void changeNoteById(String id, String withText) {
        int index = findNoteIndexById(id);
        NoteModel note = list.get(index);
        note.note = withText;
        notifyItemChanged(index);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.node_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.bind(list.get(position), delegate);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
