package se.florry.justpizza.player;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.constants.Constants;
import se.florry.engine.input.Input;
import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.game.Game;
import se.florry.justpizza.model.entity.Entity;
import se.florry.justpizza.world.World;

public final class Player
{

	private final Input input;
	private final World world;

	public final Entity controlledEntity;
	public Vector2f lightPosition;

	private boolean lightMovingUp = false;

	public Player(final Input input, final World world, final Entity entity)
	{
		this.input = input;
		this.world = world;
		this.controlledEntity = entity;
		this.lightPosition = new Vector2f();
		this.lightPosition.x = 2545;
		this.lightPosition.y = 600;
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
		})
				.release(GLFW.GLFW_KEY_KP_6, handle ->
				{
					this.controlledEntity.execute("stopMoving");
				});

		this.input.press(GLFW.GLFW_KEY_KP_4, handle ->
		{
			this.controlledEntity.execute("walkLeft");
		})
				.release(GLFW.GLFW_KEY_KP_4, handle ->
				{
					this.controlledEntity.execute("stopMoving");
				});

		this.input.release(GLFW.GLFW_KEY_N, handle ->
		{
			this.controlledEntity.execute("toggleNoClip");
		});

		this.input.press(GLFW.GLFW_KEY_KP_8, handle ->
		{
			this.controlledEntity.execute("jump");
		});

		this.input.press(GLFW.GLFW_KEY_KP_2, handle ->
		{
			this.controlledEntity.execute("duck");
		});

		this.input.release(GLFW.GLFW_KEY_KP_0, handle ->
		{
			System.out.println(this.controlledEntity.position);
		});
	}

	private void updateCamera()
	{
		Game.worldRenderOffset.x = -this.controlledEntity.position.x + Constants.Display.WIDTH / 2;
		Game.worldRenderOffset.y = -this.controlledEntity.position.y + Constants.Display.HEIGHT / 2;
	}

	public void process(final float deltaTime)
	{
		// final float speed = 200 * deltaTime;
		//
		// final float nextXpos = -this.controlledEntity.position.x +
		// Constants.Display.WIDTH / 2;
		//
		// if (nextXpos > Game.worldRenderOffset.x)
		// {
		// if (Game.worldRenderOffset.x + speed > nextXpos)
		// {
		// Game.worldRenderOffset.x = nextXpos;
		// } else
		// {
		// Game.worldRenderOffset.x += speed;
		// }
		//
		// } else
		// {
		// if (Game.worldRenderOffset.x - speed < nextXpos)
		// {
		// Game.worldRenderOffset.x = nextXpos;
		// } else
		// {
		// Game.worldRenderOffset.x -= speed;
		// }
		// }

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

		this.lightPosition.x = this.controlledEntity.position.x;
		this.lightPosition.y = this.controlledEntity.position.y + (GameConstants.Entity.WORLD_BLOCK_SIZE);

		// if (this.lightMovingUp)
		// {
		// this.lightPosition.y -= (200 * deltaTime);
		// this.lightPosition.x += (200 * deltaTime);
		// } else
		// {
		// this.lightPosition.y += (200 * deltaTime);
		// this.lightPosition.x -= (200 * deltaTime);
		// }
		//
		// if (this.lightPosition.y >= 1840)
		// this.lightMovingUp = true;
		//
		// if (this.lightPosition.y <= 600)
		// this.lightMovingUp = false;
	}

	public Vector2f getLightGridPosition()
	{
		final Vector2f gridPosition = new Vector2f();

		gridPosition.x = (int) this.lightPosition.x / GameConstants.Entity.WORLD_BLOCK_SIZE;
		gridPosition.y = (int) this.lightPosition.y / GameConstants.Entity.WORLD_BLOCK_SIZE;

		return gridPosition;
	}

}
