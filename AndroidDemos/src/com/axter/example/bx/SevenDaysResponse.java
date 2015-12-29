package com.axter.example.bx;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SevenDaysResponse {
	private int valid_distances;
	private int day_of_week;
	private long time;
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getValid_distances() {
		return valid_distances;
	}

	public void setValid_distances(int valid_distances) {
		this.valid_distances = valid_distances;
	}

	public int getDay_of_week() {
		return day_of_week;
	}

	public void setDay_of_week(int day_of_week) {
		this.day_of_week = day_of_week;
	}

	public static ArrayList<SevenDaysResponse> parse(String data) {
		ArrayList<SevenDaysResponse> list = new ArrayList<SevenDaysResponse>();

		try {
			JSONObject jsonObject = new JSONObject(data);

			if (!jsonObject.isNull("list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					SevenDaysResponse seven = new SevenDaysResponse();
//					if (!jsonObject2.isNull("run_mileages")) {
//						seven.setRun_mileages(jsonObject2.getInt("run_mileages"));
//					}
					if (!jsonObject2.isNull("valid_distances")) {
						seven.setValid_distances(jsonObject2.getInt("valid_distances"));
					}
					if (!jsonObject2.isNull("day_of_week")) {
						seven.setDay_of_week(jsonObject2.getInt("day_of_week"));
					}
					if (!jsonObject2.isNull("time")) {
						seven.setTime(jsonObject2.getInt("time")*1000l);
					}
					list.add(seven);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
}
