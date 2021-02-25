package mbutakov.ExtraWorkbench.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import mbutakov.ExtraWorkbench.CraftFileFirst;
import mbutakov.ExtraWorkbench.CraftFileSecond;
import mbutakov.ExtraWorkbench.Main;
import mbutakov.ExtraWorkbench.common.network.PacketCraftItemInWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiContainerWorkbench2 extends GuiScreen {
	private InventoryPlayer inventory;
	/** The Minecraft instance */
	private Minecraft mc;
	/** The world in which this crafting table resides */
	private World world;
	/** The crafting table co-ordinates */
	private int x, y, z,wb;
	private int guiOriginX, guiOriginY;
	private static final ResourceLocation texture = new ResourceLocation("extraworkbench", "textures/gui/craftGui.png");
	private static int blueprintsScroll = 0;
	/** Recipe scroller */
	private int recipeScroll = 0;
	/** The blueprint that is currently selected */
	private static int selectedBlueprint = 0;
	/** Spins the driveable model */
	private float spinner = 0;
	/** Whether or not the currently selected driveable can be crafted */
	private boolean canCraft = false;
	/** Item renderer */
	private static RenderItem itemRenderer = new RenderItem();
	
	public GuiContainerWorkbench2(InventoryPlayer playerinventory, World w, int i, int j, int k,int wbb) {
		inventory = playerinventory;
		mc = FMLClientHandler.instance().getClient();
		world = w;
		x = i;
		y = j;
		z = k;
		wb = wbb;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.add(new GuiButton(0, width / 2 + 22, height / 2 + 63, 40, 20, "Craft"));
	}  
	
	@Override
	protected void actionPerformed(GuiButton button)
    {
		if(selectedBlueprint < Main.craftFileSecond.size() && canCraft) {
	        if (button.id == 0)
	        {
	        	  Main.network.sendToServer(new PacketCraftItemInWorkbench(selectedBlueprint,wb));
	        }
		}
    }
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		if(selectedBlueprint > Main.craftFileSecond.size() - 1) {
			((GuiButton)buttonList.get(0)).yPosition = 55555 ;
		}else {
			((GuiButton)buttonList.get(0)).yPosition = height / 2 + 63 ;
		}
		
		String recipeName;
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int w = scaledresolution.getScaledWidth();
		int h = scaledresolution.getScaledHeight();
		drawDefaultBackground();
		GL11.glEnable(3042 /*GL_BLEND*/);
		//Bind the background texture
		mc.renderEngine.bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		guiOriginX = w / 2 - 88;
		guiOriginY = h / 2 - 99;
		//Draw the background
		drawTexturedModalRect(guiOriginX, guiOriginY, 0, 0, 176, 198);
		drawString(fontRendererObj, world.getBlock(x, y, z).getLocalizedName(), guiOriginX + 6, guiOriginY + 6, 0xffffff);
		drawString(fontRendererObj, "Ингридиенты", guiOriginX + 6, guiOriginY + 125, 0xffffff);
		for(int m = 0; m < 3; m++)
		{
			for(int n = 0; n < 8; n++)
			{
				//Add the scroll bar position to get the correct driveable
				int blueprintNumber = blueprintsScroll * 8 + 8 * m + n;
				//If this driveable is selected
				if(blueprintNumber == selectedBlueprint)
				{
					//Bind the gui texture and draw in the green highlighted panel behind the driveable item
					mc.renderEngine.bindTexture(texture);
					drawTexturedModalRect(guiOriginX + 8 + n * 18, guiOriginY + 18 + m * 18, 213, 11, 16, 16);
				}
				//If the number is within the bounds of the list
				if(blueprintNumber < Main.craftFileSecond.size())
				{
						ItemStack is = Main.craftFileSecond.get(blueprintNumber).getCraftedItem();
						drawSlotInventory(is, guiOriginX + 8 + n * 18, guiOriginY + 18 + m * 18);
					}
					//Draw the driveable item
			}
		}
		
		//Increment the spinner to spin the driveable. Wheeee!
		spinner++;	
		

		//Make this true and then set it back to false as soon as a problem occurs in finding the required parts
		
		//Return if the selectedBlueprint is invalid
		if(selectedBlueprint >= Main.craftFileSecond.size())
		{
			return;
		}
		canCraft = true;
		
		
		
		if(Main.craftFileSecond.get(selectedBlueprint) != null)
		{
			CraftFileSecond cf = Main.craftFileSecond.get(selectedBlueprint);
			InventoryPlayer temporaryInventory = new InventoryPlayer(null);
			temporaryInventory.copyInventory(inventory);
			for(int r = 0; r < 3; r++)
			{
				for(int c = 0; c < 4; c++)
				{
					//Work out what recipe item this is
					int recipeItemNumber = recipeScroll * 4 + r * 4 + c;
					//If this is actually a valid recipe item
					if(recipeItemNumber < Main.recipeSecond.get(cf).length)
					{
						//Get the itemstack required by the recipe
						ItemStack recipeStack = Main.recipeSecond.get(cf)[recipeItemNumber];
						//The total amount of items found that match this recipe stack
						int totalAmountFound = 0;
						//Iterate over the temporary inventory
						for(int n = 0; n < temporaryInventory.getSizeInventory(); n++)
						{
							//Get the stack in each slot
							ItemStack stackInSlot = temporaryInventory.getStackInSlot(n);
							//If the stack is what we want
							if(stackInSlot != null && stackInSlot.getItem() == recipeStack.getItem() && stackInSlot.getMetadata() == recipeStack.getMetadata())
							{
								//Work out the amount to take from the stack
								int amountFound = Math.min(stackInSlot.stackSize, recipeStack.stackSize - totalAmountFound);
								//Take it
								stackInSlot.stackSize -= amountFound;
								//Check for empty stacks
								if(stackInSlot.stackSize <= 0)
									stackInSlot = null;
								//Put the modified stack back in the inventory
								temporaryInventory.setInventorySlotContents(n, stackInSlot);
								//Increase the amount found counter
								totalAmountFound += amountFound;
								//If we have enough, stop looking
								if(totalAmountFound == recipeStack.stackSize)
									break;
							}
						}
						//If we didn't find enough, give the stack a red outline
						if(totalAmountFound < recipeStack.stackSize)
						{
							mc.renderEngine.bindTexture(texture);
							drawTexturedModalRect(guiOriginX + 8 + c * 18, guiOriginY + 138 + r * 18, 195, 11, 16, 16);
							canCraft = false;
						}
						//Draw the actual item we want
						drawSlotInventory(recipeStack, guiOriginX + 8 + c * 18, guiOriginY + 138 + r * 18);
					}
				}
			}
	
		}
		
		
		
		//If we can't craft it, draw a red box around the craft button
		if(!canCraft)
		{
			mc.renderEngine.bindTexture(texture);
			drawTexturedModalRect(guiOriginX + 108, guiOriginY + 160, 176, 28, 44, 24);
			drawString(fontRendererObj, "Craft", guiOriginX + 116, guiOriginY + 168, 0xa0a0a0);
		}
		//If we can craft it, draw the button for crafting
		else
		{
			super.drawScreen(i, j, f);
		}
		
	}
	
	@Override
	protected void keyTyped(char c, int i)
	{
		if (i == 1 || i == mc.gameSettings.keyBindInventory.getKeyCode())
		{
			mc.thePlayer.closeScreen();
		}
	}
	
	
	@Override
	protected void mouseClicked(int i, int j, int k)
	{
		super.mouseClicked(i, j, k);
		int x = i - guiOriginX;
		int y = j - guiOriginY;
		if (k == 0 || k == 1)
		{
			//Driveable buttons
			for(int m = 0; m < 3; m++)
			{
				for(int n = 0; n < 8; n++)	
				{
					if(x >= 8 + n * 18 && x <= 26 + n * 18 && y >= 18 + 18 * m && y <= 42 + 18 * m)
						selectedBlueprint = blueprintsScroll * 8 + m * 8 + n;
				}
			}
			//Blueprints back button
			if(x >= 158 && x <= 168 && y >= 18 && y <= 27)
			{
				if(blueprintsScroll > 0)
					blueprintsScroll--;
			}
			//Blueprints forwards button
			if(x >= 158 && x <= 168 && y >= 60 && y <= 71)
			{
				if(blueprintsScroll * 8 + 16 < Main.craftFileSecond.size())
					blueprintsScroll++;
			}
			//Return if the selectedBlueprint is invalid
			if(selectedBlueprint >=  Main.craftFileSecond.size())
			{
				return;
			}
			//Recipe back button
			if(x >= 83 && x <= 93 && y >= 141 && y <= 151)
			{
				if(recipeScroll > 0)
					recipeScroll--;
			}
			//Recipe forwards button
			if(x >= 83 && x <= 93 && y >= 177 && y <= 187)
			{
				CraftFileSecond cf = Main.craftFileSecond.get(selectedBlueprint);
				if(cf != null && recipeScroll * 4 + 12 <Main.recipeSecond.get(cf).length)
					recipeScroll++;
			}
		}
	}
	
	private void drawSlotInventory(ItemStack itemstack, int i, int j)
	{
		if(itemstack == null || itemstack.getItem() == null)
			return;
		itemRenderer.renderItemIntoGUI(fontRendererObj, mc.renderEngine, itemstack, i, j);
		itemRenderer.renderItemOverlayIntoGUI(fontRendererObj, mc.renderEngine, itemstack, i, j);
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
}
