package se.florry.justpizza.main;

import se.florry.justpizza.game.JustPizza;

public class MainGameLoop
{

	public static void main(String... args)
	{
		final JustPizza justPizza = new JustPizza();
		justPizza.run();
	}

}
