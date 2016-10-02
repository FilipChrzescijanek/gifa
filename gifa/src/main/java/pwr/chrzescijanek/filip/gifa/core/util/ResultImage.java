package pwr.chrzescijanek.filip.gifa.core.util;

public class ResultImage {

	public final byte[] data;
	public final boolean[] mask;
	public final int width;
	public final int height;
	public final int channels;

	public ResultImage( final byte[] data, final boolean[] mask, final int width, final int height, final int channels ) {
		this.data = data;
		this.mask = mask;
		this.width = width;
		this.height = height;
		this.channels = channels;
	}
}
