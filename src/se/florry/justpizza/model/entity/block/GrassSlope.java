package se.florry.justpizza.model.entity.block;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import se.florry.justpizza.world.World;

public class GrassSlope extends WorldBlock
{
	private final World world;

	private boolean prepared;

	public GrassSlope(final World world)
	{
		this.world = world;

		this.modelData.color.r = 255;
		this.modelData.color.g = 255;
		this.modelData.color.b = 255;
		this.modelData.color.a = 255;

		this.indirectLightColor.r = 26;
		this.indirectLightColor.g = 130;
		this.indirectLightColor.b = 38;
		this.indirectLightColor.a = 255;

		this.modelData.setTransparency(true);

		this.prepared = false;
	}

	@Override
	public String getTexture()
	{
		return "blocks/dirt01MossySlope";
	}

	@Override
	public void process()
	{
		if (!this.prepared)
		{
			this.prepared = true;
			world.add((int) this.position.x, (int) this.position.y + 1, 0, new GrassSlopeInverted());
		}
	}

	@Override
	public List<Rectangle> getCollisionShape()
	{
		final int STEP_SIZE = 32;
		List<Rectangle> collisions = new ArrayList<>();

		for (int i = 0; i < STEP_SIZE; i++)
		{
			int x = (int) this.position.x;
			int y = (int) this.position.y + (i * (this.getSize().height / STEP_SIZE));
			int width = (i * this.getSize().width / STEP_SIZE);
			int height = (i * this.getSize().height / STEP_SIZE);

			final Rectangle temp = new Rectangle(x, y, width, height);

			collisions.add(temp);
		}

		return collisions;
	}

	// @Override
	// public void collide(Entity entity)
	// {
	// entity.position.y -= 100 * Game.getDeltaTime();
	// }

}
