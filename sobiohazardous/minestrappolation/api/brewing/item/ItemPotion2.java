package sobiohazardous.minestrappolation.api.brewing.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import sobiohazardous.minestrappolation.api.Minestrappolation;
import sobiohazardous.minestrappolation.api.brewing.brewing.Brewing;
import sobiohazardous.minestrappolation.api.brewing.brewing.BrewingBase;
import sobiohazardous.minestrappolation.api.brewing.brewing.BrewingList;
import sobiohazardous.minestrappolation.api.brewing.brewing.PotionUtils;
import sobiohazardous.minestrappolation.api.brewing.entity.EntityPotion2;

import com.google.common.collect.HashMultimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * @author Clashsoft
 */
public class ItemPotion2 extends Item
{
	public static boolean	SHIFT				= false;
	private Icon			bottle;
	public Icon				splashbottle;
	private Icon			liquid;

	public ItemPotion2(int par1)
	{
		super(par1);
		this.setMaxStackSize(Minestrappolation.potionStackSize);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabBrewing);
		this.setTextureName("potion");
	}

	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[] { Minestrappolation.potions, CreativeTabs.tabBrewing, CreativeTabs.tabAllSearch };
	}

	/**
	 * Returns a list of potion effects for the specified itemstack.
	 */
	public List<Brewing> getEffects(ItemStack par1ItemStack)
	{
		if (par1ItemStack != null && !isWater(par1ItemStack.getItemDamage()))
		{
			if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("Brewing"))
			{
				List var6 = new ArrayList();
				NBTTagList var3 = par1ItemStack.getTagCompound().getTagList("Brewing");
				boolean var2 = true;

				for (int var4 = 0; var4 < var3.tagCount(); ++var4)
				{
					NBTTagCompound var5 = (NBTTagCompound) var3.tagAt(var4);
					Brewing b = Brewing.readFromNBT(var5);
					var6.add(b);
				}
				return var6;
			}
			else
			{
				return brewingTransform(par1ItemStack.getItemDamage(), Item.potion.getEffects(par1ItemStack));
			}
		}
		return new ArrayList();
	}

	private static List brewingTransform(int par1, List<PotionEffect> par2List)
	{
		if (par2List != null && par2List.size() > 0)
		{
			List<Brewing> ret = new ArrayList<Brewing>(par2List.size());
			for (PotionEffect effect : par2List)
			{
				ret.add(new Brewing(effect, 0, effect.getDuration(), BrewingList.awkward));
			}
			return ret;
		}
		return new ArrayList();
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			--par1ItemStack.stackSize;
		}

		if (!par2World.isRemote)
		{
			List var4 = this.getEffects(par1ItemStack);

			if (var4 != null)
			{
				Iterator var5 = var4.iterator();

				while (var5.hasNext())
				{
					Brewing var6 = (Brewing) var5.next();
					if (var6.getEffect() != null)
					{
						par3EntityPlayer.addPotionEffect(var6.getEffect());
					}
				}
			}
		}

		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			if (par1ItemStack.stackSize <= 0)
			{
				return new ItemStack(Item.glassBottle);
			}

			par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle));
		}

		return par1ItemStack;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 32;
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.drink;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (isSplash(par1ItemStack.getItemDamage()))
		{
			if (!par3EntityPlayer.capabilities.isCreativeMode)
			{
				--par1ItemStack.stackSize;
			}

			par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!par2World.isRemote)
			{
				par2World.spawnEntityInWorld(new EntityPotion2(par2World, par3EntityPlayer, par1ItemStack));
			}

			return par1ItemStack;
		}
		else
		{
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			return par1ItemStack;
		}
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Gets an icon index based on an item's damage value
	 */
	public Icon getIconFromDamage(int par1)
	{
		return isSplash(par1) ? splashbottle : bottle;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(ItemStack par1ItemStack, int par2)
	{
		return par2 == 0 ? this.liquid : (isSplash(par1ItemStack.getItemDamage()) ? splashbottle : bottle);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = this.bottle = par1IconRegister.registerIcon(this.getIconString() + "_bottle_drinkable");
		this.splashbottle = par1IconRegister.registerIcon(this.getIconString() + "_bottle_splash");
		this.liquid = par1IconRegister.registerIcon(this.getIconString() + "_overlay");
	}

	@SideOnly(Side.CLIENT)
	public static Icon func_94589_d(String par0Str)
	{
		return par0Str.equals("bottle_drinkable") ? Minestrappolation.potion2.bottle : (par0Str.equals("bottle_splash") ? Minestrappolation.potion2.splashbottle : (par0Str.equals("overlay") ? Minestrappolation.potion2.liquid : null));
	}

	/**
	 * returns wether or not a potion is a throwable splash potion based on
	 * damage value
	 */
	public boolean isSplash(int par1)
	{
		return par1 == 2 ? true : par1 == 1 ? false : ItemPotion.isSplash(par1);
	}

	public boolean isWater(int par1)
	{
		return par1 == 0;
	}

	public float	hue	= 0;

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		// hue = (hue + 0.0005F);
		// int color = Color.HSBtoRGB(hue, 1F, 1F);
		if (par2 == 0 && par1ItemStack != null)
		{
			if (isWater(par1ItemStack.getItemDamage()))
			{
				return 0x0C0CFF;
			}
			List<Brewing> effects = getEffects(par1ItemStack);
			if (effects != null && effects.size() > 0)
			{
				int[] i1 = new int[effects.size()];

				for (int j = 0; j < effects.size(); j++)
				{
					Brewing b = effects.get(j);
					i1[j] = b instanceof BrewingBase ? 0x0C0CFF : b.getLiquidColor();
				}
				return PotionUtils.combineColors(i1);
			}
			else
			{
				return 0x0C0CFF;
			}
		}
		else
		{
			return 16777215;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int i)
	{
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		List effects = getEffects(par1ItemStack);
		if (isWater(par1ItemStack.getItemDamage()))
		{
			return StatCollector.translateToLocal("item.emptyPotion.name").trim();
		}
		else
		{
			String var2 = "";

			if (isSplash(par1ItemStack.getItemDamage()))
			{
				var2 = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
			}

			List<Brewing> var3 = this.getEffects(par1ItemStack);
			String var4 = "";

			if (var3 != null && !var3.isEmpty())
			{
				if (var3.size() == Brewing.combinableEffects.size())
				{
					return "\u00a7b" + var2 + StatCollector.translateToLocal("potion.alleffects.postfix");
				}
				else if (var3.size() > 3)
				{
					return var2 + StatCollector.translateToLocal("potion.potionof") + " " + var3.size() + " " + StatCollector.translateToLocal("potion.effects");
				}
				else if (var3.get(0).isBase())
				{
					return StatCollector.translateToLocal("potion.prefix." + ((BrewingBase) var3.get(0)).basename).trim() + " " + var2 + super.getItemDisplayName(par1ItemStack);
				}
				for (int i = 0; i < var3.size(); i++)
				{
					if (i == 0)
					{
						var4 = StatCollector.translateToLocal(var3.get(i).getEffect() != null && var3.get(i).getEffect().getPotionID() > 0 ? (var3.get(i).getEffect().getEffectName() + ".postfix") : "");
						var2 += StatCollector.translateToLocal(var4).trim();
					}
					else if (i + 1 == var3.size())
					{
						var4 = var3.get(i).getEffect().getEffectName();
						var2 += " " + StatCollector.translateToLocal("potion.and") + " " + StatCollector.translateToLocal(var4).trim();
					}
					else
					{
						var4 = var3.get(i).getEffect().getEffectName();
						var2 += ", " + StatCollector.translateToLocal(var4).trim();
					}
				}
				return var2;
			}
			else
			{
				return super.getItemDisplayName(par1ItemStack);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return super.getFontRenderer(stack);
	}

	float	glowPos	= 0F;

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (!isWater(par1ItemStack.getItemDamage()))
		{
			List<Brewing> var5 = this.getEffects(par1ItemStack);
			HashMultimap<String, AttributeModifier> hashmultimap = HashMultimap.create();

			if (var5 != null && !var5.isEmpty())
			{
				int longestString = this.getItemDisplayName(par1ItemStack).length() + 10;
				glowPos += 0.25F;
				if (glowPos >= longestString)
					glowPos = 0;
				for (int i = 0; i < var5.size(); i++)
				{
					Brewing var7 = var5.get(i);
					boolean isNormalEffect = var7.getEffect() != null && var7.getEffect().getPotionID() > 0;
					String var8 = (isNormalEffect ? StatCollector.translateToLocal(var7.getEffect().getEffectName()) : "\u00a77" + StatCollector.translateToLocal("potion.empty")).trim();
					StringBuilder builder = new StringBuilder(var8);
					int randPos = 0;

					/*
					 * Fills the Attribute List Map
					 */
					if (var7.getEffect() != null)
					{
						Potion potion = Potion.potionTypes[var7.getEffect().getPotionID()];
						Map map = potion.func_111186_k();

						if (map != null && map.size() > 0)
						{
							for (Object o : map.keySet())
							{
								AttributeModifier attributemodifier = (AttributeModifier) map.get(o);
								if (attributemodifier != null)
								{
									AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.func_111183_a(var7.getEffect().getAmplifier(), attributemodifier), attributemodifier.getOperation());
									hashmultimap.put(((Attribute) o).getAttributeUnlocalizedName(), attributemodifier1);
								}
							}
						}
					}

					if (var7.getEffect() != null && var7.getEffect().getDuration() > 20)
					{
						builder.append(" (").append(var7.getEffect().getDuration() >= 1000000 ? StatCollector.translateToLocal("potion.infinite") : Potion.getDurationString(var7.getEffect())).append(")");
					}

					int glowPos2 = MathHelper.floor_float(glowPos) < var8.length() ? MathHelper.floor_float(glowPos) : var8.length();

					String var10 = builder.substring(0, glowPos2);
					String var11 = glowPos2 < builder.length() ? String.valueOf(builder.charAt(glowPos2)) : "";
					String var12 = glowPos2 + 1 < builder.length() ? builder.substring(glowPos2 + 1) : "";

					if (isNormalEffect)
					{
						builder.delete(0, builder.length());
						String colorLight = "";
						String colorDark = "";
						
						if (var7.isBadEffect())
						{
							colorLight = EnumChatFormatting.RED.toString();
							colorDark = EnumChatFormatting.DARK_RED.toString();
						}
						else
						{
							colorLight = EnumChatFormatting.GREEN.toString();
							colorDark = EnumChatFormatting.DARK_GREEN.toString();
						}
						builder.append(colorDark).append(var10).append(colorLight).append(var11).append(colorDark).append(var12);
					}
					par3List.add(builder.toString());
				}
				/*
				 * Advanced Potion Info
				 */
				if (Minestrappolation.advancedPotionInfo && Keyboard.isKeyDown(Keyboard.KEY_CAPITAL))
				{
					List<String> usedTo = PotionUtils.getUsedTo(par1ItemStack);
					if (!usedTo.isEmpty() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
					{
						par3List.add(StatCollector.translateToLocal("potion.useto") + ":");
						par3List.addAll(usedTo);
					}
					else
					{
						if (var5.size() == 1 && Minestrappolation.CLASHSOFT_API() && Minestrappolation.MORE_POTIONS_MOD())
						{
							for (Brewing b : var5)
							{
								if (b.getEffect() != null)
								{
									String desc = b.getEffect().getEffectName() + ".description";
									String s = StatCollector.translateToLocal(desc);
									par3List.add(EnumChatFormatting.RED + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("potion.description.missing"));						
								}
							}
						}
						if (var5.size() > 1)
						{
							String green = (EnumChatFormatting.GREEN) + "\u00a7o";
							String red = (EnumChatFormatting.RED) + "\u00a7o";

							int goodEffects = PotionUtils.getGoodEffects(var5);
							float goodEffectsPercentage = (float) goodEffects / (float) var5.size() * 100;
							int badEffects = PotionUtils.getBadEffects(var5);
							float badEffectsPercentage = (float) badEffects / (float) var5.size() * 100;
							int averageAmplifier = PotionUtils.getAverageAmplifier(var5);
							int averageDuration = PotionUtils.getAverageDuration(var5);
							int maxAmplifier = PotionUtils.getMaxAmplifier(var5);
							int maxDuration = PotionUtils.getMaxDuration(var5);

							if (goodEffects > 1)
								par3List.add((EnumChatFormatting.GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.goodeffects") + ": " + green + goodEffects + " (" + String.format("%.1f", goodEffectsPercentage) + "%)");
							if (badEffects > 1)
								par3List.add((EnumChatFormatting.GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.negativeEffects") + ": " + red + badEffects + " (" + String.format("%.1f", badEffectsPercentage) + "%)");
							if (averageAmplifier > 0)
								par3List.add((EnumChatFormatting.GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.averageamplifier") + ": " + (EnumChatFormatting.DARK_GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.potency." + averageAmplifier));
							par3List.add((EnumChatFormatting.GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.averageduration") + ": " + (EnumChatFormatting.DARK_GRAY) + "\u00a7o" + Potion.getDurationString(new PotionEffect(0, averageDuration, 0)));
							if (maxAmplifier > 0)
								par3List.add((EnumChatFormatting.GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.highestamplifier") + ": " + (EnumChatFormatting.DARK_GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.potency." + maxAmplifier));
							par3List.add((EnumChatFormatting.GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.highestduration") + ": " + (EnumChatFormatting.DARK_GRAY) + "\u00a7o" + Potion.getDurationString(new PotionEffect(0, maxDuration, 0)));
						}
						if (Brewing.getExperience(par1ItemStack) > 0.3F)
						{
							par3List.add((EnumChatFormatting.GRAY) + "\u00a7o" + StatCollector.translateToLocal("potion.value") + ": " + (EnumChatFormatting.YELLOW) + "\u00a7o" + String.format("%.2f", (Brewing.getExperience(par1ItemStack) * 100F) / 270.870F));
						}
					}
				}
				/*
				 * Attribute List
				 */
				if (!hashmultimap.isEmpty())
				{
					par3List.add("");
					par3List.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));

					for (String key : hashmultimap.keys())
					{
						for (AttributeModifier attributemodifier2 : hashmultimap.get(key))
						{
							int op = attributemodifier2.getOperation();
							double d0 = attributemodifier2.getAmount();
							double d1;

							if (op != 1 && op != 2)
							{
								d1 = d0;
							}
							else
							{
								d1 = d0 * 100.0D;
							}

							if (d0 > 0.0D)
							{
								par3List.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + op, new Object[] { ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + key) }));
							}
							else if (d0 < 0.0D)
							{
								d1 *= -1.0D;
								par3List.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + op, new Object[] { ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + key) }));
							}
						}
					}
				}
			}
			else
			{
				String var6 = StatCollector.translateToLocal("potion.empty").trim();
				par3List.add("\u00a77" + var6);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack, int par1)
	{
		List var2 = this.getEffects(par1ItemStack);
		return var2 != null && !var2.isEmpty() && ((Brewing) var2.get(0)).getEffect() != null && par1 == 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		if (par2CreativeTabs == CreativeTabs.tabBrewing || par2CreativeTabs == CreativeTabs.tabAllSearch)
		{
			par3List.add(new ItemStack(this, 1, 0));
			ItemStack allEffects1 = new ItemStack(this, 1, 1);
			ItemStack allEffects2 = new ItemStack(this, 1, 2);
			ItemStack good1 = new ItemStack(this, 1, 1);
			ItemStack good2 = new ItemStack(this, 1, 2);
			ItemStack bad1 = new ItemStack(this, 1, 1);
			ItemStack bad2 = new ItemStack(this, 1, 2);

			for (BrewingBase brewing : Brewing.baseBrewings)
			{
				for (int i = 1; i <= 2; i++)
				{
					par3List.add(brewing.addBrewingToItemStack(new ItemStack(this, 1, i)));
				}
			}
			for (Brewing brewing : Brewing.effectBrewings)
			{
				for (int i = 1; i <= 2; i++)
				{
					for (Brewing brewing2 : brewing.getSubTypes())
					{
						Brewing var1 = new Brewing(brewing2.getEffect(), brewing2.getMaxAmplifier(), brewing2.getMaxDuration(), brewing2.getOpposite(), brewing2.getIngredient(), brewing2.getBase());
						if (i == 2 && var1 != null && var1.getEffect() != null && var1.getEffect().getPotionID() > 0)
						{
							var1.setEffect(new PotionEffect(var1.getEffect().getPotionID(), MathHelper.ceiling_double_int(var1.getEffect().getDuration() * 0.75D), var1.getEffect().getAmplifier()));
						}
						par3List.add(var1.addBrewingToItemStack(new ItemStack(this, 1, i)));
					}
				}
			}

			if (Minestrappolation.MORE_POTIONS_MOD())
			{
				for (Brewing brewing : Brewing.goodEffects)
				{
					if (brewing != BrewingList.effectRemove)
					{
						good1 = brewing.addBrewingToItemStack(good1);
						good2 = brewing.addBrewingToItemStack(good2);
					}
				}
				for (Brewing brewing : Brewing.badEffects)
				{
					if (brewing != BrewingList.effectRemove)
					{
						bad1 = brewing.addBrewingToItemStack(bad1);
						bad2 = brewing.addBrewingToItemStack(bad2);
					}
				}
				for (Brewing brewing : Brewing.combinableEffects)
				{
					allEffects1 = brewing.addBrewingToItemStack(allEffects1);
					allEffects2 = brewing.addBrewingToItemStack(allEffects2);
				}

				par3List.add(allEffects1);
				par3List.add(allEffects2);
				par3List.add(good1);
				par3List.add(good2);
				par3List.add(bad1);
				par3List.add(bad2);
			}
		}
		if (Minestrappolation.MORE_POTIONS_MOD() && Minestrappolation.multiPotions && (par2CreativeTabs == Minestrappolation.potions || par2CreativeTabs == CreativeTabs.tabAllSearch))
		{
			for (int i = 1; i <= 2; i++)
			{
				for (Brewing brewing1 : Brewing.combinableEffects)
				{
					for (Brewing brewing2 : Brewing.combinableEffects)
					{
						if (brewing1 != brewing2)
						{
							Brewing var1 = new Brewing(brewing1.getEffect(), brewing1.getMaxAmplifier(), brewing1.getMaxDuration(), brewing1.getOpposite(), brewing1.getIngredient(), brewing1.getBase());
							Brewing var2 = new Brewing(brewing2.getEffect(), brewing2.getMaxAmplifier(), brewing2.getMaxDuration(), brewing2.getOpposite(), brewing2.getIngredient(), brewing2.getBase());
							if (i == 2 && var1 != null && var1.getEffect() != null && var1.getEffect().getPotionID() > 0)
							{
								var1.setEffect(new PotionEffect(var1.getEffect().getPotionID(), MathHelper.ceiling_double_int(var1.getEffect().getDuration() * 0.75D), var1.getEffect().getAmplifier()));
							}
							if (i == 2 && var2 != null && var2.getEffect() != null && var2.getEffect().getPotionID() > 0)
							{
								var2.setEffect(new PotionEffect(var2.getEffect().getPotionID(), MathHelper.ceiling_double_int(var2.getEffect().getDuration() * 0.75D), var2.getEffect().getAmplifier()));
							}
							par3List.add(var2.addBrewingToItemStack(var1.addBrewingToItemStack(new ItemStack(this, 1, i))));
						}
					}
				}
			}
		}
		/*
		 * ItemStack skyPotion =
		 * Brewing.digSpeed.addBrewingToItemStack(Brewing.heal
		 * .addBrewingToItemStack(new ItemStack(this, 1, i)));
		 * skyPotion.setItemName("\u00a7eSky's Butter Potion"); //6, e
		 * par3List.add(skyPotion);
		 */
	}

	public boolean isEffectInstant(ItemStack par1ItemStack)
	{
		List<Brewing> effects = getEffects(par1ItemStack);
		if (effects.size() == 0)
			return false;
		boolean flag = true;
		for (Brewing b : effects)
			flag &= (b.getEffect() != null ? Potion.potionTypes[b.getEffect().getPotionID()].isInstant() : true);
		return flag;
	}

	@Override
	public Entity createEntity(World world, Entity entity, ItemStack itemstack)
	{
		if (entity instanceof EntityPlayer && isSplash(itemstack.getItemDamage()))
		{
			if (!((EntityPlayer) entity).capabilities.isCreativeMode)
			{
				--itemstack.stackSize;
			}

			world.playSoundAtEntity((entity), "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			Entity e = new EntityPotion2(world, ((EntityPlayer) entity), itemstack);

			if (!world.isRemote)
			{
				world.spawnEntityInWorld(e);
			}

			return e;
		}
		return null;
	}

	public Icon getSplashIcon(ItemStack stack)
	{
		return splashbottle;
	}
}