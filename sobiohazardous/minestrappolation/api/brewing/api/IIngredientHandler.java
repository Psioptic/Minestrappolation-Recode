package sobiohazardous.minestrappolation.api.brewing.api;

import net.minecraft.item.ItemStack;

public interface IIngredientHandler
{
	public boolean canHandleIngredient(ItemStack ingredient);
	
	public ItemStack applyIngredient(ItemStack ingredient, ItemStack potion);
	
	public boolean canApplyIngredient(ItemStack ingredient, ItemStack potion);
}
