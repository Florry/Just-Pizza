package se.florry.justpizza.player;

import org.lwjgl.glfw.GLFW;

import se.florry.engine.constants.Constants;
import se.florry.engine.input.Input;
import se.florry.justpizza.game.Game;
import se.florry.justpizza.model.entity.Entity;
import se.florry.justpizza.world.World;

public final class Player
{

	private final Input input;
	private final World world;

	private final Entity controlledEntity;

	public Player(final Input input, final World world, final Entity entity)
	{
		this.input = input;
		this.world = world;
		this.controlledEntity = entity;
	}

	public void init()
	{
		this.addInputs();
		this.world.addEntity(this.controlledEntity);
		this.controlledEntity.addProcessCallbackHandler(handle ->
		{
			updateCamera();
		});
	}

	private void addInputs()
	{

		this.input.press(GLFW.GLFW_KEY_KP_6, handle ->
		{
			this.controlledEntity.execute("walkRight");
		});
		this.input.press(GLFW.GLFW_KEY_KP_4, handle ->
		{
			this.controlledEntity.execute("walkLeft");
		});
		this.input.press(GLFW.GLFW_KEY_KP_8, handle ->
		{
			this.controlledEntity.execute("jump");
		});

		this.input.release(GLFW.GLFW_KEY_KP_0, handle ->
		{
			System.out.println(this.controlledEntity.position);
		});
	}

	private void updateCamera()
	{
		// Game.worldRenderOffset.x = -this.controlledEntity.position.x +
		// Constants.Display.WIDTH / 2;
		Game.worldRenderOffset.y = -this.controlledEntity.position.y + Constants.Display.HEIGHT / 2;
	}

	public void process(final float deltaTime)
	{
		final float speed = 200 * deltaTime;

		final float nextXpos = -this.controlledEntity.position.x + Constants.Display.WIDTH / 2;

		if (nextXpos > Game.worldRenderOffset.x)
		{
			if (Game.worldRenderOffset.x + speed > nextXpos)
			{
				Game.worldRenderOffset.x = nextXpos;
			} else
			{
				Game.worldRenderOffset.x += speed;
			}

		} else
		{
			if (Game.worldRenderOffset.x - speed < nextXpos)
			{
				Game.worldRenderOffset.x = nextXpos;
			} else
			{
				Game.worldRenderOffset.x -= speed;
			}
		}

		// final float nextYpos = -this.controlledEntity.position.y +
		// Constants.Display.HEIGHT / 2;
		//
		// if (nextYpos > Game.worldRenderOffset.y)
		// {
		// if (Game.worldRenderOffset.y + speed > nextYpos)
		// {
		// Game.worldRenderOffset.y = nextYpos;
		// } else
		// {
		// Game.worldRenderOffset.y += speed;
		// }
		// } else
		// {
		// if (Game.worldRenderOffset.y - speed < nextYpos)
		// {
		// Game.worldRenderOffset.y = nextYpos;
		// } else
		// {
		// Game.worldRenderOffset.y -= speed;
		// }
		// }

	}

}
