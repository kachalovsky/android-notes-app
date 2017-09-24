package fit.bstu.gsusapp.models;

import java.util.UUID;

/**
 * Created by andre on 24.09.2017.
 */

public class NoteModel {
    public NoteModel(String note, boolean isDone) {
        this.note = note;
        this.isDone = isDone;
        id = UUID.randomUUID().toString();
    }

    public String note;
    public boolean isDone = false;
    public String id;
}
