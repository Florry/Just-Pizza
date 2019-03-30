package se.florry.justpizza.utils;

public class GameUtils
{

	public static double getDistance(final int x1, final float y1, final int x, final float y)
	{
		return Math.sqrt(Math.pow((x1 - x), 2) + Math.pow((y - y1), 2));
	}

	public static double getDistance(float x, float y, float x2, float y2)
	{
		return GameUtils.getDistance((int) x, (int) y, (int) x2, (int) y2);
	}

}
