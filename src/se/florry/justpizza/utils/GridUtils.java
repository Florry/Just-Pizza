package se.florry.justpizza.utils;

public class GridUtils
{

	public void gridLoop(final int width, final int height, final GridLoopHandler handler)
	{
		gridLoop(0, 0, width, height, handler);
	}

	public static void gridLoop(final int xStart, final int yStart, final int width, final int height, final GridLoopHandler handler)
	{
		for (int x = xStart; x < width; x++)
		{
			for (int y = yStart; y < width; y++)
			{
				handler.handle(x, y);
			}
		}
	}

	public static interface GridLoopHandler
	{

		public void handle(final int x, final int y);

	}

}
