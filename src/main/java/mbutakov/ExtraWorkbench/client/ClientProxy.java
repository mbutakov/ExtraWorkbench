package mbutakov.ExtraWorkbench.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import mbutakov.ExtraWorkbench.CraftFileFirst;
import mbutakov.ExtraWorkbench.Main;
import mbutakov.ExtraWorkbench.client.gui.GuiContainerWorkbench;
import mbutakov.ExtraWorkbench.client.gui.GuiContainerWorkbench2;
import mbutakov.ExtraWorkbench.common.CommonProxy;
import mbutakov.ExtraWorkbench.common.network.PacketCraftItemInWorkbench;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {
	
	public void preInit() {
		super.preInit();
	}

	public void init() {
		super.init();
	}

	
	  
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			return new GuiContainerWorkbench(player.inventory, world, x, y, z,1);
		case 1:
			return new GuiContainerWorkbench2(player.inventory, world, x, y, z,2);
		}
		return null;
	}
	
}
