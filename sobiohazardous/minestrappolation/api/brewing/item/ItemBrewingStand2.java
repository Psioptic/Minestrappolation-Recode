package sobiohazardous.minestrappolation.api.brewing.item;

import sobiohazardous.minestrappolation.api.Minestrappolation;

import net.minecraft.item.ItemReed;

public class ItemBrewingStand2 extends ItemReed
{
	public ItemBrewingStand2(int id)
	{
		super(id, Minestrappolation.brewingStand2);
		this.setTextureName("brewing_stand");	
	}
}
