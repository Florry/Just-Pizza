package se.florry.justpizza.model.entity.humanoid;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.input.Input;
import se.florry.engine.model.AnimatedQuadModel;
import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.model.entity.Entity;
import se.florry.justpizza.world.World;

public final class Humanoid extends Entity
{

	public static final float ANIM_ANIMATION_STEP_SIZE = 1f / 6f;
	public static final float ANIM_WALK_RIGHT = 0;
	public static final float ANIM_WALK_LEFT = ANIM_ANIMATION_STEP_SIZE * 1;
	public static final float ANIM_IDLE_RIGHT = ANIM_ANIMATION_STEP_SIZE * 2;
	public static final float ANIM_IDLE_LEFT = ANIM_ANIMATION_STEP_SIZE * 3;
	public static final float ANIM_JUMP_RIGHT = ANIM_ANIMATION_STEP_SIZE * 4;
	public static final float ANIM_JUMP_LEFT = ANIM_ANIMATION_STEP_SIZE * 5;

	private final static int TOTAL_FRAMES = 4;

	public Humanoid(final World world, final Input input)
	{
		super(new AnimatedQuadModel(GameConstants.Entity.HUMANOID_SIZE, 1, 1, 1, 2), new Vector2f(), world);

		this.isAffectedByGravity = true;
		this.modelData.setTransparency(true);

		this.modelData.frameDimensions.x = 1 / (float) TOTAL_FRAMES;
		this.modelData.frameDimensions.y = 1 / (float) (TOTAL_FRAMES + 2);
		this.modelData.textureSize.y = 1 / (float) (TOTAL_FRAMES + 2);

		this.modelData.setAnimationRate(6);
		this.modelData.setTotalFrames(4);
		this.modelData.startAnimation();

		this.setupBehaviours();
	}

	private void setupBehaviours()
	{
		final int strength = 50;
		final Vector2f maxVelocity = new Vector2f(200, 0);

		this.behaviours.put("jump", Void ->
		{
			if (!this.isFalling())
			{
				this.velocity.y -= 400;
			}
		});

		this.behaviours.put("walkLeft", run ->
		{
			if (this.velocity.x > -maxVelocity.x)
			{
				this.velocity.x -= strength;
			}
		});

		this.behaviours.put("walkRight", Void ->
		{
			if (this.velocity.x < maxVelocity.x)
			{
				this.velocity.x += strength;
			}
		});
	}

	@Override
	public void process()
	{
		super.process();

		if (this.velocity.x > 0)
		{
			// if (this.isFalling)
			// {
			// this.setAnimation(ANIM_JUMP_RIGHT);
			// } else
			// {
			this.setAnimation(ANIM_WALK_RIGHT);
			// }
		} else if (this.velocity.x < 0)
		{
			// if (this.isFalling)
			// {
			// this.setAnimation(ANIM_JUMP_LEFT);
			// } else
			// {
			this.setAnimation(ANIM_WALK_LEFT);
			// }
		} else
		{
			if (this.direction == 1)
			{
				this.setAnimation(ANIM_IDLE_RIGHT);
			} else
			{
				this.setAnimation(ANIM_IDLE_LEFT);
			}
		}
	}

	@Override
	public String getTexture()
	{
		return "jonas01";
	}

}
