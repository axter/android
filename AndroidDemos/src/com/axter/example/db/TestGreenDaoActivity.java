package com.axter.example.db;

import java.util.Date;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.axter.example.R;

import de.greenrobot.daoexample.DaoMaster;
import de.greenrobot.daoexample.DaoMaster.DevOpenHelper;
import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.Note;
import de.greenrobot.daoexample.NoteDao;
import de.greenrobot.daoexample.User;
import de.greenrobot.daoexample.UserDao;

public class TestGreenDaoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
	}

	public void aa(View view) {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
		SQLiteDatabase db = helper.getWritableDatabase();

		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		NoteDao noteDao = daoSession.getNoteDao();
		Note note = new Note(null, "AA", "cccsdf", new Date());
		System.out.println(noteDao.insert(note));
		Note note2 = new Note(null, "AA", "cccsdf", new Date());
		System.out.println(noteDao.insert(note2));
		
		UserDao userDao = daoSession.getUserDao();
		userDao.insert(new User(null, "BB", "CC", new Date()));
	}
}
