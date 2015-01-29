package io.github.stickerkadai;

import org.json.JSONArray;

public final class Constants {

	public static String[] IMAGES;

	private Constants() {
	}

    public static void getConstants(){
        try {
            JSONArray StikersArray = (JSONArray) new JSONObjectRetriever().execute("http://stickerkadai.github.io/stickers.json").get();
            int noOfStickers = StikersArray.length();
            IMAGES = new String[noOfStickers];
            for (int i = 0; i < noOfStickers; i++)
                IMAGES[i] = "http://stickerkadai.github.io/Stickers/" + StikersArray.getJSONObject(i).getString("path");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

}
