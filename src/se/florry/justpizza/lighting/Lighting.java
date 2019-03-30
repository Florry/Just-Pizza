package se.florry.justpizza.lighting;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.model.Color;
import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.game.Game;
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

		this.blackout();

		final int xStart = this.world.getXStart();
		final int xLimit = this.world.getXLimit();
		final int yStart = this.world.getYStart();
		final int yLimit = this.world.getYLimit();

		for (int x = xStart; x < xLimit; x++)
			for (int y = yStart; y < yLimit; y++)
			{
				int lightStrength = 255;
				final WorldBlock current = this.world.get(x, y);

				if (current != null && !(current instanceof Air))
				{
					final float currentAdjustedGridX = (current.position.x + (Game.worldRenderOffset.x / GameConstants.Entity.WORLD_BLOCK_SIZE))
							/ GameConstants.Entity.WORLD_BLOCK_SIZE;
					final float currentAdjustedGridY = (current.position.y + (Game.worldRenderOffset.y / GameConstants.Entity.WORLD_BLOCK_SIZE))
							/ GameConstants.Entity.WORLD_BLOCK_SIZE;

					final float distance = (float) GameUtils.getDistance(currentAdjustedGridX,
							currentAdjustedGridY,
							this.player.getLightGridPosition().x,
							this.player.getLightGridPosition().y);

					if (distance > 9.5f)
						continue;

					if (distance > 2f)
						lightStrength -= (distance * 10);

					final Vector2f direction = new Vector2f(currentAdjustedGridX, currentAdjustedGridY);

					direction.x -= this.player.getLightGridPosition().x;
					direction.y -= this.player.getLightGridPosition().y;

					try
					{
						direction.normalise(direction);

						final boolean isLit = this.rayMarch(new Vector2f(currentAdjustedGridX, currentAdjustedGridY),
								direction,
								this.player.getLightGridPosition(),
								current.id);

						if (isLit)
							current.setLightLevel(new Color(lightStrength, lightStrength, lightStrength));

					} catch (final Exception e)
					{
					}
				}
			}

	}

	final int MAX_STEPS = 100;

	private boolean rayMarch(final Vector2f origin, final Vector2f direction, final Vector2f endLocation, final Integer currentId)
	{
		boolean didHitBlock = false;

		for (int i = 1; i < this.MAX_STEPS; i++)
		{
			final Vector2f positionToCheck = new Vector2f();

			positionToCheck.x = origin.x + (direction.x);
			positionToCheck.y = origin.y + (direction.y);

			positionToCheck.x *= i;
			positionToCheck.y *= i;

			final int x = (int) Math.round(positionToCheck.x);
			final int y = (int) Math.round(positionToCheck.y);

			if (x == (int) Math.round(endLocation.x) && y == (int) Math.round(endLocation.y))
			{
				didHitBlock = false;
				break;
			}

			final WorldBlock wallBlock = this.world.get(x, y);

			if (wallBlock != null && !(wallBlock instanceof Air) && wallBlock.id != currentId)
			{
				didHitBlock = true;
				break;
			}

			// didHitBlock = true;
		}

		return !didHitBlock;
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

	private void blackout()
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
