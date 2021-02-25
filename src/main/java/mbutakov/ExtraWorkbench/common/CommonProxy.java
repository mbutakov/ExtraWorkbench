package mbutakov.ExtraWorkbench.common;

import cpw.mods.fml.common.network.IGuiHandler;
import mbutakov.ExtraWorkbench.CraftFileFirst;
import mbutakov.ExtraWorkbench.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {
	
	public void preInit() {

	}

	public void init() {
		
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		if(ID == 0) {
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO Auto-generated method stub\
		return null;
	}
}
