package fit.bstu.gsusapp;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import fit.bstu.gsusapp.models.NoteModel;

import static android.R.id.list;

/**
 * Created by andre on 24.09.2017.
 */

class TaskManager {
    public static List<NoteModel> getTasksForDate(Context context, Date date) {
        String yourFilePath = getFilePath( date);
        try {
            FileInputStream fis = context.openFileInput(yourFilePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return Arrays.asList(new Gson().fromJson(sb.toString(), NoteModel[].class));
        }
        catch (Exception e) {
            return new ArrayList<NoteModel>();
        }

    }

    public static void insertNote(Context context, Date date, NoteModel note) {
        ArrayList<NoteModel> notes = new ArrayList<NoteModel>(TaskManager.getTasksForDate(context, date));
        notes.add(note);
        TaskManager.writeTasksForDate(context, date, notes);
    }

    public static void writeTasksForDate(Context context, Date date, Collection<NoteModel> data) {
        String yourFilePath = getFilePath(date);
        String json = new Gson().toJson(data);
        try {
            FileOutputStream fileout = context.openFileOutput(yourFilePath, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(json);
            outputWriter.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getFilePath (Date date) {
        return  date.toString() + ".txt";
    }

    public static void deleteNoteById(Context context, Date selectedDate, String id) {
        List<NoteModel> tasks = new ArrayList<NoteModel>(getTasksForDate(context, selectedDate));
        int deletedIndex = findNoteIndexById(tasks, id);
        tasks.remove(deletedIndex);
        writeTasksForDate(context, selectedDate, tasks);
    }

    public static void changeNoteById(Context context, Date selectedDate, String id, String noteText) {
        ArrayList<NoteModel> tasks = new ArrayList<NoteModel>(getTasksForDate(context, selectedDate));
        int noteIndex = findNoteIndexById(tasks, id);
        NoteModel note = tasks.get(noteIndex);
        note.note = noteText;
        writeTasksForDate(context, selectedDate, tasks);
    }

    public static void setCheckedNoteById(Context context, Date selectedDate, String id, boolean isChecked) {
        ArrayList<NoteModel> tasks = new ArrayList<NoteModel>(getTasksForDate(context, selectedDate));
        int noteIndex = findNoteIndexById(tasks, id);
        if (noteIndex != -1) {
            NoteModel note = tasks.get(noteIndex);
            note.isDone = isChecked;
            writeTasksForDate(context, selectedDate, tasks);
        }
    }


    private static int findNoteIndexById (List<NoteModel> list, String id) {
        for(NoteModel item : list) {
            if (item.id.equals(id)) return list.indexOf(item);
        }
        return -1;
    }
}
