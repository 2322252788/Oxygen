package me.Oxygen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import me.Oxygen.event.EventManager;
import me.Oxygen.manager.CommandManager;
import me.Oxygen.manager.FileManager;
import me.Oxygen.manager.ModuleManager;
import me.Oxygen.manager.MusicManager;
import me.Oxygen.ui.clickgui.UIClick;
import me.Oxygen.utils.Liquidbounce.rotation.RotationUtils;
import me.Oxygen.utils.fontRenderer.FontLoader;
import me.Oxygen.utils.world.Check;

/**
 *           [Oxygen]
 * 
 *   "基于Slowly框架制作的Client"
 * 
 *          [2020.6.12]
 *       
 *        @author Rainbow
 */

public class Oxygen {

	public static Oxygen INSTANCE = new Oxygen();
	
	public Logger logger = LogManager.getLogger();
	
	public String CLIENT_NAME = "Oxygen";
	public final String CLIENT_VERSION = "1.0";
	public final String bulid = "2020721";
	public final String copyright = "Private client - " + CLIENT_NAME + " " + CLIENT_VERSION + "- Rainbow";
	
	public EventManager EventMgr;
	public ModuleManager ModMgr;
	public CommandManager ComMgr;
	public FileManager FileMgr;
	public MusicManager MusicMgr;
	public FontLoader font;
	public UIClick clickgui;
	public me.Oxygen.ui.clickgui3.UIClick crink;
	
	public void Start(){
		logger.info("[" + CLIENT_NAME + "]" + " " + " Loading......");
		this.EventMgr = new EventManager();
		this.ModMgr = new ModuleManager();
		this.ComMgr = new CommandManager();
		this.MusicMgr = new MusicManager();
		this.clickgui = new UIClick();
		this.crink = new me.Oxygen.ui.clickgui3.UIClick();
		this.font = new FontLoader();
		this.FileMgr = new FileManager();
		Display.setTitle("Oxygen " + bulid);
		new Check();
		new RotationUtils();
		System.gc();
	}
	
	public void Stop() {
		FileMgr.saveFriends();
		FileMgr.saveKeys();
		FileMgr.saveMods();
		FileMgr.saveValues();
	}

}
