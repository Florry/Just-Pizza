package se.florry.justpizza.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.constants.Constants;
import se.florry.engine.model.Color;
import se.florry.engine.model.Engine;
import se.florry.engine.model.TextModel;
import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.lighting.Lighting;
import se.florry.justpizza.model.entity.block.Air;
import se.florry.justpizza.model.entity.humanoid.Humanoid;
import se.florry.justpizza.physics.Gravity;
import se.florry.justpizza.player.Player;
import se.florry.justpizza.world.World;

public class Game
{

	public static Vector2f worldRenderOffset = new Vector2f();

	private final Engine engine;
	private final World world;
	private final Lighting lighting;
	private final Gravity gravity;
	private final Player player;
	private final Humanoid playerHumanoid;

	private final TextModel fpsMeter;

	private static float deltaTime;

	protected Game(final Engine engine)
	{
		this.engine = engine;
		this.world = new World();
		this.fpsMeter = new TextModel("FPS: ", 16, new Vector2f(10, 10), Constants.Colors.GREEN);
		this.gravity = new Gravity(this.world);
		this.playerHumanoid = new Humanoid(this.world, this.engine.input());
		this.player = new Player(this.engine.input(), this.world, this.playerHumanoid);
		this.lighting = new Lighting(this.world, this.player);
	}

	protected void init()
	{
		this.world.init();
		this.player.init();
		this.addInputs();
		this.engine.setBackgroundColor(new Color(123, 189, 255, 255));

		for (int i = 0; i < this.world.rows(1); i++)
		{
			if (this.world.get(1, i) != null)
			{
				if (!(this.world.get(1, i) instanceof Air))
				{
					worldRenderOffset.y -= i * GameConstants.Entity.WORLD_BLOCK_SIZE - GameConstants.Entity.WORLD_BLOCK_SIZE * 5;
					break;
				}
			}
		}
	}

	protected void run()
	{
		this.engine.run(deltaTime ->
		{

			setDeltaTime(deltaTime);
			this.processGravity();
			this.processWorld();
			this.player.process(deltaTime);
			this.displayFPS();
			this.processLighting();

			this.world.renderedObjects = 0;
		});
	}

	private static void setDeltaTime(final float input)
	{
		deltaTime = input;
	}

	private void addInputs()
	{
		final Vector2f speed = new Vector2f(100, 0);

		this.engine.input()
				.press(GLFW.GLFW_KEY_RIGHT, Void ->
				{
					worldRenderOffset.x -= 1 * speed.x;
				})
				.press(GLFW.GLFW_KEY_LEFT, Void ->
				{
					worldRenderOffset.x += 1 * speed.x;
				})
				.press(GLFW.GLFW_KEY_DOWN, Void ->
				{
					worldRenderOffset.y -= 1 * speed.x;
				})
				.press(GLFW.GLFW_KEY_UP, Void ->
				{
					worldRenderOffset.y += 1 * speed.x;
				})
				.press(GLFW.GLFW_KEY_LEFT_SHIFT, Void ->
				{
					speed.x = 10;
				})
				.release(GLFW.GLFW_KEY_LEFT_SHIFT, Void ->
				{
					speed.x = 1;
				})
				.release(GLFW.GLFW_KEY_SPACE, Void ->
				{
					this.lighting.toggleLighting();
				});
	}

	private void displayFPS()
	{
		if (this.engine.getCurrentFrame() % 60 == 0)
		{
			this.fpsMeter.setSentence("FPS: " + this.engine.getFramerate() + ". Rendered Entities: " + this.world.renderedObjects + ". Total: "
					+ this.world.columns() * this.world.rows(this.world.columns() - 1));
		}
		this.fpsMeter.render();
	}

	private void processGravity()
	{
		this.gravity.process();
	}

	private void processLighting()
	{
		if (this.engine.getCurrentFrame() % 15 == 0)
		{
			this.lighting.process();
		}
	}

	private void processWorld()
	{
		this.world.process();
	}

	public static float getDeltaTime()
	{
		return deltaTime;
	}

}
