package se.florry.justpizza.physics;

import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.game.Game;
import se.florry.justpizza.world.World;

public final class Gravity
{

	private final World world;

	public Gravity(final World world)
	{
		this.world = world;
	}

	public void process()
	{
		this.world.getEntities()
				.forEach(entity ->
				{
					if (entity.isAffectedByGravity())
					{
						entity.velocity.y += GameConstants.Physics.GRAVITY * Game.getDeltaTime();
					}
				});
	}

}
