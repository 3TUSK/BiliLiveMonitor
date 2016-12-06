package info.tritusk.projectbilistream;

import java.util.function.Supplier;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * A {@code Danmaku} object is an immutable representation of a single, deserailzed danmaku
 * retrieved from danmaku services, e.g. bilibili, niconico. It provides information 
 * about the danmaku sender, content, {@linkplain Danmaku#timeStamp() time stamp}, origin,
 * etc..
 * @author 3TUSK
 * @since 0.0.0.0
 */
@Immutable
public interface Danmaku extends Comparable<Danmaku>, Supplier<String> {
	
	/**
	 * This method return a time stamp for a <code>danmaku</code> instance
	 * for comparator. There is no convention of definition of "time stamp"
	 * for a <code>danmaku</code> instance; it can be the time of this
	 * Java object is created, or the time when danmaku is received, or even
	 * the time when it was sent by user using any type of interface.<br>
	 * No matter what type of time stamp was returned, it must promise that,
	 * a collection of danmakus can be sorted by time, i.e. the return value
	 * of this function.
	 * @return The time stamp of this Danmaku.
	 * @see java.lang.Comparable
	 */
	@Nonnegative
	long timeStamp();
	
	/**
	 * Return the danmaku object in a plain text form.
	 * @return The danmaku in plain text form.
	 */
	@Nonnull
	String getAsPlainText();
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Note</b>: The purpose of implementing {@link Supplier} interface on 
	 * a danmaku instance is still unclear.
	 * @return The danmaku object in a plain text form. 
	 * By default, it invokes {@linkplain Danmaku#getAsPlainText getAsPlainText} method.
	 */
	default String get() {
		return getAsPlainText();
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Note</b>: Default implementation of compareTo, in this case,
	 * is based on time stamp.
	 * @param another The Danmaku object to be compared.
	 * @return 1 if and only if the time stamp of {@code another} danmaku object is smaller than of this danmaku object; 0 for the same; -1 for otherwise.
	 */
	default int compareTo(@Nonnull Danmaku another) {
		return this.timeStamp() > another.timeStamp() ? 1 : this.timeStamp() == another.timeStamp() ? 0 : -1;
	}

}
