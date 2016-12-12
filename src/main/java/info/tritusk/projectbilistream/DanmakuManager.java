package info.tritusk.projectbilistream;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum DanmakuManager {
	
	INSTANCE;
	
	final Queue<Danmaku> danmakuQueue = new ConcurrentLinkedQueue<>();
	
	public boolean push(@Nonnull Danmaku danmaku) {
		return this.danmakuQueue.offer(danmaku);
	}
	
	@Nullable
	public Danmaku pop() {
		return this.danmakuQueue.poll();
	}
	
	public boolean hasNoNewDanmaku() {
		return danmakuQueue.isEmpty();
	}

}
