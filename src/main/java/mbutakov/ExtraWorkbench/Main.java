package mbutakov.ExtraWorkbench;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import mbutakov.ExtraWorkbench.common.CommonProxy;
import mbutakov.ExtraWorkbench.common.blocks.workbenchFirst.BlockWorkbenchEx;
import mbutakov.ExtraWorkbench.common.blocks.workbenchFirst.BlockWorkbenchEx2;
import mbutakov.ExtraWorkbench.common.network.PacketCraftItemInWorkbench;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

@Mod(modid = "ExtraWorkbench", name = "ExtraWorkbench", version = "0.1")
public class Main {
	
	@SidedProxy(clientSide = "mbutakov.ExtraWorkbench.client.ClientProxy", serverSide = "mbutakov.ExtraWorkbench.common.CommonProxy")
	public static CommonProxy proxy;
	public static final String MODID = "ExtraWorkbench";
	public static final String NAME = "ExtraWorkbench";
	public static final String VERSION = "0.1";
	public static final String MC_VERSION = "1.7.10";	
	@Instance(Main.MODID)
	public static Main instance;
    public static Block blockExWorkbench = new BlockWorkbenchEx();;
    public static Block blockExWorkbench2 = new BlockWorkbenchEx2();;
    public static File craftFileDir;
    public static File craftFileDirSecondPath;
    public static File craftFileDirFirstPath;
    public static ArrayList<CraftFileFirst> craftFile = new ArrayList();
    public static ArrayList<CraftFileSecond> craftFileSecond = new ArrayList();
    public static HashMap<CraftFileFirst, ItemStack[]> recipe = new HashMap();
    public static HashMap<CraftFileSecond, ItemStack[]> recipeSecond = new HashMap();
	public static SimpleNetworkWrapper network;
	public static int craftInFirstWb;
	public static int craftInSecondWb;
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit();
	    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
	    GameRegistry.registerBlock(blockExWorkbench, "AmmoWorkbench");
	    GameRegistry.registerBlock(blockExWorkbench2, "UniversalWorkbench");
	    craftFileDirFirstPath = new File(e.getModConfigurationDirectory().getParentFile(), "/ExtraWorkbench/craftFirst.txt");
	    craftFileDir = new File(e.getModConfigurationDirectory().getParentFile(), "/ExtraWorkbench");
	    craftFileDirSecondPath = new File(e.getModConfigurationDirectory().getParentFile(), "/ExtraWorkbench/craftSecond.txt");
	    network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	    network.registerMessage(PacketCraftItemInWorkbench.Handler.class, PacketCraftItemInWorkbench.class, 1, Side.SERVER);
	    LanguageRegistry.addName(blockExWorkbench, "Ammo Workbench");
	    LanguageRegistry.addName(blockExWorkbench2, "Universal Workbench");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init();
	    readFirstConfigFile();
	    readSecondConfigFile();
	}

	
	public static void readFirstConfigFile() {
	      try {
	    	  if (!craftFileDir.exists()) {
	    		   craftFileDir.mkdir();
	    	  }
	            File file = craftFileDirFirstPath;
	            //создаем объект FileReader для объекта File
	            if (file.createNewFile()) {
	            }
	            else {
		            FileReader fr = new FileReader(file);
		            //создаем BufferedReader с существующего FileReader для построчного считывания
		            BufferedReader reader = new BufferedReader(fr);
		            // считаем сначала первую строку
		            String line = reader.readLine();
		            while (line != null) {
		            	new  CraftFileFirst(line);
		                // считываем остальные строки в цикле
		                line = reader.readLine();
		            }
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void readSecondConfigFile() {
	      try {
	            File file = craftFileDirSecondPath;
	            //создаем объект FileReader для объекта File
	            if (file.createNewFile()) {
	            }
	            else {
		            FileReader fr = new FileReader(file);
		            //создаем BufferedReader с существующего FileReader для построчного считывания
		            BufferedReader reader = new BufferedReader(fr);
		            // считаем сначала первую строку
		            String line = reader.readLine();
		            while (line != null) {
		            	new  CraftFileSecond(line);
		                // считываем остальные строки в цикле
		                line = reader.readLine();
		            }
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	
		
}
