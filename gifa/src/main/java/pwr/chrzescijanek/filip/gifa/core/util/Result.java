package pwr.chrzescijanek.filip.gifa.core.util;

import com.sun.javafx.UnmodifiableArrayList;

import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

public final class Result {

	private final List<String> imageNames;
	private final Map<String, UnmodifiableArrayList<Double>> results;

	public Result( final List< String > imageNames, final Map< String, UnmodifiableArrayList< Double > > results ) {
		this.imageNames = unmodifiableList(imageNames);
		this.results = unmodifiableMap(results);
	}

	public List< String > getImageNames() {
		return imageNames;
	}

	public Map< String, UnmodifiableArrayList< Double > > getResults() { return results; }

}
