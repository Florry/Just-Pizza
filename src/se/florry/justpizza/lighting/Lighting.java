package se.florry.justpizza.lighting;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.model.Color;
import se.florry.justpizza.model.entity.block.Air;
import se.florry.justpizza.model.entity.block.WorldBlock;
import se.florry.justpizza.model.entity.light.Light;
import se.florry.justpizza.model.entity.light.PointLight;
import se.florry.justpizza.world.World;

public class Lighting
{

	private final World world;
	private boolean enabled;

	private final List<Light> lights;

	public Lighting(final World world)
	{
		this.world = world;
		this.enabled = true;
		this.lights = new ArrayList<>();
	}

	public void enableLighting()
	{
		this.enabled = true;
	}

	public void disableLighting()
	{
		this.enabled = false;
	}

	public void toggleLighting()
	{
		this.enabled = !this.enabled;
	}

	public void process()
	{
		final int xStart = this.world.getXStart() - 2;
		final int xLimit = this.world.getXLimit() + 2;
		final int yLimit = this.world.getYLimit() + 2;

		for (int i = 0; i < 2; i++)
		{
			for (int x = xStart; x < xLimit + 2; x++)
			{
				int lightStrength = 255;
				boolean hasReachedBlock = false;

				for (int y = 0; y < yLimit; y++)
				{
					final WorldBlock current = this.world.get(x, y, i);
					if (this.world.get(x, y, i) != null)
					{
						current.setLightLevel(new Color(0, 0, 0, 0));
					}

					if (lightStrength > 0)
					{
						if (this.world.get(x, y, i) != null)
						{
							if (!(this.world.get(x, y, i) instanceof Air))
							{
								hasReachedBlock = true;

								current.getLightLevel().r += lightStrength - (20 * i);
								current.getLightLevel().g += lightStrength - (20 * i);
								current.getLightLevel().b += lightStrength - (20 * i);
								current.getLightLevel().a += lightStrength - (20 * i);

								lightStrength -= 5;
							} else
							{
								current.getLightLevel().r += 255;
								current.getLightLevel().g += 255;
								current.getLightLevel().b += 255;
							}
						}
					}

					if (!this.enabled)
					{
						if (current != null)
						{
							current.getLightLevel().r += 255;
							current.getLightLevel().g += 255;
							current.getLightLevel().b += 255;
							current.getLightLevel().a += 255;
						}
					}
				}
			}
		}

	}

	public void process2()
	{
		this.blackout();

		for (int x = 0; x < this.world.columns(); x++)
		{
			int lightStrength = 255;
			boolean hasReachedBlock = false;

			for (int y = 0; y < this.world.rows(x); y++)
			{
				final WorldBlock current = this.world.get(x, y);
				if (current != null)
				{
					current.setLightLevel(new Color(0, 0, 0, 0));

					if (!(current instanceof Air))
					{
						if (!hasReachedBlock)
						{
							hasReachedBlock = true;
							current.setLightLevel(new Color(lightStrength, lightStrength, lightStrength, 255));
							this.lights.add(new PointLight(new Vector2f(x, y), current.getIndirectLightColor()));
						}
						current.getLightLevel().r += lightStrength;
						current.getLightLevel().g += lightStrength;
						current.getLightLevel().b += lightStrength;
						current.getLightLevel().a += lightStrength;

						lightStrength -= 20;
					} else
					{
						if (!hasReachedBlock)
						{
							current.setLightLevel(new Color(lightStrength, lightStrength, lightStrength, lightStrength));
						} else
						{
							final WorldBlock previous = this.world.get(x, y - 1);
							if (previous != null)
							{
								Color lightLevel = previous.getLightLevel();
								lightLevel.r -= (lightStrength / 2);
								lightLevel.g -= (lightStrength / 2);
								lightLevel.b -= (lightStrength / 2);
								previous.setLightLevel(lightLevel);
							}
							break;
						}
					}
				}
			}
		}

		// this.lights.forEach(light ->
		// {
		// light.process(this.world);
		// });
		//
		// this.lights.clear();
	}

	private void blackout()
	{
		for (int x = 0; x < this.world.columns(); x++)
		{
			for (int y = 0; y < this.world.rows(x); y++)
			{
				final WorldBlock current = this.world.get(x, y);
				if (this.world.get(x, y) != null)
				{
					current.setLightLevel(new Color(0, 0, 0, 0));
				}
			}
		}
	}

}
