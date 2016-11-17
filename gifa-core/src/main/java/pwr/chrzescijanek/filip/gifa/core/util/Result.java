package pwr.chrzescijanek.filip.gifa.core.util;

import com.sun.javafx.UnmodifiableArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

/**
 * Represents images evaluation result.
 */
public final class Result {

	private final List<List<String>> imageNames;

	private final Map<String, UnmodifiableArrayList<Double>> scores;

	/**
	 * Constructs new Result with given image names and scores.
	 *
	 * @param imageNames image names
	 * @param scores     associated scores
	 */
	public Result(final List<List<String>> imageNames, final Map<String, UnmodifiableArrayList<Double>> scores) {
		final List<List<String>> imgNames = new ArrayList<>();
		for (final List<String> names : imageNames)
			imgNames.add(unmodifiableList(names));
		this.imageNames = unmodifiableList(imgNames);
		this.scores = unmodifiableMap(scores);
	}

	/**
	 * @return image names
	 */
	public List<List<String>> getImageNames() {
		return imageNames;
	}

	/**
	 * @return scores
	 */
	public Map<String, UnmodifiableArrayList<Double>> getScores() {
		return scores;
	}

}
