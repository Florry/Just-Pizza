package se.florry.justpizza.model.entity.block;

public class Dirt extends WorldBlock
{

	public Dirt()
	{
		this.modelData.color.r = 255;
		this.modelData.color.g = 255;
		this.modelData.color.b = 255;
		this.modelData.color.a = 255;

		this.indirectLightColor.r = 115;
		this.indirectLightColor.g = 67;
		this.indirectLightColor.b = 53;
		this.indirectLightColor.a = 255;
	}

	@Override
	public String getTexture()
	{
		return "blocks/dirt01";
	}

}
