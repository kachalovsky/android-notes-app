package fit.bstu.gsusapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import fit.bstu.gsusapp.models.NoteModel;

/**
 * Created by andre on 23.09.2017.
 */

class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final CheckBox checkbox;
    private final Button deleteButton;
    private final ImageButton editButton;


    public RecyclerViewHolder(View itemView) {
        super(itemView);
        checkbox = (CheckBox) itemView.findViewById(R.id.noteCheckbox);
        deleteButton = (Button) itemView.findViewById(R.id.delete_btn);
        editButton = (ImageButton) itemView.findViewById(R.id.edit_btn);
    }

    public void bind(final NoteModel note, final NoteRecyclerDelegate delegate) {
        checkbox.setChecked(note.isDone);
        checkbox.setText(note.note);
        if (delegate == null) return;
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                delegate.onChecked(note.id, isChecked);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onDeletePressed(note.id);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onEditPressed(note.id, (String)checkbox.getText());
            }
        });
    }
}
