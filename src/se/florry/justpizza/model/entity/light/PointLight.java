package se.florry.justpizza.model.entity.light;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.model.Color;
import se.florry.justpizza.model.entity.block.WorldBlock;
import se.florry.justpizza.world.World;

public class PointLight extends Light
{

	private final int radius;
	private final int strength;
	private final Color color;

	public PointLight(final Vector2f position, final Color color, final int radius, final int strength)
	{
		super(position);
		this.radius = radius;
		this.strength = strength;
		this.color = color;
	}

	public PointLight(final Vector2f position, final Color color)
	{
		this(position, color, 5, 255);
	}

	public int getRadius()
	{
		return this.radius;
	}

	public int getStrength()
	{
		return this.strength;
	}

	@Override
	public void process(final World world)
	{
		for (int x = (int) this.position.x - (this.radius / 2); x < (int) this.position.x + (this.radius / 2); x++)
		{
			for (int y = (int) this.position.y - ((this.radius)); y < this.position.y + 2; y++)
			{
				WorldBlock current = world.get(x, y);

				if (current != null)
				{
					Color newLightLevel = current.getLightLevel();

					double distance = getDistance((int) this.position.x, this.position.y, x, y);
					float shadowMultiplier = (float) ((float) ((float) this.strength - distance) / distance);

					newLightLevel.r += shadowMultiplier * ((this.color.r / 64));
					newLightLevel.g += shadowMultiplier * ((this.color.g / 64));
					newLightLevel.b += shadowMultiplier * ((this.color.b / 64));
					newLightLevel.a += shadowMultiplier;
				}
			}
		}
	}

	public static double getDistance(final int x1, final float y1, final int x, final float y)
	{
		return Math.sqrt(Math.pow((x1 - x), 2) + Math.pow((y - y1), 2));
	}
}
