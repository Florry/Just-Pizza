package se.florry.justpizza.game;

import se.florry.engine.model.Engine;

/*
 * The game API
 */
public class JustPizza
{

	private final Engine engine;
	private final Game game;

	public JustPizza()
	{
		this.engine = new Engine();
		this.engine.setTitle("Just Pizza");
		this.game = new Game(this.engine);
	}

	public void run()
	{
		this.game.init();
		this.game.run();
	}

}
