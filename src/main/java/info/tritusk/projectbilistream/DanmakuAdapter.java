package info.tritusk.projectbilistream;

import java.io.IOException;

import javax.annotation.Nullable;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DanmakuAdapter extends TypeAdapter<Danmaku> {

	@Override
	public void write(JsonWriter out, Danmaku value) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nullable
	public Danmaku read(JsonReader in) throws IOException {
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
			case ("cmd"): {
				if (!in.nextString().equals("DANMU_MSG")) { //Only support DANMU_MSG for now. TODO: support various type of msg
					in.endObject();
					return null;
				}
			}
			case ("info"): {
				in.beginArray(); // info begin
				in.beginArray(); // index 0 begin
				in.endArray(); // index 0 end
				String context = in.nextString(); //index 1
				in.beginArray(); // index 2 begin
				String sender = in.nextString();
				boolean isOp = in.nextInt() == 1;
				boolean isVIP = in.nextInt() == 1;
				String timeStamp = ""; //Not sure whether it is here or not
				in.endArray(); // index 2 end
				in.endArray(); // info end
				in.endObject(); // json end
				return new Danmaku() {
					
					private final long timeStamp = System.currentTimeMillis();

					@Override
					public long timeStamp() {
						return timeStamp;
					}

					@Override
					public String getAsPlainText() {
						return "[Bilibili] " + sender + ": " + context;
					}
					
				};
			}
			default: {
				in.endObject();
				return null;
			}
			}
		}
		in.endObject();
		return null;
	}

}
