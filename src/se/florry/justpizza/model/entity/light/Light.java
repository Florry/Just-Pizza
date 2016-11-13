package se.florry.justpizza.model.entity.light;

import org.lwjgl.util.vector.Vector2f;

import se.florry.justpizza.world.World;

public class Light
{

	public final Vector2f position;

	public Light(final Vector2f position)
	{
		this.position = position;
	}

	public void process(final World world)
	{
	}

}
