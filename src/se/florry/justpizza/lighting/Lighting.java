package se.florry.justpizza.lighting;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.model.Color;
import se.florry.justpizza.model.entity.block.Air;
import se.florry.justpizza.model.entity.block.WorldBlock;
import se.florry.justpizza.model.entity.light.Light;
import se.florry.justpizza.model.entity.light.PointLight;
import se.florry.justpizza.player.Player;
import se.florry.justpizza.utils.GameUtils;
import se.florry.justpizza.world.World;

public class Lighting
{

	private final World world;
	private final Player player;
	private boolean enabled;

	private final List<Light> lights;

	public Lighting(final World world, final Player player)
	{
		this.world = world;
		this.enabled = true;
		this.lights = new ArrayList<>();
		this.player = player;
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
		// final int xStart = this.world.getXStart() - 2;
		// final int xLimit = this.world.getXLimit() + 2;
		// final int yLimit = this.world.getYLimit() + 2;
		//
		// for (int i = 0; i < 2; i++)
		// {
		// for (int x = xStart; x < xLimit + 2; x++)
		// {
		// int lightStrength = 255;
		// boolean hasReachedBlock = false;
		//
		// for (int y = 0; y < yLimit; y++)
		// {
		// final WorldBlock current = this.world.get(x, y, i);
		// if (this.world.get(x, y, i) != null)
		// {
		// current.setLightLevel(new Color(0, 0, 0, 0));
		// }
		//
		// if (lightStrength > 0)
		// {
		// if (this.world.get(x, y, i) != null)
		// {
		// if (!(this.world.get(x, y, i) instanceof Air))
		// {
		// hasReachedBlock = true;
		//
		// current.getLightLevel().r += lightStrength - (20 * i);
		// current.getLightLevel().g += lightStrength - (20 * i);
		// current.getLightLevel().b += lightStrength - (20 * i);
		// current.getLightLevel().a += lightStrength - (20 * i);
		//
		// lightStrength -= 5;
		// } else
		// {
		// current.getLightLevel().r += 255;
		// current.getLightLevel().g += 255;
		// current.getLightLevel().b += 255;
		// }
		// }
		// }
		//
		// if (!this.enabled)
		// {
		// if (current != null)
		// {
		// current.getLightLevel().r += 255;
		// current.getLightLevel().g += 255;
		// current.getLightLevel().b += 255;
		// current.getLightLevel().a += 255;
		// }
		// }
		// }
		// }
		// }
		this.renderLighting();
	}

	public void renderLighting()
	{
		// this.blackout();

		final int xStart = this.world.getXStart();
		final int xLimit = this.world.getXLimit();
		final int yStart = this.world.getYStart();
		final int yLimit = this.world.getYLimit();

		for (int x = xStart; x < xLimit; x++)
			for (int y = yStart; y < yLimit; y++)
			// for (int x = 0; x < this.world.columns(); x++)
			// for (int y = 0; y < this.world.rows(x); y++)
			{
				int lightStrength = 255;
				final WorldBlock current = this.world.get(x, y);

				if (current != null)
				{

					final float distance = (float) GameUtils.getDistance(this.player.getLightGridPosition().x, this.player.getLightGridPosition().y, x, y);

					// if (distance > 9.5f)
					// continue;

					// if (distance > 5.5f)
					// lightStrength -= (distance) * 10;

					try
					{

						// final int skylightHits =
						// this.calculateShadowsFromSkylight(x, y, direction,
						// this.player.getLightGridPosition(), current.id);

						// final Vector2f direction = new Vector2f(x, y);
						//
						// direction.x -= this.player.getLightGridPosition().x;
						// direction.y -= this.player.getLightGridPosition().y;
						//
						// direction.normalise(direction);

						int hits = this.calculateShadowsFromLight(x,
								y,
								(int) this.player.getLightGridPosition().x,
								(int) this.player.getLightGridPosition().y,
								current.id);

						// int hits = this.calculateShadowsFromLight(x, y, x +
						// 2, 0, current.id);

						if (hits == 0)
							current.setLightLevel(new Color(lightStrength, lightStrength, lightStrength));
						else
						{
							lightStrength += 150;
							current.setLightLevel(new Color(lightStrength / hits, lightStrength / hits, lightStrength / hits));
						}

					} catch (final Exception e)
					{
					}
				}
			}
	}

	final int MAX_STEPS = 100;
	final int MAX_HITS = 100;

	private int calculateShadowsFromLight(final int xOrigin, final int yOrigin, final int endLocationX, final int endLocationY, final Integer currentId)
	{
		int hits = 0;

		final Vector2f direction = new Vector2f(xOrigin, yOrigin);

		direction.x -= endLocationX;
		direction.y -= endLocationY;

		direction.normalise(direction);

		for (int i = 0; i < this.MAX_STEPS; i++)
		{
			final int x = Math.round(new Float(xOrigin) - (direction.x * i));
			final int y = Math.round(new Float(yOrigin) - (direction.y * i));

			if (x == endLocationX && y == endLocationY)
			{
				hits++;
				break;
			}

			final WorldBlock wallBlock = this.world.get(x, y);

			if (wallBlock != null && wallBlock.blocksLight())
			{
				hits++;

				if (hits == this.MAX_HITS)
					break;
			}
		}

		return hits;
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
					} else if (!hasReachedBlock)
						current.setLightLevel(new Color(lightStrength, lightStrength, lightStrength, lightStrength));
					else
					{
						final WorldBlock previous = this.world.get(x, y - 1);
						if (previous != null)
						{
							final Color lightLevel = previous.getLightLevel();
							lightLevel.r -= lightStrength / 2;
							lightLevel.g -= lightStrength / 2;
							lightLevel.b -= lightStrength / 2;
							previous.setLightLevel(lightLevel);
						}
						break;
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

	public void blackout()
	{
		for (int x = 0; x < this.world.columns(); x++)
			for (int y = 0; y < this.world.rows(x); y++)
			{
				final WorldBlock current = this.world.get(x, y);
				if (this.world.get(x, y) != null)
					current.setLightLevel(new Color(0, 0, 0, 0));
			}
	}

}
