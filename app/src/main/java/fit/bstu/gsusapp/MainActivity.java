package fit.bstu.gsusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fit.bstu.gsusapp.models.NoteModel;

public class MainActivity extends AppCompatActivity implements NoteRecyclerDelegate {

    final Context context = this;
    private RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private NotesAdapter notesAdapter;
    private Date selectedDate;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCalendarViewListener();
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        layoutManager = new LinearLayoutManager(this);
        notesAdapter = new NotesAdapter();
        notesAdapter.delegate = this;
        recycleView.setAdapter(notesAdapter);
        recycleView.setLayoutManager(layoutManager);
        notesAdapter.fetchNotes(context, selectedDate);

    }

    private void addCalendarViewListener() {
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = createDate(year, month, dayOfMonth);
                notesAdapter.fetchNotes(context, selectedDate);
                updateSelectedDateView();
            }
        });
        selectedDate = createDate(new Date());
        updateSelectedDateView();
    }

    private void updateSelectedDateView() {
        TextView tw = (TextView) findViewById(R.id.textView);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
        tw.setText(df.format(selectedDate));
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.AM_PM);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    private Date createDate(Date from) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTime();
    }

    public void onAddClick(View view) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String note = userInput.getText().toString();
                                Toast.makeText(context, "Note was created: " +note, Toast.LENGTH_LONG).show();
                                NoteModel newNote = new NoteModel(note, false);
                                TaskManager.insertNote(context, selectedDate, newNote);
                                notesAdapter.insertNote(context, newNote);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void callAlertDialog (final String id, String oldValue) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setText(oldValue);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int dialogId) {
                                String note = userInput.getText().toString();
                                Toast.makeText(context, "Note was edit: " + note, Toast.LENGTH_LONG).show();
                                TaskManager.changeNoteById(context, selectedDate, id, note);
                                notesAdapter.changeNoteById(id, note);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    public void onDeletePressed(String id) {
        TaskManager.deleteNoteById(context, selectedDate, id);
        notesAdapter.deleteNoteById(id);
        Toast.makeText(context, "Note was deleted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEditPressed(String id, String oldNoteText) {
        callAlertDialog(id, oldNoteText);
    }

    @Override
    public void onChecked(String id, boolean isChecked) {
        TaskManager.setCheckedNoteById(context, selectedDate, id, isChecked);
    }
}
