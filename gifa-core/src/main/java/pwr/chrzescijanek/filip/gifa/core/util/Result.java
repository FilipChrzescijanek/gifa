package pwr.chrzescijanek.filip.gifa.core.util;

import com.sun.javafx.UnmodifiableArrayList;

import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

/**
 * Represents images evaluation result.
 */
public final class Result {

	private final List<String> imageNames;
	private final Map<String, UnmodifiableArrayList<Double>> scores;

	/**
	 * Constructs new Result with given image names and scores.
	 *
	 * @param imageNames image names
	 * @param scores     associated scores
	 */
	public Result(final List<String> imageNames, final Map<String, UnmodifiableArrayList<Double>> scores) {
		this.imageNames = unmodifiableList(imageNames);
		this.scores = unmodifiableMap(scores);
	}

	/**
	 * @return image names
	 */
	public List<String> getImageNames() {
		return imageNames;
	}

	/**
	 * @return scores
	 */
	public Map<String, UnmodifiableArrayList<Double>> getScores() {
		return scores;
	}

}
