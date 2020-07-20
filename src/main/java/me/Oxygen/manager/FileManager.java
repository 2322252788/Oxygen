package me.Oxygen.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import me.Oxygen.Oxygen;
import me.Oxygen.modules.Module;
import me.Oxygen.utils.other.Friend;
import me.Oxygen.value.Value;
import net.minecraft.client.Minecraft;

public class FileManager {
	private Minecraft mc = Minecraft.getMinecraft();
    private static String fileDir;
    
    public FileManager() {
    	Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " " + "Config Loading...");
        fileDir = String.valueOf((Object)this.mc.mcDataDir.getAbsolutePath()) + "/" + Oxygen.INSTANCE.CLIENT_NAME;
        File fileFolder = new File(fileDir);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        
        try {
            this.loadKeys();
            this.loadValues();
            this.loadMods();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveKeys() {
        File f = new File(String.valueOf(fileDir) + "/Keys.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Module m : Oxygen.INSTANCE.ModMgr.getModules()) {
                String keyName = m.getKeyCode() < 0 ? "None" : Keyboard.getKeyName((int)m.getKeyCode());
                pw.write(String.valueOf((Object)m.getName()) + ":" + keyName + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadKeys() throws IOException {
    	Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " Loading Key Config...");
        File f = new File(String.valueOf(fileDir) + "/Keys.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            BufferedReader br = new BufferedReader((Reader)new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains((CharSequence)":")) continue;
                String[] split = line.split(":");
                Module m = Oxygen.INSTANCE.ModMgr.getModuleByName((String)split[0]);
                int key = Keyboard.getKeyIndex((String)split[1]);
                if (m == null || key == -1) continue;
                m.setKeyCode(key);
            }
            
        }
    }
    
    public void saveFriends() {
        File f = new File(fileDir + "/Friends.txt");

        try {
           if (!f.exists()) {
              f.createNewFile();
           }

           PrintWriter pw = new PrintWriter(f);
           Iterator var4 = FriendManager.getFriends().iterator();

           while(var4.hasNext()) {
              Friend friend = (Friend)var4.next();
              pw.print(friend.getName() + ":" + friend.getAlias() + "\n");
           }

           pw.close();
        } catch (Exception var5) {
           var5.printStackTrace();
        }

     }

     public void loadFriends() throws IOException {
        File f = new File(this.fileDir + "/Friends.txt");
        if (!f.exists()) {
           f.createNewFile();
        } else {
           BufferedReader br = new BufferedReader(new FileReader(f));

           String line;
           while((line = br.readLine()) != null) {
              if (line.contains(":")) {
                 String[] split = line.split(":");
                 if (line.length() >= 2) {
                    Friend friend = new Friend(split[0], split[1]);
                    FriendManager.getFriends().add(friend);
                 }
              }
           }
        }

     }
    
    public void saveMods() {
        File f = new File(String.valueOf((Object)this.fileDir) + "/Mods.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Module m : Oxygen.INSTANCE.ModMgr.getModules()) {
                pw.print(String.valueOf((Object)m.getName()) + ":" + m.isEnabled() + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMods() throws IOException {
    	Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " Loading Mods Config...");
        File f = new File(String.valueOf((Object)this.fileDir) + "/Mods.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            BufferedReader br = new BufferedReader((Reader)new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains((CharSequence)":")) continue;
                String[] split = line.split(":");
                Module m = Oxygen.INSTANCE.ModMgr.getModuleByName((String)split[0]);
                boolean state = Boolean.parseBoolean((String)split[1]);
                if (m == null) continue;
                m.set(state);
            }
        }
    }
    
    public void saveValues() {
        File f = new File(String.valueOf(fileDir) + "/Values.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            for (Value value : Value.list) {
                String valueName = value.getValueName();
                if (value.isValueBoolean) {
                    pw.print(String.valueOf((Object)valueName) + ":B:" + value.getValueState() + "\n");
                    continue;
                }
                if (value.isValueDouble) {
                    pw.print(String.valueOf((Object)valueName) + ":D:" + value.getValueState() + "\n");
                    continue;
                }
                if (!value.isValueMode) continue;
                pw.print(String.valueOf((Object)valueName) + ":S:" + value.getModeTitle() + ":" + value.getCurrentMode() + "\n");
            }
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadValues() throws IOException {
    	Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " Loading Values Config...");
        File f = new File(String.valueOf((Object)this.fileDir) + "/Values.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            BufferedReader br = new BufferedReader((Reader)new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains((CharSequence)":")) continue;
                String[] split = line.split(":");
                for (Value value : Value.list) {
                    if (!split[0].equalsIgnoreCase(value.getValueName())) continue;
                    if (value.isValueBoolean && split[1].equalsIgnoreCase("B")) {
                        value.setValueState((Object)Boolean.parseBoolean((String)split[2]));
                        continue;
                    }
                    if (value.isValueDouble && split[1].equalsIgnoreCase("D")) {
                        value.setValueState((Object)Double.parseDouble((String)split[2]));
                        continue;
                    }
                    if (!value.isValueMode || !split[1].equalsIgnoreCase("S") || !split[2].equalsIgnoreCase(value.getModeTitle())) continue;
                    value.setCurrentMode(Integer.parseInt((String)split[3]));
                }
            }
        }
    }
}
