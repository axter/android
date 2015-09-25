package com.axter.tools.greenDAO;

import java.io.File;
import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * 使用greenDAO工具创建数据库表,等基础类
 * freemarker.jar
 * greendao-generator-2.0.0.jar
 * @author zhaobo
 *
 */
public class CreateTable {
	public static void main(String[] args) throws IOException, Exception {
		String dirPath = "src-dao";
		Schema schema = new Schema(4, "de.greenrobot.dao.table");
		addNote(schema);
		addUser(schema);
		addPP(schema);
		File dirFile = new File(dirPath);
		if(!dirFile.exists())
			dirFile.mkdirs();
		new DaoGenerator().generateAll(schema, dirPath);
	}

	private static void addNote(Schema schema) {
		Entity note = schema.addEntity("Note");
		note.addIdProperty();
		note.addStringProperty("text").notNull();
		note.addStringProperty("comment");
		note.addDateProperty("date");
	}

	private static void addUser(Schema schema) {
		Entity note = schema.addEntity("User");
		note.addIdProperty();
		note.addStringProperty("text").notNull();
		note.addStringProperty("psw");
		note.addDateProperty("date");
	}

	private static void addPP(Schema schema) {
		Entity note = schema.addEntity("PP");
		note.addIdProperty();
		note.addStringProperty("text").notNull();
		note.addStringProperty("psw");
		note.addDateProperty("date");
	}
}
